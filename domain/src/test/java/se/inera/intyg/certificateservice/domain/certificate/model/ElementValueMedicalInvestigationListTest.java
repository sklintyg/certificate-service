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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ElementValueMedicalInvestigationListTest {

  private static final CharsetEncoder ISO_8859_1_ENCODER = StandardCharsets.ISO_8859_1.newEncoder();
  private static final String INVALID_CHAR = "\u0400";

  @Nested
  class Encoding {

    @Test
    void shouldReturnCanEncodeWhenListIsEmpty() {
      final var result =
          ElementValueMedicalInvestigationList.builder()
              .list(Collections.emptyList())
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCanEncodeWhenInformationSourceIsNull() {
      final var result =
          ElementValueMedicalInvestigationList.builder()
              .list(List.of(MedicalInvestigation.builder().build()))
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCanEncodeWhenInformationSourceTextIsValid() {
      final var result =
          ElementValueMedicalInvestigationList.builder()
              .list(
                  List.of(
                      MedicalInvestigation.builder()
                          .informationSource(ElementValueText.builder().text("valid text").build())
                          .build()))
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCannotEncodeWhenInformationSourceTextHasInvalidChars() {
      final var result =
          ElementValueMedicalInvestigationList.builder()
              .list(
                  List.of(
                      MedicalInvestigation.builder()
                          .informationSource(ElementValueText.builder().text(INVALID_CHAR).build())
                          .build()))
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertFalse(result.canEncode());
      assertEquals(List.of(INVALID_CHAR), result.invalidChars());
    }

    @Test
    void shouldReturnDistinctInvalidCharsAcrossMultipleRows() {
      final var result =
          ElementValueMedicalInvestigationList.builder()
              .list(
                  List.of(
                      MedicalInvestigation.builder()
                          .informationSource(ElementValueText.builder().text(INVALID_CHAR).build())
                          .build(),
                      MedicalInvestigation.builder()
                          .informationSource(ElementValueText.builder().text(INVALID_CHAR).build())
                          .build()))
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertFalse(result.canEncode());
      assertEquals(1, result.invalidChars().size());
    }
  }

  @Nested
  class IsEmpty {

    @Test
    void shouldReturnTrueIfNull() {
      assertTrue(ElementValueMedicalInvestigationList.builder().build().isEmpty());
    }

    @Test
    void shouldReturnFalseIfMedicalInvestigationListWithValue() {
      assertFalse(
          ElementValueMedicalInvestigationList.builder()
              .list(
                  List.of(
                      MedicalInvestigation.builder()
                          .investigationType(ElementValueCode.builder().code("CODE").build())
                          .informationSource(ElementValueText.builder().text("TEXT").build())
                          .date(ElementValueDate.builder().date(LocalDate.now()).build())
                          .build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnFalseIfMedicalInvestigationListWithValueAndEmptyValue() {
      assertFalse(
          ElementValueMedicalInvestigationList.builder()
              .list(
                  List.of(
                      MedicalInvestigation.builder()
                          .investigationType(ElementValueCode.builder().code("CODE").build())
                          .informationSource(ElementValueText.builder().text("TEXT").build())
                          .date(ElementValueDate.builder().date(LocalDate.now()).build())
                          .build(),
                      MedicalInvestigation.builder().build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnTrueIfMedicalInvestigationListWithEmptyValue() {
      assertTrue(
          ElementValueMedicalInvestigationList.builder()
              .list(
                  List.of(
                      MedicalInvestigation.builder()
                          .investigationType(ElementValueCode.builder().build())
                          .informationSource(ElementValueText.builder().build())
                          .date(ElementValueDate.builder().build())
                          .build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnTrueIfEmpty() {
      assertTrue(
          ElementValueMedicalInvestigationList.builder()
              .list(Collections.emptyList())
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnTrueIfOneMissingValue() {
      assertTrue(
          ElementValueMedicalInvestigationList.builder()
              .list(
                  List.of(
                      MedicalInvestigation.builder()
                          .investigationType(ElementValueCode.builder().code("CODE").build())
                          .informationSource(ElementValueText.builder().text("TEXT").build())
                          .date(ElementValueDate.builder().build())
                          .build()))
              .build()
              .isEmpty());
    }
  }
}
