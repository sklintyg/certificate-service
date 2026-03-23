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
package se.inera.intyg.certificateservice.domain.certificatemodel.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;

class ElementConfigurationRadioBooleanTest {

  @Test
  void shouldReturnSimplifiedValueForTrue() {
    final var value = ElementValueBoolean.builder().value(true).build();

    final var config = ElementConfigurationRadioBoolean.builder().selectedText("Ja").build();

    assertEquals("Ja", ((ElementSimplifiedValueText) config.simplified(value).get()).text());
  }

  @Test
  void shouldReturnSimplifiedValueForFalse() {
    final var value = ElementValueBoolean.builder().value(false).build();

    final var config =
        ElementConfigurationRadioBoolean.builder().selectedText("Ja").unselectedText("No").build();

    assertEquals("No", ((ElementSimplifiedValueText) config.simplified(value).get()).text());
  }

  @Test
  void shouldReturnSimplifiedValueForEmpty() {
    final var expected =
        Optional.of(ElementSimplifiedValueText.builder().text("Ej angivet").build());
    final var config =
        ElementConfigurationRadioBoolean.builder().selectedText("Ja").unselectedText("No").build();

    assertEquals(expected, config.simplified(config.emptyValue()));
  }
}
