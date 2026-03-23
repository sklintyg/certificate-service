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

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate.ElementConfigurationDateBuilder;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class CertificateDataValueConverterDateTest {

  private static final String ELEMENT_ID = "elementId";
  private static final LocalDate TEST_DATE = LocalDate.of(2021, 5, 20);
  private static final FieldId FIELD_ID = new FieldId("code");
  private final CertificateDataValueConverterDate converter =
      new CertificateDataValueConverterDate();
  private ElementConfigurationDateBuilder elementConfigurationDateBuilder;

  @BeforeEach
  void setUp() {
    elementConfigurationDateBuilder = ElementConfigurationDate.builder().id(FIELD_ID);
  }

  @Test
  void shouldThrowExceptionIfWrongClassOfValueIfElementValueNotNull() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(ElementConfigurationDate.builder().id(FIELD_ID).build())
            .build();

    final var elementValueDate = ElementValueText.builder().build();

    assertThrows(
        IllegalStateException.class, () -> converter.convert(configuration, elementValueDate));
  }

  @Test
  void shouldNotThrowExceptionIfWrongClassOfValueIfElementValueIsNull() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(ElementConfigurationDate.builder().id(FIELD_ID).build())
            .build();

    final var result = converter.convert(configuration, null);
    assertNull(((CertificateDataValueDate) result).getDate());
  }

  @Test
  void shouldThrowExceptionIfWrongClassOfConfig() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(ElementConfigurationTextArea.builder().id(FIELD_ID).build())
            .build();

    final var elementValueDate = ElementValueDate.builder().build();

    assertThrows(
        IllegalStateException.class, () -> converter.convert(configuration, elementValueDate));
  }

  @Test
  void shallReturnType() {
    assertEquals(ElementType.DATE, converter.getType());
  }

  @Test
  void shallCreateCertificateDataValueDate() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(elementConfigurationDateBuilder.build())
            .build();

    final var elementValueDate = ElementValueDate.builder().build();

    final var result = converter.convert(configuration, elementValueDate);

    assertInstanceOf(CertificateDataValueDate.class, result);
  }

  @Test
  void shallSetIdFromConfigurationDateValue() {
    final var configuration =
        ElementSpecification.builder()
            .configuration(elementConfigurationDateBuilder.build())
            .build();

    final var elementValueDate = ElementValueDate.builder().build();

    final var result = converter.convert(configuration, elementValueDate);

    assertEquals(FIELD_ID.value(), ((CertificateDataValueDate) result).getId());
  }

  @Test
  void shallSetCorrectDateForDateValue() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(elementConfigurationDateBuilder.build())
            .build();

    final var elementValueDate = ElementValueDate.builder().date(TEST_DATE).build();

    final var result = converter.convert(configuration, elementValueDate);

    assertEquals(TEST_DATE, ((CertificateDataValueDate) result).getDate());
  }

  @Test
  void shallSetValueToNull() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(elementConfigurationDateBuilder.build())
            .build();

    final var elementValueDate = ElementValueDate.builder().build();

    final var result = converter.convert(configuration, elementValueDate);

    assertNull(
        ((CertificateDataValueDate) result).getDate(),
        "If no value is provided value should be null");
  }
}
