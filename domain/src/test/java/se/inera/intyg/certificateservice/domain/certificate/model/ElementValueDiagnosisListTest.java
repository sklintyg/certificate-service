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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ElementValueDiagnosisListTest {

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
