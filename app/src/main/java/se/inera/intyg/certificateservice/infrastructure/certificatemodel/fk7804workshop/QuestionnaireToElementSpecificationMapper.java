/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804workshop;

import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemEnableWhenComponent;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextField;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementLayout;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRule;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.common.model.Code;

/**
 * Maps a FHIR R5 Questionnaire to a list of internal ElementSpecification objects.
 */
@Slf4j
public class QuestionnaireToElementSpecificationMapper {

  private QuestionnaireToElementSpecificationMapper() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Maps all top-level items of a FHIR Questionnaire to ElementSpecifications.
   *
   * <p>The certificate-service frontend organizes the form by categories: every question must
   * belong to a category in order to be rendered. FHIR Questionnaires often expose questions
   * directly at the top level (without a wrapping {@code group}), which would otherwise result in
   * an empty form. To handle this we group consecutive top-level non-group items into a synthetic
   * category so that all questions become visible.
   *
   * @param questionnaire the FHIR Questionnaire resource
   * @return list of ElementSpecification representing the questionnaire structure
   */
  public static List<ElementSpecification> map(Questionnaire questionnaire) {
    if (questionnaire == null || questionnaire.getItem().isEmpty()) {
      return Collections.emptyList();
    }

    return questionnaire.getItem().stream()
        .map(QuestionnaireToElementSpecificationMapper::wrapInCategory)
        .toList();
  }

  private static ElementSpecification wrapInCategory(QuestionnaireItemComponent item) {
    final var inner = mapItem(item);
    return ElementSpecification.builder()
        .id(new ElementId("category." + item.getLinkId()))
        .configuration(
            ElementConfigurationCategory.builder()
                .name(item.getText() != null ? item.getText() : "Frågor")
                .build()
        )
        .rules(inner.rules())
        .children(List.of(inner))
        .build();
  }

  private static ElementSpecification mapItem(QuestionnaireItemComponent item) {
    final var type = item.getType();

    if (type == QuestionnaireItemType.GROUP) {
      return mapGroup(item);
    }

    return mapQuestion(item);
  }

  private static ElementSpecification mapGroup(QuestionnaireItemComponent item) {
    final var children = item.getItem().stream()
        .map(QuestionnaireToElementSpecificationMapper::mapItem)
        .toList();

    return ElementSpecification.builder()
        .id(new ElementId(item.getLinkId()))
        .configuration(
            ElementConfigurationCategory.builder()
                .name(item.getText())
                .build()
        )
        .children(children)
        .build();
  }

  private static ElementSpecification mapQuestion(QuestionnaireItemComponent item) {
    final var elementId = new ElementId(item.getLinkId());
    final var fieldId = new FieldId(toSafeFieldId(item.getLinkId()));

    final var configuration = switch (item.getType()) {
      case STRING -> ElementConfigurationTextField.builder()
          .id(fieldId)
          .name(item.getText())
          .build();
      case TEXT -> ElementConfigurationTextArea.builder()
          .id(fieldId)
          .name(item.getText())
          .build();
      case BOOLEAN -> ElementConfigurationCheckboxBoolean.builder()
          .id(fieldId)
          .name(item.getText())
          .build();
      case DATE, DATETIME -> ElementConfigurationDate.builder()
          .id(fieldId)
          .name(item.getText())
          .build();
      case CODING -> mapChoiceConfiguration(item, fieldId);
      case DISPLAY -> ElementConfigurationTextArea.builder()
          .id(fieldId)
          .name(item.getText())
          .build();
      case INTEGER, DECIMAL -> ElementConfigurationTextField.builder()
          .id(fieldId)
          .name(item.getText())
          .build();
      default -> {
        log.warn("Unsupported questionnaire item type '{}' for linkId '{}', defaulting to text field",
            item.getType(), item.getLinkId());
        yield ElementConfigurationTextField.builder()
            .id(fieldId)
            .name(item.getText())
            .build();
      }
    };

    final var children = item.getItem().stream()
        .map(QuestionnaireToElementSpecificationMapper::mapItem)
        .toList();

    final var rules = buildShowRules(item);

    return ElementSpecification.builder()
        .id(elementId)
        .configuration(configuration)
        .rules(rules)
        .children(children)
        .build();
  }

  private static List<ElementRule> buildShowRules(QuestionnaireItemComponent item) {
    return item.getEnableWhen().stream()
        .filter(QuestionnaireItemEnableWhenComponent::hasAnswerBooleanType)
        .map(ew -> {
          final var questionLinkId = ew.getQuestion();
          final var safeFieldId = toSafeFieldId(questionLinkId);
          final var answerBoolean = ew.getAnswerBooleanType().getValue();
          final var expression = Boolean.TRUE.equals(answerBoolean)
              ? "$" + safeFieldId
              : "!$" + safeFieldId + " && !empty($" + safeFieldId + ")";
          return (ElementRule) ElementRuleExpression.builder()
              .id(new ElementId(questionLinkId))
              .type(ElementRuleType.SHOW)
              .expression(new RuleExpression(expression))
              .build();
        })
        .toList();
  }

  private static String toSafeFieldId(String linkId) {
    return linkId.replace("-", "_");
  }

  private static ElementConfigurationRadioMultipleCode mapChoiceConfiguration(
      QuestionnaireItemComponent item, FieldId fieldId) {
    final var codes = item.getAnswerOption().stream()
        .map(option -> {
          final var coding = option.getValueCoding();
          return new ElementConfigurationCode(
              new FieldId(coding.getCode()),
              coding.getDisplay(),
              new Code(coding.getCode(), coding.getSystem(), coding.getDisplay())
          );
        })
        .toList();

    return ElementConfigurationRadioMultipleCode.builder()
        .id(fieldId)
        .name(item.getText())
        .list(codes)
        .elementLayout(ElementLayout.ROWS)
        .build();
  }
}

