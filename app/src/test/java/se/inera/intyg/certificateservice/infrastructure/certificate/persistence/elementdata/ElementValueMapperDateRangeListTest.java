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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.elementdata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.DateRange;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRangeList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueMapperDateRangeListTest {

  private ElementValueMapperDateRangeList elementValueMapper;

  @BeforeEach
  void setUp() {
    elementValueMapper = new ElementValueMapperDateRangeList();
  }

  @Test
  void shallReturnTrueIfClassMappedIsMappedElementValueDateRangeList() {
    assertTrue(elementValueMapper.supports(MappedElementValueDateRangeList.class));
  }

  @Test
  void shallReturnTrueIfClassElementValueIsElementValueDateRangeList() {
    assertTrue(elementValueMapper.supports(ElementValueDateRangeList.class));
  }

  @Test
  void shallReturnFalseForUnsupportedClass() {
    assertFalse(elementValueMapper.supports(String.class));
  }

  @Test
  void shallMapToDomain() {
    final var to = LocalDate.now();
    final var from = LocalDate.now().minusDays(1);
    final var expectedValue =
        ElementValueDateRangeList.builder()
            .dateRangeListId(new FieldId("ID"))
            .dateRangeList(
                List.of(
                    DateRange.builder()
                        .dateRangeId(new FieldId("RANGE_ID"))
                        .to(to)
                        .from(from)
                        .build()))
            .build();

    final var mappedElementValue =
        MappedElementValueDateRangeList.builder()
            .dateRangeListId("ID")
            .dateRangeList(
                List.of(MappedDateRange.builder().id("RANGE_ID").to(to).from(from).build()))
            .build();

    final var actualValue = elementValueMapper.toDomain(mappedElementValue);

    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallMapToMapped() {
    final var to = LocalDate.now();
    final var from = LocalDate.now().minusDays(1);
    final var expectedValue =
        MappedElementValueDateRangeList.builder()
            .dateRangeListId("ID")
            .dateRangeList(
                List.of(MappedDateRange.builder().id("RANGE_ID").to(to).from(from).build()))
            .build();

    final var elementValue =
        ElementValueDateRangeList.builder()
            .dateRangeListId(new FieldId("ID"))
            .dateRangeList(
                List.of(
                    DateRange.builder()
                        .dateRangeId(new FieldId("RANGE_ID"))
                        .to(to)
                        .from(from)
                        .build()))
            .build();

    final var actualValue = elementValueMapper.toMapped(elementValue);

    assertEquals(expectedValue, actualValue);
  }
}
