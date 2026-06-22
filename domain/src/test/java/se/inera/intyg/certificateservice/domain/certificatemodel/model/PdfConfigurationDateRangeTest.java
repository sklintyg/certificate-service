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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRange;

class PdfConfigurationDateRangeTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final FieldId FIELD_ID = new FieldId("fieldId");
  private static final PdfFieldId PDF_FROM = new PdfFieldId("fromField");
  private static final PdfFieldId PDF_TO = new PdfFieldId("toField");

  private final Certificate certificate = mock(Certificate.class);

  @Test
  void shallReturnPdfFieldsForFromAndToDates() {
    final var expected =
        List.of(
            PdfField.builder().fieldId(PDF_FROM).value("2026-01-10").build(),
            PdfField.builder().fieldId(PDF_TO).value("2026-01-20").build());

    final var elementValue =
        ElementValueDateRange.builder()
            .id(FIELD_ID)
            .fromDate(LocalDate.of(2026, 1, 10))
            .toDate(LocalDate.of(2026, 1, 20))
            .build();

    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var config = PdfConfigurationDateRange.builder().from(PDF_FROM).to(PDF_TO).build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var result =
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(expected, result);
  }

  @Test
  void shallReturnPdfFieldForFromDateOnly() {
    final var expected = List.of(PdfField.builder().fieldId(PDF_FROM).value("2026-02-01").build());

    final var elementValue =
        ElementValueDateRange.builder().id(FIELD_ID).fromDate(LocalDate.of(2026, 2, 1)).build();

    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var config = PdfConfigurationDateRange.builder().from(PDF_FROM).to(PDF_TO).build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var result =
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(expected, result);
  }

  @Test
  void shallReturnPdfFieldForToDateOnly() {
    final var expected = List.of(PdfField.builder().fieldId(PDF_TO).value("2026-03-15").build());

    final var elementValue =
        ElementValueDateRange.builder().id(FIELD_ID).toDate(LocalDate.of(2026, 3, 15)).build();

    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var config = PdfConfigurationDateRange.builder().from(PDF_FROM).to(PDF_TO).build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var result =
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(expected, result);
  }

  @Test
  void shallReturnEmptyWhenBothDatesAreNull() {
    final var elementValue = ElementValueDateRange.builder().id(FIELD_ID).build();

    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var config = PdfConfigurationDateRange.builder().from(PDF_FROM).to(PDF_TO).build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var result =
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(Collections.emptyList(), result);
  }

  @Test
  void shallReturnEmptyWhenValueIsNotDateRange() {
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var config = PdfConfigurationDateRange.builder().from(PDF_FROM).to(PDF_TO).build();

    doReturn(
            Optional.of(
                ElementData.builder()
                    .id(ELEMENT_ID)
                    .value(mock(ElementValueBoolean.class))
                    .build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var result =
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(Collections.emptyList(), result);
  }

  @Test
  void shallReturnEmptyWhenElementDataDoesNotExist() {
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var config = PdfConfigurationDateRange.builder().from(PDF_FROM).to(PDF_TO).build();

    doReturn(Optional.empty()).when(certificate).getElementDataById(ELEMENT_ID);

    final var result =
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(Collections.emptyList(), result);
  }
}
