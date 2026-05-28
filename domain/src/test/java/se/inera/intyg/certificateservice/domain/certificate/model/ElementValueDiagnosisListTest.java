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
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ElementValueDiagnosisListTest {

  private static final CharsetEncoder ISO_8859_1_ENCODER = StandardCharsets.ISO_8859_1.newEncoder();
  private static final String INVALID_CHAR = "\u0400";

  @Nested
  class Encoding {

    @Test
    void shouldReturnCanEncodeWhenDiagnosesIsEmpty() {
      final var result = ElementValueDiagnosisList.builder().build().encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCanEncodeWhenDescriptionIsNull() {
      final var result =
          ElementValueDiagnosisList.builder()
              .diagnoses(List.of(ElementValueDiagnosis.builder().build()))
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCanEncodeWhenDescriptionIsValid() {
      final var result =
          ElementValueDiagnosisList.builder()
              .diagnoses(
                  List.of(
                      ElementValueDiagnosis.builder()
                          .code("J22")
                          .description("valid description")
                          .build()))
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCannotEncodeWhenDescriptionHasInvalidChars() {
      final var result =
          ElementValueDiagnosisList.builder()
              .diagnoses(
                  List.of(
                      ElementValueDiagnosis.builder()
                          .code("J22")
                          .description(INVALID_CHAR)
                          .build()))
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertFalse(result.canEncode());
      assertEquals(List.of(INVALID_CHAR), result.invalidChars());
    }

    @Test
    void shouldReturnDistinctInvalidCharsAcrossMultipleDiagnoses() {
      final var result =
          ElementValueDiagnosisList.builder()
              .diagnoses(
                  List.of(
                      ElementValueDiagnosis.builder().description(INVALID_CHAR).build(),
                      ElementValueDiagnosis.builder().description(INVALID_CHAR).build()))
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertFalse(result.canEncode());
      assertEquals(1, result.invalidChars().size());
    }

    @Test
    void shouldNotCheckCodeForEncoding() {
      final var result =
          ElementValueDiagnosisList.builder()
              .diagnoses(
                  List.of(
                      ElementValueDiagnosis.builder()
                          .code(INVALID_CHAR)
                          .description("valid")
                          .build()))
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
    }
  }

  @Nested
  class IsEmpty {

    @Test
    void shouldReturnTrueIfNull() {
      assertTrue(ElementValueDiagnosisList.builder().build().isEmpty());
    }

    @Test
    void shouldReturnFalseIfDiagnosisListWithValue() {
      assertFalse(
          ElementValueDiagnosisList.builder()
              .diagnoses(
                  List.of(
                      ElementValueDiagnosis.builder()
                          .code("DiagnosisList 1")
                          .description("Description")
                          .build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnFalseIfDiagnosisListWithValueAndEmptyValue() {
      assertFalse(
          ElementValueDiagnosisList.builder()
              .diagnoses(
                  List.of(
                      ElementValueDiagnosis.builder()
                          .code("DiagnosisList 1")
                          .description("Description")
                          .build(),
                      ElementValueDiagnosis.builder().build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnTrueIfDiagnosisListWithEmptyValue() {
      assertTrue(
          ElementValueDiagnosisList.builder()
              .diagnoses(List.of(ElementValueDiagnosis.builder().build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnTrueIfEmpty() {
      assertTrue(
          ElementValueDiagnosisList.builder().diagnoses(Collections.emptyList()).build().isEmpty());
    }
  }

}

