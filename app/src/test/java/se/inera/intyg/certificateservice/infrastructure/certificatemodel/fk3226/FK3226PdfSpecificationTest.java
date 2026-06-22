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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;

class FK3226PdfSpecificationTest {

  @Test
  void shallReturnCustomPdfSpecification() {
    assertInstanceOf(CustomPdfSpecification.class, FK3226PdfSpecification.create());
  }

  @Test
  void shallIncludePdfTemplatePathProvider() {
    assertNotNull(FK3226PdfSpecification.create().pdfTemplatePathProvider());
  }

  @Test
  void shallIncludeOverlayTextProvider() {
    assertNotNull(FK3226PdfSpecification.create().overlayTextProvider());
    assertInstanceOf(
        OverlayTextProvider.class, FK3226PdfSpecification.create().overlayTextProvider());
  }

  @Test
  void shallIncludePatientFieldIds() {
    final var expected =
        List.of(
            new PdfFieldId("form1[0].#subform[0].flt_txtPnr[0]"),
            new PdfFieldId("form1[0].#subform[1].flt_txtPnr[1]"));

    assertEquals(expected, FK3226PdfSpecification.create().patientIdFieldIds());
  }

  @Test
  void shallIncludeSignatureFields() {
    final var expected =
        CustomPdfSignature.builder()
            .signaturePageIndex(1)
            .pdfTagIndexProvider(new FK3226PdfTagProvider())
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

    assertEquals(expected, FK3226PdfSpecification.create().signature());
  }

  @Test
  void shallNotSetOverflowOnV1() {
    final var spec = FK3226PdfSpecification.create();
    assertNull(spec.overflowFieldId());
    assertNull(spec.overFlowPageIndex());
  }
}
