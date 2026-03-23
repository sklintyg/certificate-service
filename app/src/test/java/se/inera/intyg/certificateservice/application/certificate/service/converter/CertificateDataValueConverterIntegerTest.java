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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueInteger;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueInteger;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationInteger;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextField;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class CertificateDataValueConverterIntegerTest {

  private static final String ELEMENT_ID = "elementId";
  private static final Integer TEST_VALUE = 42;
  private static final FieldId FIELD_ID = new FieldId("intCode");
  private static final String NAME = "NAME";
  private static final String UNIT_OF_MEASUREMENT = "unitOfMeasurement";
  private final CertificateDataValueConverterInteger converter =
      new CertificateDataValueConverterInteger();
  private final ElementConfigurationInteger elementConfigurationInteger =
      ElementConfigurationInteger.builder()
          .id(FIELD_ID)
          .name(NAME)
          .unitOfMeasurement(UNIT_OF_MEASUREMENT)
          .build();

  @Test
  void shouldThrowExceptionIfWrongClassOfValueIfElementValueNotNull() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(elementConfigurationInteger)
            .build();

    final var elementValueText = ElementValueText.builder().build();

    assertThrows(
        IllegalStateException.class, () -> converter.convert(configuration, elementValueText));
  }

  @Test
  void shouldNotThrowExceptionIfWrongClassOfValueIfElementValueIsNull() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(elementConfigurationInteger)
            .build();

    final var result = converter.convert(configuration, null);
    assertNull(((CertificateDataValueInteger) result).getValue());
  }

  @Test
  void shouldThrowExceptionIfWrongClassOfConfig() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(ElementConfigurationTextField.builder().id(FIELD_ID).build())
            .build();

    final var elementValueInteger = ElementValueInteger.builder().build();

    assertThrows(
        IllegalStateException.class, () -> converter.convert(configuration, elementValueInteger));
  }

  @Test
  void shallReturnType() {
    assertEquals(ElementType.INTEGER, converter.getType());
  }

  @Test
  void shallCreateCertificateDataIntegerValue() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(elementConfigurationInteger)
            .build();

    final var elementValueInteger = ElementValueInteger.builder().build();

    final var result = converter.convert(configuration, elementValueInteger);

    assertInstanceOf(CertificateDataValueInteger.class, result);
  }

  @Test
  void shallSetIdFromConfigurationIntegerValue() {
    final var configuration =
        ElementSpecification.builder().configuration(elementConfigurationInteger).build();

    final var elementValueInteger = ElementValueInteger.builder().build();

    final var result = converter.convert(configuration, elementValueInteger);

    assertEquals(FIELD_ID.value(), ((CertificateDataValueInteger) result).getId());
  }

  @Test
  void shallSetUnitOfMeassurementFromConfiguration() {
    final var configuration =
        ElementSpecification.builder().configuration(elementConfigurationInteger).build();

    final var elementValueInteger = ElementValueInteger.builder().build();

    final var result = converter.convert(configuration, elementValueInteger);

    assertEquals(
        UNIT_OF_MEASUREMENT, ((CertificateDataValueInteger) result).getUnitOfMeasurement());
  }

  @Test
  void shallSetCorrectValueForIntegerValue() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(elementConfigurationInteger)
            .build();

    final var elementValueInteger = ElementValueInteger.builder().value(TEST_VALUE).build();

    final var result = converter.convert(configuration, elementValueInteger);

    assertEquals(TEST_VALUE, ((CertificateDataValueInteger) result).getValue());
  }

  @Test
  void shallSetValueToNull() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(elementConfigurationInteger)
            .build();

    final var elementValueInteger = ElementValueInteger.builder().build();

    final var result = converter.convert(configuration, elementValueInteger);

    assertNull(
        ((CertificateDataValueInteger) result).getValue(),
        "If no value is provided value should be null");
  }
}
