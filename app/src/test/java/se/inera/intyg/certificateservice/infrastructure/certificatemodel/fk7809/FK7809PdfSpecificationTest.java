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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7809;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverflowPageIndex;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.SignatureOverlayDetails;

class FK7809PdfSpecificationTest {

  private static final float EXPECTED_SIGNATURE_OVERLAY_X = 173f;
  private static final float EXPECTED_SIGNATURE_OVERLAY_Y = 528f;
  private static final int EXPECTED_SIGNATURE_PAGE_INDEX = 3;

  @Test
  void shallReturnCustomPdfSpecification() {
    assertInstanceOf(CustomPdfSpecification.class, FK7809PdfSpecification.create());
  }

  @Test
  void shallIncludePdfTemplatePathProvider() {
    assertNotNull(FK7809PdfSpecification.create().pdfTemplatePathProvider());
    assertInstanceOf(
        FK7809TemplatePathProvider.class,
        FK7809PdfSpecification.create().pdfTemplatePathProvider());
  }

  @Test
  void shallIncludeOverlayTextProvider() {
    assertNotNull(FK7809PdfSpecification.create().overlayTextProvider());
    assertInstanceOf(
        OverlayTextProvider.class, FK7809PdfSpecification.create().overlayTextProvider());
  }

  @Test
  void shallIncludeOverlaySignatureDetails() {
    final var details = FK7809PdfSpecification.create().overlayTextProvider().signatureDetails();

    assertEquals(
        SignatureOverlayDetails.builder()
            .signatureTextX(EXPECTED_SIGNATURE_OVERLAY_X)
            .signatureTextY(EXPECTED_SIGNATURE_OVERLAY_Y)
            .signaturePageIndex(EXPECTED_SIGNATURE_PAGE_INDEX)
            .build(),
        details);
  }

  @Test
  void shallIncludePatientFieldIds() {
    final var expected =
        List.of(
            new PdfFieldId("form1[0].#subform[0].flt_txtPersonNr[0]"),
            new PdfFieldId("form1[0].#subform[1].flt_txtPersonNr[1]"),
            new PdfFieldId("form1[0].#subform[2].flt_txtPersonNr[2]"),
            new PdfFieldId("form1[0].#subform[3].flt_txtPersonNr[3]"),
            new PdfFieldId("form1[0].#subform[4].flt_txtPersonNr[4]"));

    assertEquals(expected, FK7809PdfSpecification.create().patientIdFieldIds());
  }

  @Test
  void shallIncludeSignatureFields() {
    final var expected =
        CustomPdfSignature.builder()
            .signaturePageIndex(3)
            .pdfTagIndexProvider(new FK7809PdfTagProvider())
            .signedDateFieldId(new PdfFieldId("form1[0].#subform[3].flt_datUnderskrift[0]"))
            .signedByNameFieldId(new PdfFieldId("form1[0].#subform[3].flt_txtNamnfortydligande[0]"))
            .paTitleFieldId(new PdfFieldId("form1[0].#subform[3].flt_txtBefattning[0]"))
            .specialtyFieldId(
                new PdfFieldId("form1[0].#subform[3].flt_txtEventuellSpecialistkompetens[0]"))
            .hsaIdFieldId(new PdfFieldId("form1[0].#subform[3].flt_txtLakarensHSA-ID[0]"))
            .workplaceCodeFieldId(new PdfFieldId("form1[0].#subform[3].flt_txtArbetsplatskod[0]"))
            .contactInformation(
                new PdfFieldId("form1[0].#subform[3].flt_txtVardgivarensNamnAdressTelefon[0]"))
            .build();

    assertEquals(expected, FK7809PdfSpecification.create().signature());
  }

  @Test
  void shallIncludeOverflowConfiguration() {
    final var spec = FK7809PdfSpecification.create();

    assertEquals(
        new PdfFieldId("form1[0].#subform[4].flt_txtFortsattningsblad[0]"), spec.overflowFieldId());
    assertEquals(new OverflowPageIndex(4), spec.overFlowPageIndex());
  }
}
