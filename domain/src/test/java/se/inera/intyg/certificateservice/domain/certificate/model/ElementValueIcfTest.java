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
package se.inera.intyg.certificateservice.domain.certificate.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationIcf;

class ElementValueIcfTest {

  private static final CharsetEncoder ISO_8859_1_ENCODER = StandardCharsets.ISO_8859_1.newEncoder();
  private static final String INVALID_CHAR = "\u0400";

  @Nested
  class Encoding {

    @Test
    void shouldReturnCanEncodeWhenTextIsNull() {
      final var result = ElementValueIcf.builder().build().encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCanEncodeWhenTextIsEmpty() {
      final var result = ElementValueIcf.builder().text("").build().encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCanEncodeWhenTextIsValid() {
      final var result =
          ElementValueIcf.builder().text("valid text").build().encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCannotEncodeWhenTextHasInvalidChars() {
      final var result =
          ElementValueIcf.builder().text(INVALID_CHAR).build().encoding(ISO_8859_1_ENCODER);
      assertFalse(result.canEncode());
      assertEquals(List.of(INVALID_CHAR), result.invalidChars());
    }
  }

  @Nested
  class FormatIcfValueTextTests {

    @Test
    void shouldReturnTextIfIcfCodesIsEmpty() {
      final var expectedValue = "expectedValue";
      final var elementValueIcf = ElementValueIcf.builder().text(expectedValue).build();

      final var result = elementValueIcf.formatIcfValueText(null);
      assertEquals(expectedValue, result);
    }

    @Test
    void shouldReturnFormattedTextIfIcdCodesHasValue() {
      final var expectedText =
          """
          collectionsLabel icfCode1 - icfCode2

          text
          """;
      final var elementValueIcf =
          ElementValueIcf.builder().text("text").icfCodes(List.of("icfCode1", "icfCode2")).build();

      final var elementConfigurationIcf =
          ElementConfigurationIcf.builder().collectionsLabel("collectionsLabel").build();

      final var result = elementValueIcf.formatIcfValueText(elementConfigurationIcf);
      assertEquals(expectedText, result);
    }
  }
}
