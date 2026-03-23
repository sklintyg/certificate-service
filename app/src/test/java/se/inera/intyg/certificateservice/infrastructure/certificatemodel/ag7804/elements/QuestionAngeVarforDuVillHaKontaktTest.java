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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionAngeVarforDuVillHaKontakt.QUESTION_VARFOR_KONTAKT_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionAngeVarforDuVillHaKontakt.QUESTION_VARFOR_KONTAKT_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionKontakt.QUESTION_KONTAKT_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionKontakt.QUESTION_KONTAKT_ID;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;

class QuestionAngeVarforDuVillHaKontaktTest {

  @Test
  void shouldIncludeId() {
    final var element = QuestionAngeVarforDuVillHaKontakt.questionAngeVarforDuVillHaKontakt();
    assertEquals(QUESTION_VARFOR_KONTAKT_ID, element.id());
  }

  @Test
  void shouldIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationTextArea.builder()
            .id(QUESTION_VARFOR_KONTAKT_FIELD_ID)
            .name(
                "Ange varför du vill ha kontakt och vem som i första hand ska kontaktas samt kontaktuppgifter")
            .build();

    final var element = QuestionAngeVarforDuVillHaKontakt.questionAngeVarforDuVillHaKontakt();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shouldIncludeValidation() {
    final var element = QuestionAngeVarforDuVillHaKontakt.questionAngeVarforDuVillHaKontakt();
    final var expectedValidations =
        List.of(ElementValidationText.builder().mandatory(false).limit(4000).build());
    assertEquals(expectedValidations, element.validations());
  }

  @Test
  void shouldIncludeRules() {
    final var expectedRules =
        List.of(
            ElementRuleExpression.builder()
                .type(ElementRuleType.SHOW)
                .id(QUESTION_KONTAKT_ID)
                .expression(new RuleExpression("$" + QUESTION_KONTAKT_FIELD_ID.value()))
                .build());

    final var element = QuestionAngeVarforDuVillHaKontakt.questionAngeVarforDuVillHaKontakt();

    assertEquals(expectedRules, element.rules());
  }

  @Nested
  class ShouldValidate {

    @Test
    void shallReturnTrueIfBooleanIsTrue() {
      final var elementData =
          List.of(
              ElementData.builder()
                  .id(new ElementId("103"))
                  .value(ElementValueBoolean.builder().value(true).build())
                  .build());

      final var element = QuestionAngeVarforDuVillHaKontakt.questionAngeVarforDuVillHaKontakt();

      final var shouldValidate =
          element.elementSpecification(new ElementId("103.2")).shouldValidate();

      assertTrue(shouldValidate.test(elementData));
    }

    @Test
    void shallReturnFalseIfElementMissing() {
      final var elementData =
          List.of(
              ElementData.builder()
                  .id(new ElementId("8.1"))
                  .value(ElementValueBoolean.builder().value(true).build())
                  .build());

      final var element = QuestionAngeVarforDuVillHaKontakt.questionAngeVarforDuVillHaKontakt();

      final var shouldValidate =
          element.elementSpecification(new ElementId("103.2")).shouldValidate();

      assertFalse(shouldValidate.test(elementData));
    }

    @Test
    void shallReturnFalseIfElementFalse() {
      final var elementData =
          List.of(
              ElementData.builder()
                  .id(new ElementId("103"))
                  .value(ElementValueBoolean.builder().value(false).build())
                  .build());

      final var element = QuestionAngeVarforDuVillHaKontakt.questionAngeVarforDuVillHaKontakt();

      final var shouldValidate =
          element.elementSpecification(new ElementId("103.2")).shouldValidate();

      assertFalse(shouldValidate.test(elementData));
    }
  }

  @Test
  void shouldNotIncludeWhenRenewing() {
    final var elementSpecification =
        QuestionAngeVarforDuVillHaKontakt.questionAngeVarforDuVillHaKontakt();
    assertFalse(elementSpecification.includeWhenRenewing());
  }
}
