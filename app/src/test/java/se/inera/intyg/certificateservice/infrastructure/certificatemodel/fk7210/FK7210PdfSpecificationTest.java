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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210PdfSpecification.PDF_FK_7210_PDF;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210PdfSpecification.PDF_NO_ADDRESS_FK_7210_PDF;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210PdfSpecification.PDF_SIGNATURE_TEXT_FONT_SIZE;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210PdfSpecification.PDF_SIGNATURE_TEXT_X;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210PdfSpecification.PDF_SIGNATURE_TEXT_Y;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndex;

class FK7210PdfSpecificationTest {

  @Test
  void shallReturnCustomPdfSpecification() {
    assertInstanceOf(CustomPdfSpecification.class, FK7210PdfSpecification.create());
  }

  @Test
  void shallIncludePdfTemplatePathWithAddress() {
    assertEquals(PDF_FK_7210_PDF, FK7210PdfSpecification.create().pdfTemplatePath());
  }

  @Test
  void shallIncludePdfTemplatePathNoAddress() {
    assertEquals(
        PDF_NO_ADDRESS_FK_7210_PDF, FK7210PdfSpecification.create().pdfNoAddressTemplatePath());
  }

  @Test
  void shallIncludePatientFieldId() {
    final var expected = List.of(new PdfFieldId("form1[0].#subform[0].flt_txtPersonNr[0]"));

    assertEquals(expected, FK7210PdfSpecification.create().patientIdFieldIds());
  }

  @Test
  void shallIncludeSignatureFields() {
    final var expected =
        PdfSignature.builder()
            .signaturePageIndex(0)
            .signatureWithAddressTagIndex(new PdfTagIndex(15))
            .signatureWithoutAddressTagIndex(new PdfTagIndex(7))
            .signedDateFieldId(new PdfFieldId("form1[0].#subform[0].flt_datUnderskrift[0]"))
            .signedByNameFieldId(new PdfFieldId("form1[0].#subform[0].flt_txtNamnfortydligande[0]"))
            .paTitleFieldId(new PdfFieldId("form1[0].#subform[0].flt_txtBefattning[0]"))
            .specialtyFieldId(
                new PdfFieldId("form1[0].#subform[0].flt_txtEventuellSpecialistkompetens[0]"))
            .hsaIdFieldId(new PdfFieldId("form1[0].#subform[0].flt_txtLakarensHSA-ID[0]"))
            .workplaceCodeFieldId(new PdfFieldId("form1[0].#subform[0].flt_txtArbetsplatskod[0]"))
            .contactInformation(
                new PdfFieldId("form1[0].#subform[0].flt_txtVardgivarensNamnAdressTelefon[0]"))
            .build();

    assertEquals(expected, FK7210PdfSpecification.create().signature());
  }

  @Test
  void shallIncludeSignatureTextX() {
    assertEquals(PDF_SIGNATURE_TEXT_X, FK7210PdfSpecification.create().signatureTextX());
  }

  @Test
  void shallIncludeSignatureTextY() {
    assertEquals(PDF_SIGNATURE_TEXT_Y, FK7210PdfSpecification.create().signatureTextY());
  }

  @Test
  void shallIncludeSignatureTextFontSize() {
    assertEquals(
        PDF_SIGNATURE_TEXT_FONT_SIZE, FK7210PdfSpecification.create().signatureTextFontSize());
  }
}
