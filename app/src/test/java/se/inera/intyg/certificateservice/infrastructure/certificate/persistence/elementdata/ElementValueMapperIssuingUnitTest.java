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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;

class ElementValueMapperIssuingUnitTest {

  private static final String ADDRESS = "address";
  private static final String ZIP_CODE = "zipCode";
  private static final String CITY = "city";
  private static final String PHONE_NUMBER = "phoneNumber";
  private ElementValueMapperIssuingUnit elementValueMapperIssuingUnit;

  @BeforeEach
  void setUp() {
    elementValueMapperIssuingUnit = new ElementValueMapperIssuingUnit();
  }

  @Test
  void shallSupportClassMappedElementValueIssuingUnit() {
    assertTrue(elementValueMapperIssuingUnit.supports(MappedElementValueIssuingUnit.class));
  }

  @Test
  void shallReturnFalseForUnsupportedClass() {
    assertFalse(elementValueMapperIssuingUnit.supports(String.class));
  }

  @Test
  void shallSupportClassElementValueUnitContactInformation() {
    assertTrue(elementValueMapperIssuingUnit.supports(ElementValueUnitContactInformation.class));
  }

  @Test
  void shallMapToDomain() {
    final var expectedValue =
        ElementValueUnitContactInformation.builder()
            .address(ADDRESS)
            .zipCode(ZIP_CODE)
            .city(CITY)
            .phoneNumber(PHONE_NUMBER)
            .build();

    final var mappedElementValueIssuingUnit =
        MappedElementValueIssuingUnit.builder()
            .address(ADDRESS)
            .zipCode(ZIP_CODE)
            .city(CITY)
            .phoneNumber(PHONE_NUMBER)
            .build();

    final var actualValue = elementValueMapperIssuingUnit.toDomain(mappedElementValueIssuingUnit);

    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallMapToMapped() {
    final var expectedValue =
        MappedElementValueIssuingUnit.builder()
            .address(ADDRESS)
            .zipCode(ZIP_CODE)
            .city(CITY)
            .phoneNumber(PHONE_NUMBER)
            .build();

    final var elementValueUnitContactInformation =
        ElementValueUnitContactInformation.builder()
            .address(ADDRESS)
            .zipCode(ZIP_CODE)
            .city(CITY)
            .phoneNumber(PHONE_NUMBER)
            .build();

    final var actualValue =
        elementValueMapperIssuingUnit.toMapped(elementValueUnitContactInformation);

    assertEquals(expectedValue, actualValue);
  }
}
