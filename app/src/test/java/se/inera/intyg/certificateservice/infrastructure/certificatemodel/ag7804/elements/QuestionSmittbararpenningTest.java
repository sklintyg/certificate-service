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
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionSmittbararpenning.questionSmittbararpenning;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;

class QuestionSmittbararpenningTest {

  @Test
  void shouldIncludeId() {
    final var element = questionSmittbararpenning();
    assertEquals(new ElementId("27"), element.id());
  }

  @Test
  void shouldIncludeConfiguration() {
    final var expected =
        ElementConfigurationCheckboxBoolean.builder()
            .id(new FieldId("27.1"))
            .label("Förhållningsregler enligt smittskyddslagen på grund av smitta")
            .selectedText("Ja")
            .unselectedText("Ej angivet")
            .build();
    final var element = questionSmittbararpenning();
    assertEquals(expected, element.configuration());
  }

  @Test
  void shouldIncludeValidation() {
    final var expected = List.of(ElementValidationBoolean.builder().mandatory(false).build());
    final var element = questionSmittbararpenning();
    assertEquals(expected, element.validations());
  }
}
