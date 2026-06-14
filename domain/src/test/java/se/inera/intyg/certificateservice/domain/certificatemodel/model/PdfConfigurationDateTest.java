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
import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;

class PdfConfigurationDateTest {
  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final PdfFieldId PDF_FIELD_ID = new PdfFieldId("pdfFieldId");

  private final Certificate certificate = mock(Certificate.class);

  @Test
  void shallReturnPdfFieldIfValueIsDate() {
    final var expected = Optional.of(new PdfField(PDF_FIELD_ID, "2026-06-14"));

    final var elementValue = ElementValueDate.builder().date(LocalDate.of(2026, 6, 14)).build();

    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var config = PdfConfigurationDate.builder().pdfFieldId(PDF_FIELD_ID).build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var result = config.toPdfField(elementSpec, certificate);

    assertEquals(expected, result);
  }

  @Test
  void shallReturnEmptyIfDateIsNull() {
    final var elementValue = ElementValueDate.builder().date(null).build();

    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var config = PdfConfigurationDate.builder().pdfFieldId(PDF_FIELD_ID).build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var result = config.toPdfField(elementSpec, certificate);

    assertEquals(Optional.empty(), result);
  }

  @Test
  void shallReturnEmptyIfValueIsNotDate() {
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var config = PdfConfigurationDate.builder().pdfFieldId(PDF_FIELD_ID).build();

    doReturn(
            Optional.of(
                ElementData.builder()
                    .id(ELEMENT_ID)
                    .value(mock(ElementValueBoolean.class))
                    .build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var result = config.toPdfField(elementSpec, certificate);

    assertEquals(Optional.empty(), result);
  }

  @Test
  void shallReturnEmptyIfElementDataDoesNotExist() {
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var config = PdfConfigurationDate.builder().pdfFieldId(PDF_FIELD_ID).build();

    doReturn(Optional.empty()).when(certificate).getElementDataById(ELEMENT_ID);

    final var result = config.toPdfField(elementSpec, certificate);

    assertEquals(Optional.empty(), result);
  }
}
