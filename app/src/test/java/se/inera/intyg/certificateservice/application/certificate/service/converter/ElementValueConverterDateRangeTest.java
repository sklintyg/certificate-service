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
import static se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType.DATE_RANGE;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDate;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDateRange;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRange;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@ExtendWith(MockitoExtension.class)
class ElementValueConverterDateRangeTest {

  @InjectMocks private ElementValueConverterDateRange elementValueConverterDateRange;

  @Test
  void shallReturnTypeDateRange() {
    assertEquals(DATE_RANGE, elementValueConverterDateRange.getType());
  }

  @Test
  void shallThrowIfTypeIsNotCertificateDataValueDateRange() {
    final var wrongType = CertificateDataValueDate.builder().build();
    final var exception =
        assertThrows(
            IllegalStateException.class, () -> elementValueConverterDateRange.convert(wrongType));
    assertEquals("Invalid value type. Type was 'DATE'", exception.getMessage());
  }

  @Test
  void shallConvertToElementValueDateRange() {
    final var value =
        CertificateDataValueDateRange.builder()
            .id("id")
            .from(LocalDate.now())
            .to(LocalDate.now().plusDays(1))
            .build();

    final var expectedResult =
        ElementValueDateRange.builder()
            .id(new FieldId("id"))
            .fromDate(LocalDate.now())
            .toDate(LocalDate.now().plusDays(1))
            .build();

    final var result = elementValueConverterDateRange.convert(value);
    assertEquals(expectedResult, result);
  }
}
