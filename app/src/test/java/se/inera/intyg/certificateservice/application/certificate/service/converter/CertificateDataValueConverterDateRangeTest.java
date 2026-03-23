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
import static se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType.DATE_RANGE;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDateRange;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRange;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDateRange;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@ExtendWith(MockitoExtension.class)
class CertificateDataValueConverterDateRangeTest {

  @InjectMocks
  private CertificateDataValueConverterDateRange certificateDataValueConverterDateRange;

  @Test
  void shallReturnTypeDateRange() {
    assertEquals(DATE_RANGE, certificateDataValueConverterDateRange.getType());
  }

  @Test
  void shallThrowIfElementValueIsWrongType() {
    final var wrongValue = ElementValueText.builder().build();
    assertThrows(
        IllegalStateException.class,
        () -> certificateDataValueConverterDateRange.convert(null, wrongValue));
  }

  @Test
  void shallThrowIfConfigurationIsWrongType() {
    final var wrongValue =
        ElementSpecification.builder()
            .configuration(ElementConfigurationTextArea.builder().build())
            .build();

    assertThrows(
        IllegalStateException.class,
        () -> certificateDataValueConverterDateRange.convert(wrongValue, null));
  }

  @Test
  void shallConvertToCertificateDataValueDateRange() {
    final var expected =
        CertificateDataValueDateRange.builder()
            .id("id")
            .from(LocalDate.now())
            .to(LocalDate.now().plusDays(1))
            .build();

    final var specification =
        ElementSpecification.builder()
            .configuration(ElementConfigurationDateRange.builder().id(new FieldId("id")).build())
            .build();

    final var elementValue =
        ElementValueDateRange.builder()
            .fromDate(LocalDate.now())
            .toDate(LocalDate.now().plusDays(1))
            .build();

    final var result = certificateDataValueConverterDateRange.convert(specification, elementValue);
    assertEquals(expected, result);
  }

  @Test
  void shallConvertToCertificateDataValueDateRangeWithNullValues() {
    final var expected = CertificateDataValueDateRange.builder().id("id").build();

    final var specification =
        ElementSpecification.builder()
            .configuration(ElementConfigurationDateRange.builder().id(new FieldId("id")).build())
            .build();

    final var result = certificateDataValueConverterDateRange.convert(specification, null);
    assertEquals(expected, result);
  }
}
