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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDateList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class CertificateDataValueConverterCheckboxDateListTest {

  private static final String DATE_LIST_ID = "DATE_LIST_ID";

  private final CertificateDataValueConverterCheckboxDateList converter =
      new CertificateDataValueConverterCheckboxDateList();
  private ElementSpecification configuration;

  @BeforeEach
  void setUp() {
    configuration =
        ElementSpecification.builder()
            .configuration(
                ElementConfigurationCheckboxMultipleDate.builder()
                    .id(new FieldId(DATE_LIST_ID))
                    .build())
            .build();
  }

  @Test
  void shallThrowExceptionIfWrongClassOfValue() {
    final var elementValue = ElementValueText.builder().build();

    assertThrows(IllegalStateException.class, () -> converter.convert(configuration, elementValue));
  }

  @Test
  void shallNotThrowExceptionForWrongClassOfValueIfElementValueIsNull() {
    final var result = converter.convert(configuration, null);
    assertEquals(Collections.emptyList(), ((CertificateDataValueDateList) result).getList());
  }

  @Test
  void shallReturnType() {
    assertEquals(ElementType.CHECKBOX_MULTIPLE_DATE, converter.getType());
  }

  @Test
  void shallReturnDateListId() {
    final var elementValue = ElementValueDateList.builder().build();

    final var result = converter.convert(configuration, elementValue);

    assertEquals(DATE_LIST_ID, ((CertificateDataValueDateList) result).getId());
  }

  @Test
  void shallCreateCertificateDataValueDate() {
    final var elementValue = ElementValueDateList.builder().build();

    final var result = converter.convert(configuration, elementValue);

    assertInstanceOf(CertificateDataValueDateList.class, result);
  }

  @Test
  void shallSetCorrectIdForDateValue() {
    final var elementValue =
        ElementValueDateList.builder()
            .dateList(
                List.of(
                    ElementValueDate.builder()
                        .dateId(new FieldId("DATE_ID"))
                        .date(LocalDate.now())
                        .build()))
            .build();

    final var result = converter.convert(configuration, elementValue);

    assertEquals(
        elementValue.dateList().get(0).dateId().value(),
        ((CertificateDataValueDateList) result).getList().get(0).getId());
  }

  @Test
  void shallSetCorrectDateForDateValue() {
    final var elementValue =
        ElementValueDateList.builder()
            .dateList(
                List.of(
                    ElementValueDate.builder()
                        .dateId(new FieldId("DATE_ID"))
                        .date(LocalDate.now())
                        .build()))
            .build();

    final var result = converter.convert(configuration, elementValue);

    assertEquals(
        elementValue.dateList().get(0).date(),
        ((CertificateDataValueDateList) result).getList().get(0).getDate());
  }

  @Test
  void shallSetValueToEmpty() {
    final var elementValue = ElementValueDateList.builder().build();

    final var result = converter.convert(configuration, elementValue);

    assertTrue(
        ((CertificateDataValueDateList) result).getList().isEmpty(),
        "If no value is provided value should be empty list");
  }
}
