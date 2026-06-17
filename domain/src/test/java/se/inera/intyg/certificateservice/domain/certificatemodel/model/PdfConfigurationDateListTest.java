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
package se.inera.intyg.certificateservice.domain.certificatemodel.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFormConstants.CHECKED_BOX_VALUE;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateList;

class PdfConfigurationDateListTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final FieldId DATE_ENTRY_ID = new FieldId("undersokning");
  private static final PdfFieldId CHECKBOX_FIELD = new PdfFieldId("form.ksr[0]");
  private static final PdfFieldId DATE_FIELD = new PdfFieldId("form.dat[0]");
  private static final LocalDate DATE = LocalDate.of(2026, 3, 15);

  private final Certificate certificate = mock(Certificate.class);

  @Test
  void shallReturnCheckboxAndDateFields() {
    final var elementValue =
        ElementValueDateList.builder()
            .dateListId(DATE_ENTRY_ID)
            .dateList(List.of(ElementValueDate.builder().dateId(DATE_ENTRY_ID).date(DATE).build()))
            .build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDateList.builder()
            .dateCheckboxes(
                Map.of(
                    DATE_ENTRY_ID,
                    PdfConfigurationDateCheckbox.builder()
                        .checkboxFieldId(CHECKBOX_FIELD)
                        .dateFieldId(DATE_FIELD)
                        .build()))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var expected =
        List.of(
            PdfField.builder().fieldId(CHECKBOX_FIELD).value(CHECKED_BOX_VALUE).build(),
            PdfField.builder().fieldId(DATE_FIELD).value(DATE.toString()).build());

    assertEquals(expected, config.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallReturnEmptyWhenDateIsNull() {
    final var elementValue =
        ElementValueDateList.builder()
            .dateListId(DATE_ENTRY_ID)
            .dateList(List.of(ElementValueDate.builder().dateId(DATE_ENTRY_ID).date(null).build()))
            .build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDateList.builder()
            .dateCheckboxes(
                Map.of(
                    DATE_ENTRY_ID,
                    PdfConfigurationDateCheckbox.builder()
                        .checkboxFieldId(CHECKBOX_FIELD)
                        .dateFieldId(DATE_FIELD)
                        .build()))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    assertEquals(Collections.emptyList(), config.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallReturnEmptyForUnsupportedElementValue() {
    final var elementValue = ElementValueBoolean.builder().value(true).build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDateList.builder()
            .dateCheckboxes(
                Map.of(
                    DATE_ENTRY_ID,
                    PdfConfigurationDateCheckbox.builder()
                        .checkboxFieldId(CHECKBOX_FIELD)
                        .dateFieldId(DATE_FIELD)
                        .build()))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    assertEquals(Collections.emptyList(), config.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallReturnEmptyIfElementDataDoesNotExist() {
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDateList.builder()
            .dateCheckboxes(
                Map.of(
                    DATE_ENTRY_ID,
                    PdfConfigurationDateCheckbox.builder()
                        .checkboxFieldId(CHECKBOX_FIELD)
                        .dateFieldId(DATE_FIELD)
                        .build()))
            .build();

    doReturn(Optional.empty()).when(certificate).getElementDataById(ELEMENT_ID);

    assertEquals(Collections.emptyList(), config.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallThrowWhenDateIdNotConfigured() {
    final var otherId = new FieldId("other");
    final var elementValue =
        ElementValueDateList.builder()
            .dateListId(DATE_ENTRY_ID)
            .dateList(List.of(ElementValueDate.builder().dateId(otherId).date(DATE).build()))
            .build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDateList.builder()
            .dateCheckboxes(
                Map.of(
                    DATE_ENTRY_ID,
                    PdfConfigurationDateCheckbox.builder()
                        .checkboxFieldId(CHECKBOX_FIELD)
                        .dateFieldId(DATE_FIELD)
                        .build()))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    assertThrows(
        IllegalArgumentException.class,
        () -> config.toPdfFields(elementSpec, certificate).toList());
  }
}
