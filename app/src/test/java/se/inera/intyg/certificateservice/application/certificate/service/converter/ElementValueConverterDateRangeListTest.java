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

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDateRange;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDateRangeList;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueText;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRangeList;

class ElementValueConverterDateRangeListTest {

  private static final CertificateDataValueDateRangeList CERTIFICATE_DATA_VALUE =
      CertificateDataValueDateRangeList.builder()
          .id("ID")
          .list(
              List.of(
                  CertificateDataValueDateRange.builder()
                      .id("RANGE_ID")
                      .to(LocalDate.now())
                      .from(LocalDate.now().minusDays(1))
                      .build()))
          .build();

  private ElementValueConverterDateRangeList converter;

  @BeforeEach
  void setUp() {
    converter = new ElementValueConverterDateRangeList();
  }

  @Test
  void shallThrowIfTypeIsNotCertificateDataValueDateRangeList() {
    final var certificateDataTextValue = CertificateDataValueText.builder().build();
    final var illegalStateException =
        assertThrows(
            IllegalStateException.class, () -> converter.convert(certificateDataTextValue));
    assertTrue(illegalStateException.getMessage().contains("Invalid value type"));
  }

  @Test
  void shallReturnTypeDateRangeList() {
    assertEquals(CertificateDataValueType.DATE_RANGE_LIST, converter.getType());
  }

  @Test
  void shallConvertId() {
    final var result = converter.convert(CERTIFICATE_DATA_VALUE);
    final var actualResult = (ElementValueDateRangeList) result;
    assertEquals(CERTIFICATE_DATA_VALUE.getId(), actualResult.dateRangeListId().value());
  }

  @Test
  void shallConvertIdOfDateRange() {
    final var result = converter.convert(CERTIFICATE_DATA_VALUE);
    final var actualResult = (ElementValueDateRangeList) result;
    assertEquals(
        CERTIFICATE_DATA_VALUE.getList().get(0).getId(),
        actualResult.dateRangeList().get(0).dateRangeId().value());
  }

  @Test
  void shallConvertTo() {
    final var result = converter.convert(CERTIFICATE_DATA_VALUE);
    final var actualResult = (ElementValueDateRangeList) result;
    assertEquals(
        CERTIFICATE_DATA_VALUE.getList().get(0).getTo(), actualResult.dateRangeList().get(0).to());
  }

  @Test
  void shallConvertFrom() {
    final var result = converter.convert(CERTIFICATE_DATA_VALUE);
    final var actualResult = (ElementValueDateRangeList) result;
    assertEquals(
        CERTIFICATE_DATA_VALUE.getList().get(0).getFrom(),
        actualResult.dateRangeList().get(0).from());
  }
}
