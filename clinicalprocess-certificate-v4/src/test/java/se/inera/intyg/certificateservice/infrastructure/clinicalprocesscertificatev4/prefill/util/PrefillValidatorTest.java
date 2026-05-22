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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.PrefillErrorType.INVALID_FORMAT;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class PrefillValidatorTest {

  private static final String QUESTION_ID = "questionId";

  @Nested
  class EncodingTests {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"    ", "Valid ISO text: åäö"})
    void shouldReturnNoErrors(String text) {
      final var result = PrefillValidator.encoding(QUESTION_ID, text);
      assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnErrorIfTextContainsSingleNonIso88591Char() {
      final var result = PrefillValidator.encoding(QUESTION_ID, "invalid\u0100");
      assertEquals(1, result.size());
      assertEquals(INVALID_FORMAT, result.get(0).type());
    }

    @Test
    void shouldReturnErrorIfTextContainsMultipleNonIso88591Chars() {
      final var result = PrefillValidator.encoding(QUESTION_ID, "in\u0100v\u0101lid");
      assertEquals(1, result.size());
      assertEquals(INVALID_FORMAT, result.get(0).type());
    }
  }
}
