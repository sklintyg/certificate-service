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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PrefillValidatorTest {

  @Nested
  class ValidateIso88591 {

    @Test
    void shouldNotThrowIfTextIsNull() {
      assertDoesNotThrow(() -> PrefillValidator.validateIso88591(null));
    }

    @Test
    void shouldNotThrowIfTextIsBlank() {
      assertDoesNotThrow(() -> PrefillValidator.validateIso88591("    "));
    }

    @Test
    void shouldNotThrowIfTextIsValidIso88591() {
      assertDoesNotThrow(() -> PrefillValidator.validateIso88591("Valid ISO text: åäö"));
    }

    @Test
    void shouldThrowIfTextContainsSingleNonIso88591Char() {
      assertThrows(
          IllegalArgumentException.class, () -> PrefillValidator.validateIso88591("invalid\u0100"));
    }

    @Test
    void shouldReturnInvalidFormatErrorIfTextContainsMultipleNonIso88591Chars() {
      assertThrows(
          IllegalArgumentException.class,
          () -> PrefillValidator.validateIso88591("in\u0100v\u0101lid"));
    }
  }
}
