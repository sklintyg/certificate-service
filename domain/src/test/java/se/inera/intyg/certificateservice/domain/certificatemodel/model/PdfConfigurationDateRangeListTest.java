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
import se.inera.intyg.certificateservice.domain.certificate.model.DateRange;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRangeList;

class PdfConfigurationDateRangeListTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final FieldId RANGE_ID = new FieldId("helt");
  private static final PdfFieldId CHECKBOX = new PdfFieldId("form.ksr[0]");
  private static final PdfFieldId FROM = new PdfFieldId("form.from[0]");
  private static final PdfFieldId TO = new PdfFieldId("form.to[0]");
  private static final LocalDate FROM_DATE = LocalDate.of(2026, 1, 1);
  private static final LocalDate TO_DATE = LocalDate.of(2026, 1, 31);

  private final Certificate certificate = mock(Certificate.class);

  @Test
  void shallReturnCheckboxFromAndToFields() {
    final var elementValue =
        ElementValueDateRangeList.builder()
            .dateRangeListId(RANGE_ID)
            .dateRangeList(
                List.of(
                    DateRange.builder().dateRangeId(RANGE_ID).from(FROM_DATE).to(TO_DATE).build()))
            .build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDateRangeList.builder()
            .dateRanges(
                Map.of(
                    RANGE_ID,
                    PdfConfigurationDateRangeCheckbox.builder()
                        .checkbox(CHECKBOX)
                        .from(FROM)
                        .to(TO)
                        .build()))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var expected =
        List.of(
            PdfField.builder().fieldId(CHECKBOX).value(CHECKED_BOX_VALUE).build(),
            PdfField.builder().fieldId(FROM).value(FROM_DATE.toString()).build(),
            PdfField.builder().fieldId(TO).value(TO_DATE.toString()).build());

    assertEquals(
        expected,
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList());
  }

  @Test
  void shallReturnCheckboxOnlyWhenDatesAreNull() {
    final var elementValue =
        ElementValueDateRangeList.builder()
            .dateRangeListId(RANGE_ID)
            .dateRangeList(
                List.of(DateRange.builder().dateRangeId(RANGE_ID).from(null).to(null).build()))
            .build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDateRangeList.builder()
            .dateRanges(
                Map.of(
                    RANGE_ID,
                    PdfConfigurationDateRangeCheckbox.builder()
                        .checkbox(CHECKBOX)
                        .from(FROM)
                        .to(TO)
                        .build()))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var expected =
        List.of(PdfField.builder().fieldId(CHECKBOX).value(CHECKED_BOX_VALUE).build());

    assertEquals(
        expected,
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList());
  }

  @Test
  void shallReturnEmptyForUnsupportedElementValue() {
    final var elementValue = ElementValueBoolean.builder().value(true).build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDateRangeList.builder()
            .dateRanges(
                Map.of(
                    RANGE_ID,
                    PdfConfigurationDateRangeCheckbox.builder()
                        .checkbox(CHECKBOX)
                        .from(FROM)
                        .to(TO)
                        .build()))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    assertEquals(
        Collections.emptyList(),
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList());
  }

  @Test
  void shallReturnEmptyIfElementDataDoesNotExist() {
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDateRangeList.builder()
            .dateRanges(
                Map.of(
                    RANGE_ID,
                    PdfConfigurationDateRangeCheckbox.builder()
                        .checkbox(CHECKBOX)
                        .from(FROM)
                        .to(TO)
                        .build()))
            .build();

    doReturn(Optional.empty()).when(certificate).getElementDataById(ELEMENT_ID);

    assertEquals(
        Collections.emptyList(),
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList());
  }

  @Test
  void shallThrowWhenDateRangeIdNotConfigured() {
    final var unknown = new FieldId("unknown");
    final var elementValue =
        ElementValueDateRangeList.builder()
            .dateRangeListId(RANGE_ID)
            .dateRangeList(
                List.of(
                    DateRange.builder().dateRangeId(unknown).from(FROM_DATE).to(TO_DATE).build()))
            .build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDateRangeList.builder()
            .dateRanges(
                Map.of(
                    RANGE_ID,
                    PdfConfigurationDateRangeCheckbox.builder()
                        .checkbox(CHECKBOX)
                        .from(FROM)
                        .to(TO)
                        .build()))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            config
                .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
                .toList());
  }
}
