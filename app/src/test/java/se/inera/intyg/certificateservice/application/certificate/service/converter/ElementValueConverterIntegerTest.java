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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDate;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueInteger;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueInteger;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueConverterIntegerTest {

  private static final String ID = "integerId";
  private static final Integer VALUE = 123;
  private static final String UNIT = "st";
  private static final CertificateDataValueInteger CERTIFICATE_DATA_INTEGER_VALUE =
      CertificateDataValueInteger.builder().id(ID).value(VALUE).unitOfMeasurement(UNIT).build();
  private static final FieldId INTEGER_ID = new FieldId(ID);
  private ElementValueConverterInteger elementValueConverterInteger;

  @BeforeEach
  void setUp() {
    elementValueConverterInteger = new ElementValueConverterInteger();
  }

  @Test
  void shallThrowIfTypeIsNotCertificateDataValueInteger() {
    final var certificateDataValueDate = CertificateDataValueDate.builder().build();
    final var illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () -> elementValueConverterInteger.convert(certificateDataValueDate));
    assertTrue(illegalStateException.getMessage().contains("Invalid value type"));
  }

  @Test
  void shallReturnTypeInteger() {
    assertEquals(CertificateDataValueType.INTEGER, elementValueConverterInteger.getType());
  }

  @Test
  void shallConvertId() {
    final var result = elementValueConverterInteger.convert(CERTIFICATE_DATA_INTEGER_VALUE);
    final var actualResult = (ElementValueInteger) result;
    assertEquals(INTEGER_ID, actualResult.integerId());
  }

  @Test
  void shallConvertValue() {
    final var result = elementValueConverterInteger.convert(CERTIFICATE_DATA_INTEGER_VALUE);
    final var actualResult = (ElementValueInteger) result;
    assertEquals(VALUE, actualResult.value());
  }

  @Test
  void shallConvertUnitOfMeasurement() {
    final var result = elementValueConverterInteger.convert(CERTIFICATE_DATA_INTEGER_VALUE);
    final var actualResult = (ElementValueInteger) result;
    assertEquals(UNIT, actualResult.unitOfMeasurement());
  }
}
