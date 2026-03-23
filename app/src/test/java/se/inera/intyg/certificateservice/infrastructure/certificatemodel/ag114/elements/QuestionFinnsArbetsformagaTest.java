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
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionFinnsArbetsformaga.QUESTION_FINNS_ARBETSFORMAGA_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionFinnsArbetsformaga.QUESTION_FINNS_ARBETSFORMAGA_ID;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;

class QuestionFinnsArbetsformagaTest {

  @Test
  void shouldHaveCorrectId() {
    final var element = QuestionFinnsArbetsformaga.questionFinnsArbetsformaga();
    assertEquals(QUESTION_FINNS_ARBETSFORMAGA_ID, element.id());
  }

  @Test
  void shouldHaveCorrectConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationRadioBoolean.builder()
            .id(QUESTION_FINNS_ARBETSFORMAGA_FIELD_ID)
            .name("Finns arbetsförmåga trots sjukdom?")
            .selectedText("Ja")
            .unselectedText("Nej")
            .build();

    final var element = QuestionFinnsArbetsformaga.questionFinnsArbetsformaga();
    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shouldIncludeRules() {
    final var element = QuestionFinnsArbetsformaga.questionFinnsArbetsformaga();
    final var expectedRules =
        List.of(
            ElementRuleExpression.builder()
                .id(QUESTION_FINNS_ARBETSFORMAGA_ID)
                .type(ElementRuleType.MANDATORY)
                .expression(new RuleExpression("exists($6.1)"))
                .build());
    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shouldIncludeValidation() {
    final var element = QuestionFinnsArbetsformaga.questionFinnsArbetsformaga();
    final var expectedValidations =
        List.of(ElementValidationBoolean.builder().mandatory(true).build());
    assertEquals(expectedValidations, element.validations());
  }
}
