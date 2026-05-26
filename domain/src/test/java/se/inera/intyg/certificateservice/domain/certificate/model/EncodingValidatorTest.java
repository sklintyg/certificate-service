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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EncodingValidatorTest {

  private static final CharsetEncoder ISO_8859_1_ENCODER = StandardCharsets.ISO_8859_1.newEncoder();
  private static final CharsetEncoder US_ASCII_ENCODER = StandardCharsets.US_ASCII.newEncoder();
  private static final String INVALID_CHAR = "Ѐ";

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
      final var secondInvalidChar = "Ё";
      final var result =
          EncodingValidator.canEncode(ISO_8859_1_ENCODER, INVALID_CHAR + secondInvalidChar);
      assertEquals(2, result.invalidChars().size());
    }
  }

  @Nested
  class IsInvalidControlCharacter {

    @ParameterizedTest
    @ValueSource(strings = {"\n", "\r", "\t"})
    void shouldAllowNewlineCarriageReturnAndTab(String allowed) {
      final var result = EncodingValidator.canEncode(US_ASCII_ENCODER, allowed);
      assertTrue(result.canEncode());
    }

    @Test
    void shouldRejectControlCharacterNotInAllowedSet() {
      //  is a C1 control character: ISO control but not in {'\n', '\r', '\t'}
      final var result = EncodingValidator.canEncode(US_ASCII_ENCODER, "");
      assertFalse(result.canEncode());
    }

    @Test
    void shouldNotFilterOutNonControlInvalidChars() {
      // Cyrillic is not an ISO control character so it passes the control filter
      // and is then caught by the encoder check
      final var result = EncodingValidator.canEncode(US_ASCII_ENCODER, INVALID_CHAR);
      assertFalse(result.canEncode());
    }

    @Test
    void shouldAllowTextMixingAllowedControlCharsAndRegularChars() {
      final var result = EncodingValidator.canEncode(US_ASCII_ENCODER, "hello\nworld\r\t");
      assertTrue(result.canEncode());
    }

    @Test
    void shouldRejectTextContainingDisallowedControlCharAmongValidChars() {
      //  (SOH) is an ISO control character not in the allowed set
      final var result = EncodingValidator.canEncode(US_ASCII_ENCODER, "hello\u0001world");
      assertFalse(result.canEncode());
    }
  }
}
