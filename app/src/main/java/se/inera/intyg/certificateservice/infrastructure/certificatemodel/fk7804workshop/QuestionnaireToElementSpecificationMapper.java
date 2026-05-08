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

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.Questionnaire.QuestionnaireItemComponent;
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

    final var topLevel = questionnaire.getItem();
    final var hasAnyGroup = topLevel.stream()
        .anyMatch(item -> item.getType() == QuestionnaireItemType.GROUP);

    if (!hasAnyGroup) {
      final var children = topLevel.stream()
          .map(QuestionnaireToElementSpecificationMapper::mapItem)
          .toList();

      final var syntheticCategory = ElementSpecification.builder()
          .id(new ElementId("category." + questionnaire.getIdPart()))
          .configuration(
              ElementConfigurationCategory.builder()
                  .name(questionnaire.getTitle() != null ? questionnaire.getTitle() : "Frågor")
                  .build()
          )
          .children(children)
          .build();

      return List.of(syntheticCategory);
    }

    return topLevel.stream()
        .map(QuestionnaireToElementSpecificationMapper::mapItem)
        .toList();
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

    final var reparentedChildren = reparentByEnableWhen(children, item.getItem());

    return ElementSpecification.builder()
        .id(new ElementId(item.getLinkId()))
        .configuration(
            ElementConfigurationCategory.builder()
                .name(item.getText())
                .build()
        )
        .children(reparentedChildren)
        .build();
  }

  private static ElementSpecification mapQuestion(QuestionnaireItemComponent item) {
    final var elementId = new ElementId(item.getLinkId());
    final var fieldId = new FieldId(item.getLinkId());

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
        log.warn(
            "Unsupported questionnaire item type '{}' for linkId '{}', defaulting to text field",
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

    final var reparentedChildren = reparentByEnableWhen(children, item.getItem());

    final var rules = buildRules(item, elementId, fieldId);

    return ElementSpecification.builder()
        .id(elementId)
        .configuration(configuration)
        .children(reparentedChildren)
        .rules(rules)
        .build();
  }

  /**
   * Builds ElementRules from the FHIR item's enableWhen conditions and required flag.
   *
   * <p>FHIR enableWhen maps to SHOW rules - the item is only visible when the referenced
   * question has a specific answer. The enableBehavior (all/any) determines if conditions are
   * combined with AND (&&) or OR (||).
   *
   * <p>FHIR required maps to a MANDATORY rule on the item itself.
   */
  private static List<ElementRule> buildRules(QuestionnaireItemComponent item,
      ElementId elementId, FieldId fieldId) {
    final var rules = new ArrayList<ElementRule>();

    if (item.hasEnableWhen()) {
      final var enableWhen = item.getEnableWhen();
      final var useAnd = item.hasEnableBehavior()
          && item.getEnableBehavior() == Questionnaire.EnableWhenBehavior.ALL;

      final var expressions = enableWhen.stream()
          .map(condition -> buildEnableWhenExpression(condition))
          .toList();

      final var combinedExpression = useAnd
          ? String.join(" && ", expressions)
          : String.join(" || ", expressions);

      // The SHOW rule references the element that the condition depends on.
      // If multiple conditions exist, we use the first referenced element as the rule id.
      final var referencedElementId = new ElementId(enableWhen.getFirst().getQuestion());

      rules.add(
          ElementRuleExpression.builder()
              .id(referencedElementId)
              .type(ElementRuleType.SHOW)
              .expression(new RuleExpression(combinedExpression))
              .build()
      );
    }

    if (item.getRequired()) {
      rules.add(
          ElementRuleExpression.builder()
              .id(elementId)
              .type(ElementRuleType.MANDATORY)
              .expression(new RuleExpression("$" + fieldId.value()))
              .build()
      );
    }

    return rules;
  }

  private static String buildEnableWhenExpression(
      Questionnaire.QuestionnaireItemEnableWhenComponent condition) {
    final var questionLinkId = condition.getQuestion();
    final var operator = condition.getOperator();

    return switch (operator) {
      case EXISTS -> "$" + questionLinkId;
      case EQUAL -> {
        final var answer = resolveAnswerValue(condition);
        yield "$" + questionLinkId + " == '" + answer + "'";
      }
      case NOT_EQUAL -> {
        final var answer = resolveAnswerValue(condition);
        yield "$" + questionLinkId + " != '" + answer + "'";
      }
      case GREATER_THAN -> {
        final var answer = resolveAnswerValue(condition);
        yield "$" + questionLinkId + " > '" + answer + "'";
      }
      case LESS_THAN -> {
        final var answer = resolveAnswerValue(condition);
        yield "$" + questionLinkId + " < '" + answer + "'";
      }
      case GREATER_OR_EQUAL -> {
        final var answer = resolveAnswerValue(condition);
        yield "$" + questionLinkId + " >= '" + answer + "'";
      }
      case LESS_OR_EQUAL -> {
        final var answer = resolveAnswerValue(condition);
        yield "$" + questionLinkId + " <= '" + answer + "'";
      }
      default -> "$" + questionLinkId;
    };
  }

  private static String resolveAnswerValue(
      Questionnaire.QuestionnaireItemEnableWhenComponent condition) {
    final var answer = condition.getAnswer();
    if (answer == null) {
      return "";
    }
    if (answer.isBooleanPrimitive()) {
      return answer.primitiveValue();
    }
    if (answer instanceof org.hl7.fhir.r5.model.Coding coding) {
      return coding.getCode();
    }
    return answer.primitiveValue() != null ? answer.primitiveValue() : answer.toString();
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

  /**
   * Reparents items that have enableWhen conditions referencing a sibling item.
   *
   * <p>In FHIR, conditional items are expressed as flat siblings with enableWhen pointing to
   * another sibling. In our internal model, these should be nested as children of the referenced
   * element. This method moves such items from the sibling list into the children of the
   * referenced element.
   *
   * @param mappedItems the already-mapped ElementSpecifications (flat siblings)
   * @param sourceItems the original FHIR QuestionnaireItemComponents (for reading enableWhen)
   * @return restructured list where enableWhen-dependent items are nested under their parent
   */
  private static List<ElementSpecification> reparentByEnableWhen(
      List<ElementSpecification> mappedItems,
      List<QuestionnaireItemComponent> sourceItems) {

    if (mappedItems.isEmpty() || sourceItems.size() != mappedItems.size()) {
      return mappedItems;
    }

    // Collect the linkIds of all siblings for quick lookup
    final var siblingLinkIds = sourceItems.stream()
        .map(QuestionnaireItemComponent::getLinkId)
        .collect(Collectors.toSet());

    // Build a map: parentLinkId -> list of child ElementSpecifications to reparent
    final var reparentMap = new LinkedHashMap<String, List<ElementSpecification>>();
    final var itemsToRemove = new java.util.HashSet<String>();

    for (var i = 0; i < sourceItems.size(); i++) {
      final var sourceItem = sourceItems.get(i);
      if (sourceItem.hasEnableWhen()) {
        final var enableWhen = sourceItem.getEnableWhen();
        // Check if all enableWhen references point to a sibling
        final var referencedSibling = enableWhen.getFirst().getQuestion();
        if (siblingLinkIds.contains(referencedSibling)
            && !referencedSibling.equals(sourceItem.getLinkId())) {
          reparentMap
              .computeIfAbsent(referencedSibling, k -> new ArrayList<>())
              .add(mappedItems.get(i));
          itemsToRemove.add(sourceItem.getLinkId());
        }
      }
    }

    if (reparentMap.isEmpty()) {
      return mappedItems;
    }

    // Rebuild the list: for each item, attach reparented children and exclude moved items
    return mappedItems.stream()
        .filter(spec -> !itemsToRemove.contains(spec.id().id()))
        .map(spec -> {
          final var additionalChildren = reparentMap.get(spec.id().id());
          if (additionalChildren == null) {
            return spec;
          }
          final var mergedChildren = new ArrayList<>(spec.children());
          mergedChildren.addAll(additionalChildren);
          return ElementSpecification.builder()
              .id(spec.id())
              .configuration(spec.configuration())
              .children(mergedChildren)
              .rules(spec.rules())
              .validations(spec.validations())
              .mapping(spec.mapping())
              .pdfConfiguration(spec.pdfConfiguration())
              .shouldValidate(spec.shouldValidate())
              .includeWhenRenewing(spec.includeWhenRenewing())
              .includeInXml(spec.includeInXml())
              .includeForCitizen(spec.includeForCitizen())
              .build();
        })
        .toList();
  }
}
