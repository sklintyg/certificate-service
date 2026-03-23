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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionKontakt.QUESTION_KONTAKT_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionKontaktBeskrivning.QUESTION_KONTAKT_BESKRIVNING_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionKontaktBeskrivning.QUESTION_KONTAKT_BESKRIVNING_ID;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;

class QuestionKontaktBeskrivningTest {

  @Test
  void shouldHaveCorrectId() {
    final var element = QuestionKontaktBeskrivning.questionKontaktBeskrivning();
    assertEquals(QUESTION_KONTAKT_BESKRIVNING_ID, element.id());
  }

  @Test
  void shouldHaveCorrectConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationTextArea.builder()
            .id(QUESTION_KONTAKT_BESKRIVNING_FIELD_ID)
            .name(
                "Ange varför du vill ha kontakt och vem som i första hand ska kontaktas samt kontaktuppgifter")
            .build();

    final var element = QuestionKontaktBeskrivning.questionKontaktBeskrivning();
    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shouldIncludeRules() {
    final var element = QuestionKontaktBeskrivning.questionKontaktBeskrivning();
    final var expectedRules =
        List.of(
            ElementRuleExpression.builder()
                .id(QUESTION_KONTAKT_ID)
                .type(ElementRuleType.SHOW)
                .expression(new RuleExpression("$9.1"))
                .build(),
            ElementRuleExpression.builder()
                .id(QUESTION_KONTAKT_BESKRIVNING_ID)
                .type(ElementRuleType.MANDATORY)
                .expression(new RuleExpression("$9.2"))
                .build());
    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shouldIncludeValidation() {
    final var element = QuestionKontaktBeskrivning.questionKontaktBeskrivning();
    final var expectedValidations =
        List.of(ElementValidationText.builder().mandatory(true).build());
    assertEquals(expectedValidations, element.validations());
  }

  @Test
  void shouldIncludeMapping() {
    final var element = QuestionKontaktBeskrivning.questionKontaktBeskrivning();
    final var expectedMapping = new ElementMapping(QUESTION_KONTAKT_ID, null);
    assertEquals(expectedMapping, element.mapping());
  }

  @Nested
  class ShouldValidate {

    @Test
    void shouldReturnTrueIfParentQuestionIsTrue() {
      final var elementData =
          List.of(
              ElementData.builder()
                  .id(QUESTION_KONTAKT_ID)
                  .value(ElementValueBoolean.builder().value(true).build())
                  .build());

      final var element = QuestionKontaktBeskrivning.questionKontaktBeskrivning();
      final var shouldValidate = element.shouldValidate();

      assertTrue(shouldValidate.test(elementData));
    }

    @Test
    void shouldReturnFalseIfParentQuestionIsFalse() {
      final var elementData =
          List.of(
              ElementData.builder()
                  .id(QUESTION_KONTAKT_ID)
                  .value(ElementValueBoolean.builder().value(false).build())
                  .build());

      final var element = QuestionKontaktBeskrivning.questionKontaktBeskrivning();
      final var shouldValidate = element.shouldValidate();

      assertFalse(shouldValidate.test(elementData));
    }

    @Test
    void shouldReturnFalseIfParentQuestionIsNull() {
      final var elementData =
          List.of(
              ElementData.builder()
                  .id(QUESTION_KONTAKT_ID)
                  .value(ElementValueBoolean.builder().value(null).build())
                  .build());

      final var element = QuestionKontaktBeskrivning.questionKontaktBeskrivning();
      final var shouldValidate = element.shouldValidate();

      assertFalse(shouldValidate.test(elementData));
    }
  }
}
