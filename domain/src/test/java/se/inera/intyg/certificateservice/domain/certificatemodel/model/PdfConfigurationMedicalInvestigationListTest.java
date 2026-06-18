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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueMedicalInvestigationList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalInvestigation;

class PdfConfigurationMedicalInvestigationListTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final PdfFieldId DATE_FIELD_ID = new PdfFieldId("field1");
  private static final PdfFieldId SOURCE_FIELD_ID = new PdfFieldId("field2");
  private static final PdfFieldId INVESTIGATION_TYPE_FIELD_ID = new PdfFieldId("field3");
  private static final Map<String, String> INVESTIGATION_OPTIONS =
      Map.of("DIETIST", "Dietist", "ARBETSTERAPEUT", "Arbetsterapeut");
  private static final FieldId FIELD_ID = new FieldId("medicalInvestigation");
  private static final LocalDate DATE_VALUE = LocalDate.now();
  private static final String INVESTIGATION_TYPE_VALUE = "ARBETSTERAPEUT";
  private static final String SOURCE_VALUE = "source";

  private final Certificate certificate = mock(Certificate.class);

  @Test
  void shallEmitPdfFieldsWhenMedicalInvestigationListHasValues() {
    final var expected =
        List.of(
            PdfField.builder().fieldId(DATE_FIELD_ID).value(DATE_VALUE.toString()).build(),
            PdfField.builder().fieldId(SOURCE_FIELD_ID).value(SOURCE_VALUE).build(),
            PdfField.builder()
                .fieldId(INVESTIGATION_TYPE_FIELD_ID)
                .value("Arbetsterapeut")
                .build());

    final var elementSpec =
        ElementSpecification.builder()
            .id(ELEMENT_ID)
            .pdfConfiguration(
                PdfConfigurationMedicalInvestigationList.builder()
                    .list(
                        Map.of(
                            FIELD_ID,
                            PdfConfigurationMedicalInvestigation.builder()
                                .investigationPdfOptions(INVESTIGATION_OPTIONS)
                                .investigationPdfFieldId(INVESTIGATION_TYPE_FIELD_ID)
                                .sourceTypePdfFieldId(SOURCE_FIELD_ID)
                                .datePdfFieldId(DATE_FIELD_ID)
                                .build()))
                    .build())
            .build();

    final var elementValue =
        ElementValueMedicalInvestigationList.builder()
            .list(
                List.of(
                    MedicalInvestigation.builder()
                        .id(FIELD_ID)
                        .date(ElementValueDate.builder().date(DATE_VALUE).build())
                        .informationSource(ElementValueText.builder().text(SOURCE_VALUE).build())
                        .investigationType(
                            ElementValueCode.builder().code(INVESTIGATION_TYPE_VALUE).build())
                        .build()))
            .build();

    when(certificate.getElementDataById(ELEMENT_ID))
        .thenReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()));

    final var result =
        (elementSpec.pdfConfiguration())
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(expected, result);
  }

  @Test
  void shallReturnEmptyStreamWhenNoDateIsProvided() {
    final var elementSpec =
        ElementSpecification.builder()
            .id(ELEMENT_ID)
            .pdfConfiguration(
                PdfConfigurationMedicalInvestigationList.builder()
                    .list(
                        Map.of(
                            FIELD_ID,
                            PdfConfigurationMedicalInvestigation.builder()
                                .investigationPdfOptions(INVESTIGATION_OPTIONS)
                                .investigationPdfFieldId(INVESTIGATION_TYPE_FIELD_ID)
                                .sourceTypePdfFieldId(SOURCE_FIELD_ID)
                                .datePdfFieldId(DATE_FIELD_ID)
                                .build()))
                    .build())
            .build();

    final var elementValue =
        ElementValueMedicalInvestigationList.builder()
            .list(
                List.of(
                    MedicalInvestigation.builder()
                        .id(FIELD_ID)
                        .date(ElementValueDate.builder().build())
                        .informationSource(ElementValueText.builder().build())
                        .investigationType(ElementValueCode.builder().build())
                        .build()))
            .build();

    when(certificate.getElementDataById(ELEMENT_ID))
        .thenReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()));

    final var result =
        (elementSpec.pdfConfiguration())
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(Collections.emptyList(), result);
  }

  @Test
  void shallReturnEmptyStreamWhenNoElementData() {
    final var elementSpec =
        ElementSpecification.builder()
            .id(ELEMENT_ID)
            .pdfConfiguration(
                PdfConfigurationMedicalInvestigationList.builder()
                    .list(
                        Map.of(
                            FIELD_ID,
                            PdfConfigurationMedicalInvestigation.builder()
                                .investigationPdfOptions(INVESTIGATION_OPTIONS)
                                .investigationPdfFieldId(INVESTIGATION_TYPE_FIELD_ID)
                                .sourceTypePdfFieldId(SOURCE_FIELD_ID)
                                .datePdfFieldId(DATE_FIELD_ID)
                                .build()))
                    .build())
            .build();

    when(certificate.getElementDataById(ELEMENT_ID)).thenReturn(Optional.empty());

    final var result =
        elementSpec
            .pdfConfiguration()
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(Collections.emptyList(), result);
  }
}
