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
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionKontakt.QUESTION_KONTAKT_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionKontakt.QUESTION_KONTAKT_ID;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxBoolean;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;

class QuestionKontaktTest {

  @Test
  void shouldHaveCorrectId() {
    final var element = QuestionKontakt.questionKontakt();
    assertEquals(QUESTION_KONTAKT_ID, element.id());
  }

  @Test
  void shouldHaveCorrectConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCheckboxBoolean.builder()
            .id(QUESTION_KONTAKT_FIELD_ID)
            .name("Kontakt med arbetsgivaren")
            .label(
                "Jag önskar att arbetsgivaren kontaktar vårdenheten. Patienten har lämnat samtycke för kontakt mellan arbetsgivare och vårdgivare.")
            .selectedText("Ja")
            .unselectedText("Ej angivet")
            .build();

    final var element = QuestionKontakt.questionKontakt();
    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shouldIncludeRules() {
    final var element = QuestionKontakt.questionKontakt();
    final var expectedRules = List.of();
    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shouldIncludeValidation() {
    final var element = QuestionKontakt.questionKontakt();
    final var expectedValidations =
        List.of(ElementValidationBoolean.builder().mandatory(false).build());
    assertEquals(expectedValidations, element.validations());
  }
}
