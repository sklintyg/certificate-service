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

class EncodingValidatorTest {

  private static final CharsetEncoder ISO_8859_1_ENCODER = StandardCharsets.ISO_8859_1.newEncoder();
  private static final String INVALID_CHAR = "\u0400";

  @Nested
  class CanEncode {

    @Test
    void shouldReturnCanEncodeWhenTextIsNull() {
      final var result = EncodingValidator.canEncode(ISO_8859_1_ENCODER, null);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCanEncodeWhenTextIsEmpty() {
      final var result = EncodingValidator.canEncode(ISO_8859_1_ENCODER, "");
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCanEncodeWhenAllCharsAreValid() {
      final var result = EncodingValidator.canEncode(ISO_8859_1_ENCODER, "hello");
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCannotEncodeWhenTextHasInvalidChars() {
      final var result = EncodingValidator.canEncode(ISO_8859_1_ENCODER, INVALID_CHAR);
      assertFalse(result.canEncode());
    }

    @Test
    void shouldReturnInvalidCharInResult() {
      final var result = EncodingValidator.canEncode(ISO_8859_1_ENCODER, INVALID_CHAR);
      assertEquals(List.of(INVALID_CHAR), result.invalidChars());
    }

    @Test
    void shouldReturnDistinctInvalidChars() {
      final var result =
          EncodingValidator.canEncode(ISO_8859_1_ENCODER, INVALID_CHAR + INVALID_CHAR);
      assertEquals(1, result.invalidChars().size());
    }

    @Test
    void shouldReturnAllDistinctInvalidChars() {
      final var secondInvalidChar = "\u0401";
      final var result =
          EncodingValidator.canEncode(ISO_8859_1_ENCODER, INVALID_CHAR + secondInvalidChar);
      assertEquals(2, result.invalidChars().size());
    }
  }
}
