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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;
import org.junit.jupiter.api.Test;

class CustomPdfSpecificationTest {

  private static final String TEMPLATE_PATH = "fk7210/pdf/fk7210_v1.pdf";
  private static final String NO_ADDRESS_TEMPLATE_PATH = "fk7210/pdf/fk7210_v1_no_address.pdf";
  private static final List<PdfFieldId> PATIENT_ID_FIELD_IDS =
      List.of(new PdfFieldId("form1[0].#subform[0].flt_txtPersonNr[0]"));
  private static final PdfSignature SIGNATURE =
      PdfSignature.builder()
          .signaturePageIndex(0)
          .signatureWithAddressTagIndex(new PdfTagIndex(15))
          .signatureWithoutAddressTagIndex(new PdfTagIndex(7))
          .signedDateFieldId(new PdfFieldId("form1[0].#subform[0].flt_datUnderskrift[0]"))
          .build();

  private CustomPdfSpecification specification() {
    return CustomPdfSpecification.builder()
        .pdfTemplatePath(TEMPLATE_PATH)
        .pdfNoAddressTemplatePath(NO_ADDRESS_TEMPLATE_PATH)
        .patientIdFieldIds(PATIENT_ID_FIELD_IDS)
        .signature(SIGNATURE)
        .signatureTextX(100)
        .signatureTextY(50)
        .signatureTextFontSize(12)
        .build();
  }

  @Test
  void shallBeAPdfSpecification() {
    assertInstanceOf(PdfSpecification.class, specification());
  }

  @Test
  void shallIncludePdfTemplatePath() {
    assertEquals(TEMPLATE_PATH, specification().pdfTemplatePath());
  }

  @Test
  void shallIncludePdfNoAddressTemplatePath() {
    assertEquals(NO_ADDRESS_TEMPLATE_PATH, specification().pdfNoAddressTemplatePath());
  }

  @Test
  void shallIncludePatientIdFieldIds() {
    assertEquals(PATIENT_ID_FIELD_IDS, specification().patientIdFieldIds());
  }

  @Test
  void shallIncludeSignature() {
    assertEquals(SIGNATURE, specification().signature());
  }

  @Test
  void shallIncludeSignatureTextX() {
    assertEquals(100, specification().signatureTextX());
  }

  @Test
  void shallIncludeSignatureTextY() {
    assertEquals(50, specification().signatureTextY());
  }

  @Test
  void shallIncludeSignatureTextFontSize() {
    assertEquals(12, specification().signatureTextFontSize());
  }
}
