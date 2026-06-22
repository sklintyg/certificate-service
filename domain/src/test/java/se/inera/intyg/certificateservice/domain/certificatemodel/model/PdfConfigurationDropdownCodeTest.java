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
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.common.model.Code;

@ExtendWith(MockitoExtension.class)
class PdfConfigurationDropdownCodeTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final PdfFieldId PDF_FIELD_ID = new PdfFieldId("pdfFieldId");

  private static final FieldId CODE_ID = new FieldId("ARBETSTERAPEUT");
  private static final FieldId UNKNOWN_CODE_ID = new FieldId("UNKNOWN");

  private static final String CODE = "arbetsterapeut";
  private static final String UNKNOWN_CODE = "unknown";
  private static final String PDF_VALUE = "Arbetsterapeut";

  @Mock private Certificate certificate;
  @Mock private Code code;

  @Test
  void shallReturnPdfFieldWhenCodeIsConfigured() {
    final var expected = List.of(PdfField.builder().fieldId(PDF_FIELD_ID).value(PDF_VALUE).build());

    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var elementValue = ElementValueCode.builder().codeId(CODE_ID).code(CODE).build();

    when(certificate.getElementDataById(ELEMENT_ID))
        .thenReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()));

    final var result =
        PdfConfigurationDropdownCode.builder()
            .fieldId(PDF_FIELD_ID)
            .codes(Map.of(CODE_ID, PDF_VALUE))
            .build()
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(expected, result);
  }

  @Test
  void shallReturnEmptyListWhenElementDataIsMissing() {
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    when(certificate.getElementDataById(ELEMENT_ID)).thenReturn(Optional.empty());

    final var result =
        PdfConfigurationDropdownCode.builder()
            .fieldId(PDF_FIELD_ID)
            .codes(Map.of(CODE_ID, PDF_VALUE))
            .build()
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(List.of(), result);
  }

  @Test
  void shallReturnEmptyListWhenCodeIdIsMissing() {
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var elementValue = ElementValueCode.builder().codeId(null).code(CODE).build();

    when(certificate.getElementDataById(ELEMENT_ID))
        .thenReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()));

    final var result =
        PdfConfigurationDropdownCode.builder()
            .fieldId(PDF_FIELD_ID)
            .codes(Map.of(CODE_ID, PDF_VALUE))
            .build()
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(List.of(), result);
  }

  @Test
  void shallReturnEmptyListWhenCodeIsMissing() {
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var elementValue = ElementValueCode.builder().codeId(CODE_ID).code(null).build();

    when(certificate.getElementDataById(ELEMENT_ID))
        .thenReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()));

    final var result =
        PdfConfigurationDropdownCode.builder()
            .fieldId(PDF_FIELD_ID)
            .codes(Map.of(CODE_ID, PDF_VALUE))
            .build()
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(List.of(), result);
  }

  @Test
  void shallReturnEmptyListWhenCodeIsBlank() {
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var elementValue = ElementValueCode.builder().codeId(CODE_ID).code(" ").build();

    when(certificate.getElementDataById(ELEMENT_ID))
        .thenReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()));

    final var result =
        PdfConfigurationDropdownCode.builder()
            .fieldId(PDF_FIELD_ID)
            .codes(Map.of(CODE_ID, PDF_VALUE))
            .build()
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(List.of(), result);
  }

  @Test
  void shallThrowIfCodeIsNotConfigured() {
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();

    final var elementValue =
        ElementValueCode.builder().codeId(UNKNOWN_CODE_ID).code(UNKNOWN_CODE).build();

    when(certificate.getElementDataById(ELEMENT_ID))
        .thenReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()));

    assertThrows(
        IllegalArgumentException.class,
        () ->
            PdfConfigurationDropdownCode.builder()
                .fieldId(PDF_FIELD_ID)
                .codes(Map.of(CODE_ID, PDF_VALUE))
                .build()
                .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
                .toList());
  }

  @Test
  void shallMapElementConfigurationCodeToDropdownCodes() {
    final var expected = Map.of(CODE_ID, PDF_VALUE);

    final var result =
        PdfConfigurationDropdownCode.fromCodeConfig(
            List.of(new ElementConfigurationCode(CODE_ID, PDF_VALUE, code)));

    assertEquals(expected, result);
  }
}
