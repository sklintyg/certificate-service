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

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueIcf;

class ElementConfigurationIcfTest {

  @Test
  void shouldReturnSimplifiedValue() {
    final var text = "Test text for this test";
    final var value =
        ElementValueIcf.builder().text(text).icfCodes(List.of("code1", "code2")).build();

    final var result =
        """
        %s

        %s"""
            .formatted(String.join(" - ", value.icfCodes()), value.text());

    final var config = ElementConfigurationIcf.builder().build();

    assertEquals(result, ((ElementSimplifiedValueText) config.simplified(value).get()).text());
  }

  @Test
  void shouldReturnSimplifiedValueIfEmpty() {
    final var expected =
        Optional.of(ElementSimplifiedValueText.builder().text("Ej angivet").build());
    final var config = ElementConfigurationIcf.builder().build();

    assertEquals(expected, config.simplified(config.emptyValue()));
  }
}
