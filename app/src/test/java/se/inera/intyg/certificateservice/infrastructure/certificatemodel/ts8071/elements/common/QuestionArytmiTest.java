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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;

class QuestionArytmiTest {

  private static final ElementId ELEMENT_ID = new ElementId("11.5");

  @Test
  void shallIncludeId() {
    final var element = QuestionArytmi.questionArytmi();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationRadioBoolean.builder()
            .name("Har personen eller har personen haft någon arytmi?")
            .description("Här avses inte välbehandlat förmaksflimmer utan synkope.")
            .id(new FieldId("11.5"))
            .selectedText("Ja")
            .unselectedText("Nej")
            .build();

    final var element = QuestionArytmi.questionArytmi();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shallIncludeRules() {
    final var expectedRule =
        List.of(
            ElementRuleExpression.builder()
                .id(ELEMENT_ID)
                .type(ElementRuleType.MANDATORY)
                .expression(new RuleExpression("exists($11.5)"))
                .build(),
            ElementRuleExpression.builder()
                .id(new ElementId("11"))
                .type(ElementRuleType.SHOW)
                .expression(new RuleExpression("$11.1"))
                .build());

    final var element = QuestionArytmi.questionArytmi();

    assertEquals(expectedRule, element.rules());
  }

  @Test
  void shallIncludeValidation() {
    final var expectedValidations =
        List.of(ElementValidationBoolean.builder().mandatory(true).build());

    final var element = QuestionArytmi.questionArytmi();

    assertEquals(expectedValidations, element.validations());
  }

  @Test
  void shallIncludeMapping() {
    final var element = QuestionArytmi.questionArytmi();

    assertEquals(new ElementMapping(new ElementId("11"), null), element.mapping());
  }

  @Nested
  class ShouldValidate {

    @Test
    void shallReturnTrueIfBooleanIsTrue() {
      final var elementData =
          List.of(
              ElementData.builder()
                  .id(new ElementId("11"))
                  .value(ElementValueBoolean.builder().value(true).build())
                  .build());

      final var element = QuestionArytmi.questionArytmi();

      final var shouldValidate = element.elementSpecification(ELEMENT_ID).shouldValidate();

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

      final var element = QuestionArytmi.questionArytmi();

      final var shouldValidate = element.elementSpecification(ELEMENT_ID).shouldValidate();

      assertFalse(shouldValidate.test(elementData));
    }

    @Test
    void shallReturnFalseIfElementFalse() {
      final var elementData =
          List.of(
              ElementData.builder()
                  .id(new ElementId("11"))
                  .value(ElementValueBoolean.builder().value(false).build())
                  .build());

      final var element = QuestionArytmi.questionArytmi();

      final var shouldValidate = element.elementSpecification(ELEMENT_ID).shouldValidate();

      assertFalse(shouldValidate.test(elementData));
    }
  }
}
