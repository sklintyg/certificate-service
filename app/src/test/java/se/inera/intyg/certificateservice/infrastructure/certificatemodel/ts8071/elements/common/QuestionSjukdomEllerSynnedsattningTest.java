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
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;

class QuestionSjukdomEllerSynnedsattningTest {

  private static final ElementId ELEMENT_ID = new ElementId("7");

  @Test
  void shallIncludeId() {
    final var element = QuestionSjukdomEllerSynnedsattning.questionSjukdomEllerSynnedsattning();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationRadioBoolean.builder()
            .name("Finns uppgift om ögonsjukdom eller synnedsättning?")
            .description(
                "Exempel på vanligt förekommande ögonsjukdomar är glaukom, retinopati och retinitis pigmentosa. Exempel på synnedsättning kan vara "
                    + "dubbelseende, syn med enbart ett öga eller plötsligt nedsatt synskärpa.")
            .id(new FieldId("7.1"))
            .selectedText("Ja")
            .unselectedText("Nej")
            .build();

    final var element = QuestionSjukdomEllerSynnedsattning.questionSjukdomEllerSynnedsattning();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shallIncludeRules() {
    final var expectedRule =
        List.of(
            ElementRuleExpression.builder()
                .id(ELEMENT_ID)
                .type(ElementRuleType.MANDATORY)
                .expression(new RuleExpression("exists($7.1)"))
                .build(),
            ElementRuleExpression.builder()
                .id(new ElementId("4"))
                .type(ElementRuleType.SHOW)
                .expression(new RuleExpression("!$4.1 && !empty($4.1)"))
                .build());

    final var element = QuestionSjukdomEllerSynnedsattning.questionSjukdomEllerSynnedsattning();

    assertEquals(expectedRule, element.rules());
  }

  @Test
  void shallIncludeValidation() {
    final var expectedValidations =
        List.of(ElementValidationBoolean.builder().mandatory(true).build());

    final var element = QuestionSjukdomEllerSynnedsattning.questionSjukdomEllerSynnedsattning();

    assertEquals(expectedValidations, element.validations());
  }

  @Nested
  class ShouldValidate {

    @Test
    void shallReturnFalseIfBooleanIsTrue() {
      final var elementData =
          List.of(
              ElementData.builder()
                  .id(new ElementId("4"))
                  .value(ElementValueBoolean.builder().value(true).build())
                  .build());

      final var element = QuestionSjukdomEllerSynnedsattning.questionSjukdomEllerSynnedsattning();

      final var shouldValidate = element.elementSpecification(ELEMENT_ID).shouldValidate();

      assertFalse(shouldValidate.test(elementData));
    }

    @Test
    void shallReturnFalseIfElementMissing() {
      final var elementData =
          List.of(
              ElementData.builder()
                  .id(new ElementId("8.1"))
                  .value(ElementValueBoolean.builder().value(true).build())
                  .build());

      final var element = QuestionSjukdomEllerSynnedsattning.questionSjukdomEllerSynnedsattning();

      final var shouldValidate = element.elementSpecification(ELEMENT_ID).shouldValidate();

      assertFalse(shouldValidate.test(elementData));
    }

    @Test
    void shallReturnTrueIfElementFalse() {
      final var elementData =
          List.of(
              ElementData.builder()
                  .id(new ElementId("4"))
                  .value(ElementValueBoolean.builder().value(false).build())
                  .build());

      final var element = QuestionSjukdomEllerSynnedsattning.questionSjukdomEllerSynnedsattning();

      final var shouldValidate = element.elementSpecification(ELEMENT_ID).shouldValidate();

      assertTrue(shouldValidate.test(elementData));
    }
  }
}
