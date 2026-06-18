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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCodeList;

class PdfConfigurationCodeTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final FieldId CODE_FIELD_ID = new FieldId("AKUT");
  private static final PdfFieldId PDF_FIELD_ID = new PdfFieldId("form.checkbox[0]");
  private static final FieldId CODE_LIST_ID = new FieldId("list");

  private final Certificate certificate = mock(Certificate.class);

  @Test
  void shallReturnCheckedFieldForCode() {
    final var elementValue =
        ElementValueCode.builder().codeId(CODE_FIELD_ID).code(CODE_FIELD_ID.value()).build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationCode.builder().codes(Map.of(CODE_FIELD_ID, PDF_FIELD_ID)).build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var expected =
        List.of(PdfField.builder().fieldId(PDF_FIELD_ID).value(CHECKED_BOX_VALUE).build());

    assertEquals(
        expected,
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList());
  }

  @Test
  void shallReturnCheckedFieldsForCodeList() {
    final var codeA = new FieldId("A");
    final var codeB = new FieldId("B");
    final var pdfA = new PdfFieldId("form.a[0]");
    final var pdfB = new PdfFieldId("form.b[0]");
    final var elementValue =
        ElementValueCodeList.builder()
            .id(CODE_LIST_ID)
            .list(
                List.of(
                    ElementValueCode.builder().codeId(codeA).code("1").build(),
                    ElementValueCode.builder().codeId(codeB).code("2").build()))
            .build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationCode.builder().codes(Map.of(codeA, pdfA, codeB, pdfB)).build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var result =
        config
            .toPdfFields(elementSpec, certificate, CustomPdfSpecification.builder().build())
            .toList();

    assertEquals(2, result.size());
    assertEquals(CHECKED_BOX_VALUE, result.get(0).value());
    assertEquals(CHECKED_BOX_VALUE, result.get(1).value());
  }

  @Test
  void shallReturnEmptyForInvalidCode() {
    final var elementValue = ElementValueCode.builder().codeId(CODE_FIELD_ID).code("").build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationCode.builder().codes(Map.of(CODE_FIELD_ID, PDF_FIELD_ID)).build();

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
  void shallReturnEmptyForUnsupportedElementValue() {
    final var elementValue = ElementValueBoolean.builder().value(true).build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationCode.builder().codes(Map.of(CODE_FIELD_ID, PDF_FIELD_ID)).build();

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
  void shallThrowWhenCodeNotConfigured() {
    final var unknownCodeId = new FieldId("UNKNOWN");
    final var elementValue = ElementValueCode.builder().codeId(unknownCodeId).code("x").build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationCode.builder().codes(Map.of(CODE_FIELD_ID, PDF_FIELD_ID)).build();

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
