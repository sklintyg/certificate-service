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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.FK3226PdfSpecification.PDF_FK_3226_PDF;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.FK3226PdfSpecification.PDF_NO_ADDRESS_FK_3226_PDF;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndex;

class FK3226PdfSpecificationTest {

  @Test
  void shallIncludePdfTemplatePathWithAddress() {
    final var certificateModel = FK3226PdfSpecification.create();

    assertEquals(PDF_FK_3226_PDF, certificateModel.pdfTemplatePath());
  }

  @Test
  void shallIncludePdfTemplatePathNoAddress() {
    final var certificateModel = FK3226PdfSpecification.create();

    assertEquals(PDF_NO_ADDRESS_FK_3226_PDF, certificateModel.pdfNoAddressTemplatePath());
  }

  @Test
  void shallIncludePatientFieldId() {
    final var expected =
        List.of(
            new PdfFieldId("form1[0].#subform[0].flt_txtPnr[0]"),
            new PdfFieldId("form1[0].#subform[1].flt_txtPnr[1]"));

    final var certificateModel = FK3226PdfSpecification.create();

    assertEquals(expected, certificateModel.patientIdFieldIds());
  }

  @Test
  void shallIncludeSignatureFields() {
    final var expected =
        PdfSignature.builder()
            .signaturePageIndex(1)
            .signatureWithAddressTagIndex(new PdfTagIndex(36))
            .signatureWithoutAddressTagIndex(new PdfTagIndex(36))
            .signedDateFieldId(new PdfFieldId("form1[0].#subform[1].flt_datUnderskrift[0]"))
            .signedByNameFieldId(new PdfFieldId("form1[0].#subform[1].flt_txtNamnfortydligande[0]"))
            .paTitleFieldId(new PdfFieldId("form1[0].#subform[1].flt_txtBefattning[0]"))
            .specialtyFieldId(
                new PdfFieldId("form1[0].#subform[1].flt_txtEventuellSpecialistkompetens[0]"))
            .hsaIdFieldId(new PdfFieldId("form1[0].#subform[1].flt_txtLakarensHSA-ID[0]"))
            .workplaceCodeFieldId(new PdfFieldId("form1[0].#subform[1].flt_txtArbetsplatskod[0]"))
            .contactInformation(
                new PdfFieldId("form1[0].#subform[1].flt_txtVardgivarensNamnAdressTelefon[0]"))
            .build();

    final var certificateModel = FK3226PdfSpecification.create();

    assertEquals(expected, certificateModel.signature());
  }

  @Test
  void shallIncludeMcid() {
    final var expected = 100;
    final var certificateModel = FK3226PdfSpecification.create();

    assertEquals(expected, certificateModel.pdfMcid().value());
  }
}
