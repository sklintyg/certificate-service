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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804.FK7804PdfSpecification.PDF_FK_7804_PDF;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804.FK7804PdfSpecification.PDF_NO_ADDRESS_FK_7804_PDF;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndex;

class FK7804PdfSpecificationTest {

  @Test
  void shallCreatePdfSpecification() {
    final var pdfSpecification = FK7804PdfSpecification.create();
    assertNotNull(pdfSpecification);
  }

  @Test
  void shallIncludePdfTemplatePathWithAddress() {
    final var pdfSpecification = FK7804PdfSpecification.create();
    assertEquals(PDF_FK_7804_PDF, pdfSpecification.pdfTemplatePath());
  }

  @Test
  void shallIncludePdfTemplatePathNoAddress() {
    final var pdfSpecification = FK7804PdfSpecification.create();
    assertEquals(PDF_NO_ADDRESS_FK_7804_PDF, pdfSpecification.pdfNoAddressTemplatePath());
  }

  @Test
  void shallIncludePatientFieldIds() {
    final var expected =
        List.of(
            new PdfFieldId("form1[0].#subform[0].flt_txtPersonNr[0]"),
            new PdfFieldId("form1[0].Sida2[0].flt_txtPersonNr[0]"),
            new PdfFieldId("form1[0].Sida3[0].flt_txtPersonNr[0]"),
            new PdfFieldId("form1[0].Sida4[0].flt_txtPersonNr[0]"),
            new PdfFieldId("form1[0].#subform[4].flt_txtPersonNr[1]"));
    final var pdfSpecification = FK7804PdfSpecification.create();
    assertEquals(expected, pdfSpecification.patientIdFieldIds());
  }

  @Test
  void shallIncludeSignature() {
    final var pdfSpecification = FK7804PdfSpecification.create();
    final var signature = pdfSpecification.signature();
    assertAll(
        () -> assertNotNull(signature),
        () -> assertEquals(new PdfTagIndex(10), signature.signatureWithAddressTagIndex()),
        () -> assertEquals(new PdfTagIndex(10), signature.signatureWithoutAddressTagIndex()),
        () -> assertEquals(3, signature.signaturePageIndex()),
        () ->
            assertEquals(
                new PdfFieldId("form1[0].Sida4[0].flt_datUnderskrift[0]"),
                signature.signedDateFieldId()),
        () ->
            assertEquals(
                new PdfFieldId("form1[0].Sida4[0].flt_txtNamnfortydligande[0]"),
                signature.signedByNameFieldId()),
        () ->
            assertEquals(
                new PdfFieldId("form1[0].Sida4[0].flt_txtBefattning[0]"),
                signature.paTitleFieldId()),
        () ->
            assertEquals(
                new PdfFieldId("form1[0].Sida4[0].flt_txtEventuellSpecialistkompetens[0]"),
                signature.specialtyFieldId()),
        () ->
            assertEquals(
                new PdfFieldId("form1[0].Sida4[0].flt_txtLakarensHSA-ID[0]"),
                signature.hsaIdFieldId()),
        () ->
            assertEquals(
                new PdfFieldId("form1[0].Sida4[0].flt_txtArbetsplatskod[0]"),
                signature.workplaceCodeFieldId()),
        () ->
            assertEquals(
                new PdfFieldId("form1[0].Sida4[0].flt_txtVardgivarensNamnAdressTelefon[0]"),
                signature.contactInformation()));
  }

  @Test
  void shallIncludePdfMcid() {
    final var pdfSpecification = FK7804PdfSpecification.create();
    assertEquals(150, pdfSpecification.pdfMcid().value());
  }
}
