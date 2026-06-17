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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosis;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosisList;

class PdfConfigurationDiagnosesTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final FieldId DIAGNOSIS_ID = new FieldId("diagnos1");
  private static final PdfFieldId PREFIX = new PdfFieldId("form.diag.prefix");
  private static final PdfFieldId NAME_FIELD = new PdfFieldId("form.diag.name[0]");
  private static final PdfFieldId CODE_1 = new PdfFieldId("form.diag.code[0]");
  private static final PdfFieldId CODE_2 = new PdfFieldId("form.diag.code[1]");
  private static final PdfFieldId CODE_3 = new PdfFieldId("form.diag.code[2]");
  private static final PdfFieldId OVERFLOW = new PdfFieldId("form.diag.overflow");
  private static final String APPEARANCE = "/ArialMT 9.00 Tf 0 g";

  private final Certificate certificate = mock(Certificate.class);

  @Test
  void shallReturnNameFieldWithAppearanceAndOneFieldPerCodeCharacter() {
    final var elementValue =
        ElementValueDiagnosisList.builder()
            .diagnoses(
                List.of(
                    ElementValueDiagnosis.builder()
                        .id(DIAGNOSIS_ID)
                        .terminology("ICD-10")
                        .code("A12")
                        .description("Akut bronkit")
                        .build()))
            .build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDiagnoses.builder()
            .prefix(PREFIX)
            .maxLength(172)
            .appearance(APPEARANCE)
            .overflowSheetFieldId(OVERFLOW)
            .diagnoses(
                Map.of(
                    DIAGNOSIS_ID,
                    PdfConfigurationDiagnosis.builder()
                        .pdfNameFieldId(NAME_FIELD)
                        .pdfCodeFieldIds(List.of(CODE_1, CODE_2, CODE_3))
                        .build()))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var expected =
        List.of(
            PdfField.builder()
                .fieldId(NAME_FIELD)
                .value("Akut bronkit")
                .appearance(APPEARANCE)
                .build(),
            PdfField.builder().fieldId(CODE_1).value("A").build(),
            PdfField.builder().fieldId(CODE_2).value("1").build(),
            PdfField.builder().fieldId(CODE_3).value("2").build());

    assertEquals(expected, config.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallOmitCodeFieldsWhenCodeIsNull() {
    final var elementValue =
        ElementValueDiagnosisList.builder()
            .diagnoses(
                List.of(
                    ElementValueDiagnosis.builder()
                        .id(DIAGNOSIS_ID)
                        .terminology("ICD-10")
                        .code(null)
                        .description("Endast text")
                        .build()))
            .build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDiagnoses.builder()
            .prefix(PREFIX)
            .maxLength(172)
            .appearance(APPEARANCE)
            .overflowSheetFieldId(OVERFLOW)
            .diagnoses(
                Map.of(
                    DIAGNOSIS_ID,
                    PdfConfigurationDiagnosis.builder()
                        .pdfNameFieldId(NAME_FIELD)
                        .pdfCodeFieldIds(List.of(CODE_1, CODE_2, CODE_3))
                        .build()))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var expected =
        List.of(
            PdfField.builder()
                .fieldId(NAME_FIELD)
                .value("Endast text")
                .appearance(APPEARANCE)
                .build());

    assertEquals(expected, config.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallThrowWhenDiagnosisIdNotConfigured() {
    final var unknownId = new FieldId("unknown");
    final var elementValue =
        ElementValueDiagnosisList.builder()
            .diagnoses(
                List.of(
                    ElementValueDiagnosis.builder()
                        .id(unknownId)
                        .terminology("ICD-10")
                        .code("X")
                        .description("x")
                        .build()))
            .build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDiagnoses.builder()
            .prefix(PREFIX)
            .maxLength(172)
            .appearance(APPEARANCE)
            .overflowSheetFieldId(OVERFLOW)
            .diagnoses(
                Map.of(
                    DIAGNOSIS_ID,
                    PdfConfigurationDiagnosis.builder()
                        .pdfNameFieldId(NAME_FIELD)
                        .pdfCodeFieldIds(List.of(CODE_1))
                        .build()))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    assertThrows(
        IllegalArgumentException.class,
        () -> config.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallReturnEmptyForUnsupportedElementValue() {
    final var elementValue = ElementValueBoolean.builder().value(true).build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationDiagnoses.builder()
            .prefix(PREFIX)
            .maxLength(172)
            .appearance(APPEARANCE)
            .overflowSheetFieldId(OVERFLOW)
            .diagnoses(
                Map.of(
                    DIAGNOSIS_ID,
                    PdfConfigurationDiagnosis.builder()
                        .pdfNameFieldId(NAME_FIELD)
                        .pdfCodeFieldIds(List.of(CODE_1))
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
        PdfConfigurationDiagnoses.builder()
            .prefix(PREFIX)
            .maxLength(172)
            .appearance(APPEARANCE)
            .overflowSheetFieldId(OVERFLOW)
            .diagnoses(
                Map.of(
                    DIAGNOSIS_ID,
                    PdfConfigurationDiagnosis.builder()
                        .pdfNameFieldId(NAME_FIELD)
                        .pdfCodeFieldIds(List.of(CODE_1))
                        .build()))
            .build();

    doReturn(Optional.empty()).when(certificate).getElementDataById(ELEMENT_ID);

    assertEquals(Collections.emptyList(), config.toPdfFields(elementSpec, certificate).toList());
  }
}
