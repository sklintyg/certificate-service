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

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ElementValueDateListTest {

  @Nested
  class IsEmpty {

    @Test
    void shouldReturnTrueIfNull() {
      assertTrue(ElementValueDateList.builder().build().isEmpty());
    }

    @Test
    void shouldReturnFalseIfDateListWithValue() {
      assertFalse(
          ElementValueDateList.builder()
              .dateList(List.of(ElementValueDate.builder().date(LocalDate.now()).build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnFalseIfDateListWithValueAndEmptyValue() {
      assertFalse(
          ElementValueDateList.builder()
              .dateList(
                  List.of(
                      ElementValueDate.builder().date(LocalDate.now()).build(),
                      ElementValueDate.builder().build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnTrueIfDateListWithEmptyValue() {
      assertTrue(
          ElementValueDateList.builder()
              .dateList(List.of(ElementValueDate.builder().build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnTrueIfEmpty() {
      assertTrue(
          ElementValueDateList.builder().dateList(Collections.emptyList()).build().isEmpty());
    }
  }
}
