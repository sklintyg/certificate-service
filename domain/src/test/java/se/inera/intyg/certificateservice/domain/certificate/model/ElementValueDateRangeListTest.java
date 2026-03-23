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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueDateRangeListTest {

  private static final LocalDate LAST_TO_DATE = LocalDate.now(ZoneId.systemDefault());

  @Test
  void shallReturnLastDateRangeWhenOnlyOne() {
    final var expectedDateRange =
        DateRange.builder()
            .dateRangeId(new FieldId("last"))
            .from(LAST_TO_DATE.minusDays(10))
            .to(LAST_TO_DATE)
            .build();

    final var dateRangeList =
        ElementValueDateRangeList.builder().dateRangeList(List.of(expectedDateRange)).build();

    assertEquals(expectedDateRange, dateRangeList.lastRange());
  }

  @Test
  void shallReturnLastDateRangeWhenMultiple() {
    final var expectedDateRange =
        DateRange.builder()
            .dateRangeId(new FieldId("last"))
            .from(LAST_TO_DATE.minusDays(10))
            .to(LAST_TO_DATE)
            .build();

    final var dateRangeList =
        ElementValueDateRangeList.builder()
            .dateRangeList(
                List.of(
                    DateRange.builder()
                        .dateRangeId(new FieldId("first"))
                        .from(LAST_TO_DATE.minusDays(40))
                        .to(LAST_TO_DATE.minusDays(31))
                        .build(),
                    DateRange.builder()
                        .dateRangeId(new FieldId("middle"))
                        .from(LAST_TO_DATE.minusDays(30))
                        .to(LAST_TO_DATE.minusDays(21))
                        .build(),
                    expectedDateRange))
            .build();

    assertEquals(expectedDateRange, dateRangeList.lastRange());
  }

  @Test
  void shallReturnNullIfDateRangeListIsNull() {
    final var dateRangeList = ElementValueDateRangeList.builder().build();

    assertNull(dateRangeList.lastRange());
  }

  @Test
  void shallReturnNullIfDateRangeListIsEmpty() {
    final var dateRangeList = ElementValueDateRangeList.builder().build();

    assertNull(dateRangeList.lastRange());
  }

  @Nested
  class IsEmpty {

    @Test
    void shouldReturnTrueIfNull() {
      assertTrue(ElementValueDateRangeList.builder().build().isEmpty());
    }

    @Test
    void shouldReturnFalseIfDateRangeListWithNonEmptyValue() {
      assertFalse(
          ElementValueDateRangeList.builder()
              .dateRangeList(
                  List.of(DateRange.builder().to(LocalDate.now()).from(LocalDate.now()).build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnTrueIfDateRangeListWithEmptyValue() {
      assertTrue(
          ElementValueDateRangeList.builder()
              .dateRangeList(List.of(DateRange.builder().from(LocalDate.now()).build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnFalseIfDateRangeListWithEmptyValueAndFilledValue() {
      assertFalse(
          ElementValueDateRangeList.builder()
              .dateRangeList(
                  List.of(
                      DateRange.builder().to(LocalDate.now()).from(LocalDate.now()).build(),
                      DateRange.builder().build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnFalseIfEmpty() {
      assertTrue(
          ElementValueDateRangeList.builder()
              .dateRangeList(Collections.emptyList())
              .build()
              .isEmpty());
    }
  }
}
