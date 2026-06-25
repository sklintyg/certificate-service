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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverflowPageIndex;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayDetails;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;

class FK7810PdfSpecificationTest {

  private static final float EXPECTED_SIGNATURE_OVERLAY_X = 173f;
  private static final float EXPECTED_SIGNATURE_OVERLAY_Y = 274f;
  private static final int EXPECTED_SIGNATURE_PAGE_INDEX = 4;

  @Test
  void shallReturnCustomPdfSpecification() {
    assertInstanceOf(CustomPdfSpecification.class, FK7810PdfSpecification.create());
  }

  @Test
  void shallIncludePdfTemplatePathProvider() {
    assertNotNull(FK7810PdfSpecification.create().pdfTemplatePathProvider());
    assertInstanceOf(
        FK7810TemplatePathProvider.class,
        FK7810PdfSpecification.create().pdfTemplatePathProvider());
  }

  @Test
  void shallIncludeOverlayTextProvider() {
    assertNotNull(FK7810PdfSpecification.create().overlayTextProvider());
    assertInstanceOf(
        OverlayTextProvider.class, FK7810PdfSpecification.create().overlayTextProvider());
  }

  @Test
  void shallIncludeOverlaySignatureDetails() {
    final var details = FK7810PdfSpecification.create().overlayTextProvider().overlayDetails();

    assertEquals(
        OverlayDetails.builder()
            .signatureTextX(EXPECTED_SIGNATURE_OVERLAY_X)
            .signatureTextY(EXPECTED_SIGNATURE_OVERLAY_Y)
            .signaturePageIndex(EXPECTED_SIGNATURE_PAGE_INDEX)
            .signedTextWithAddressIndex(24)
            .signedTextWithoutAddressIndex(24)
            .sentTextIndex(3)
            .citizenTextIndex(3)
            .build(),
        details);
  }

  @Test
  void shallIncludePatientFieldIds() {
    final var expected =
        List.of(
            new PdfFieldId("form1[0].#subform[0].flt_txtPersonNr[0]"),
            new PdfFieldId("form1[0].Sida2[0].flt_txtPersonNr[0]"),
            new PdfFieldId("form1[0].Sida3[0].flt_txtPersonNr[0]"),
            new PdfFieldId("form1[0].Sida4[0].flt_txtPersonNr[0]"),
            new PdfFieldId("form1[0].#subform[5].flt_txtPersonNr[1]"),
            new PdfFieldId("form1[0].#subform[6].flt_txtPersonNr[2]"));

    assertEquals(expected, FK7810PdfSpecification.create().patientIdFieldIds());
  }

  @Test
  void shallIncludeSignatureFields() {
    final var expected =
        CustomPdfSignature.builder()
            .signaturePageIndex(4)
            .signedDateFieldId(new PdfFieldId("form1[0].#subform[5].flt_datUnderskrift[0]"))
            .signedByNameFieldId(new PdfFieldId("form1[0].#subform[5].flt_txtNamnfortydligande[0]"))
            .paTitleFieldId(new PdfFieldId("form1[0].#subform[5].flt_txtBefattning[0]"))
            .specialtyFieldId(
                new PdfFieldId("form1[0].#subform[5].flt_txtEventuellSpecialistkompetens[0]"))
            .hsaIdFieldId(new PdfFieldId("form1[0].#subform[5].flt_txtHSAid[0]"))
            .workplaceCodeFieldId(new PdfFieldId("form1[0].#subform[5].flt_txtArbetsplatskod[0]"))
            .contactInformation(
                new PdfFieldId("form1[0].#subform[5].flt_txtVardgivarensNamnAdressTelefon[0]"))
            .build();

    assertEquals(expected, FK7810PdfSpecification.create().signature());
  }

  @Test
  void shallIncludeOverflowConfiguration() {
    final var spec = FK7810PdfSpecification.create();

    assertEquals(
        new PdfFieldId("form1[0].#subform[6].flt_txtFortsattningsblad[0]"), spec.overflowFieldId());
    assertEquals(new OverflowPageIndex(5), spec.overFlowPageIndex());
  }
}
