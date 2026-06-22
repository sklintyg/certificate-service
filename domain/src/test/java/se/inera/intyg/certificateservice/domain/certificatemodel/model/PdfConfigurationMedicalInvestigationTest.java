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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalInvestigation;

class PdfConfigurationMedicalInvestigationTest {

  private static final PdfFieldId DATE_PDF_FIELD_ID = new PdfFieldId("date");
  private static final PdfFieldId SOURCE_TYPE_PDF_FIELD_ID = new PdfFieldId("sourceType");
  private static final PdfFieldId INVESTIGATION_PDF_FIELD_ID = new PdfFieldId("investigation");

  private static final String INVESTIGATION_CODE = "LAB";
  private static final String INVESTIGATION_VALUE = "Laboratorieundersökning";

  @Test
  void shallReturnAllPdfFields() {
    final var expected =
        List.of(
            pdfField(DATE_PDF_FIELD_ID, "2026-06-21"),
            pdfField(SOURCE_TYPE_PDF_FIELD_ID, "Journal"),
            pdfField(INVESTIGATION_PDF_FIELD_ID, INVESTIGATION_VALUE));

    final var result =
        pdfConfiguration()
            .toPdfFields(
                MedicalInvestigation.builder()
                    .date(ElementValueDate.builder().date(LocalDate.of(2026, 6, 21)).build())
                    .informationSource(ElementValueText.builder().text("Journal").build())
                    .investigationType(ElementValueCode.builder().code(INVESTIGATION_CODE).build())
                    .build())
            .toList();

    assertEquals(expected, result);
  }

  @Test
  void shallReturnEmptyListWhenInvestigationIsEmpty() {
    final var result =
        pdfConfiguration().toPdfFields(MedicalInvestigation.builder().build()).toList();

    assertEquals(List.of(), result);
  }

  @Test
  void shallNotIncludeDateFieldWhenDateValueIsNull() {
    final var result =
        pdfConfiguration()
            .toPdfFields(
                MedicalInvestigation.builder().date(ElementValueDate.builder().build()).build())
            .toList();

    assertEquals(List.of(), result);
  }

  @Test
  void shallNotIncludeSourceTypeFieldWhenTextIsEmpty() {
    final var result =
        pdfConfiguration()
            .toPdfFields(
                MedicalInvestigation.builder()
                    .informationSource(ElementValueText.builder().text("").build())
                    .build())
            .toList();

    assertEquals(List.of(), result);
  }

  @Test
  void shallNotIncludeInvestigationFieldWhenCodeIsEmpty() {
    final var result =
        pdfConfiguration()
            .toPdfFields(
                MedicalInvestigation.builder()
                    .investigationType(ElementValueCode.builder().code("").build())
                    .build())
            .toList();

    assertEquals(List.of(), result);
  }

  @Test
  void shallNotIncludeInvestigationFieldWhenCodeIsNotConfigured() {
    final var result =
        pdfConfiguration()
            .toPdfFields(
                MedicalInvestigation.builder()
                    .investigationType(ElementValueCode.builder().code("UNKNOWN").build())
                    .build())
            .toList();

    assertEquals(List.of(), result);
  }

  private static PdfConfigurationMedicalInvestigation pdfConfiguration() {
    return PdfConfigurationMedicalInvestigation.builder()
        .datePdfFieldId(DATE_PDF_FIELD_ID)
        .sourceTypePdfFieldId(SOURCE_TYPE_PDF_FIELD_ID)
        .investigationPdfFieldId(INVESTIGATION_PDF_FIELD_ID)
        .investigationPdfOptions(Map.of(INVESTIGATION_CODE, INVESTIGATION_VALUE))
        .build();
  }

  private static PdfField pdfField(PdfFieldId fieldId, String value) {
    return PdfField.builder().fieldId(fieldId).value(value).build();
  }
}
