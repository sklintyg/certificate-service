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

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayDetails;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;

public class FK3226PdfSpecification {

  private static final int PDF_SIGNATURE_PAGE_INDEX = 1;
  private static final float PDF_SIGNATURE_TEXT_X = 173f;
  private static final float PDF_SIGNATURE_TEXT_Y = 300f;

  private static final PdfFieldId PDF_PATIENT_ID_FIELD_ID_1 =
      new PdfFieldId("form1[0].#subform[0].flt_txtPnr[0]");
  private static final PdfFieldId PDF_PATIENT_ID_FIELD_ID_2 =
      new PdfFieldId("form1[0].#subform[1].flt_txtPnr[1]");
  private static final PdfFieldId PDF_SIGNED_DATE_FIELD_ID =
      new PdfFieldId("form1[0].#subform[1].flt_datUnderskrift[0]");
  private static final PdfFieldId PDF_SIGNED_BY_NAME_FIELD_ID =
      new PdfFieldId("form1[0].#subform[1].flt_txtNamnfortydligande[0]");
  private static final PdfFieldId PDF_SIGNED_BY_PA_TITLE =
      new PdfFieldId("form1[0].#subform[1].flt_txtBefattning[0]");
  private static final PdfFieldId PDF_SIGNED_BY_SPECIALTY =
      new PdfFieldId("form1[0].#subform[1].flt_txtEventuellSpecialistkompetens[0]");
  private static final PdfFieldId PDF_HSA_ID_FIELD_ID =
      new PdfFieldId("form1[0].#subform[1].flt_txtLakarensHSA-ID[0]");
  private static final PdfFieldId PDF_WORKPLACE_CODE_FIELD_ID =
      new PdfFieldId("form1[0].#subform[1].flt_txtArbetsplatskod[0]");
  private static final PdfFieldId PDF_CONTACT_INFORMATION =
      new PdfFieldId("form1[0].#subform[1].flt_txtVardgivarensNamnAdressTelefon[0]");

  private FK3226PdfSpecification() {
    throw new IllegalStateException("Utility class");
  }

  public static CustomPdfSpecification create() {
    return CustomPdfSpecification.builder()
        .pdfTemplatePathProvider(new FK3226TemplatePathProvider())
        .patientIdFieldIds(List.of(PDF_PATIENT_ID_FIELD_ID_1, PDF_PATIENT_ID_FIELD_ID_2))
        .signature(
            CustomPdfSignature.builder()
                .signaturePageIndex(PDF_SIGNATURE_PAGE_INDEX)
                .signedDateFieldId(PDF_SIGNED_DATE_FIELD_ID)
                .signedByNameFieldId(PDF_SIGNED_BY_NAME_FIELD_ID)
                .paTitleFieldId(PDF_SIGNED_BY_PA_TITLE)
                .specialtyFieldId(PDF_SIGNED_BY_SPECIALTY)
                .hsaIdFieldId(PDF_HSA_ID_FIELD_ID)
                .workplaceCodeFieldId(PDF_WORKPLACE_CODE_FIELD_ID)
                .contactInformation(PDF_CONTACT_INFORMATION)
                .build())
        .overlayTextProvider(
            new OverlayTextProvider(
                OverlayDetails.builder()
                    .signatureTextX(PDF_SIGNATURE_TEXT_X)
                    .signatureTextY(PDF_SIGNATURE_TEXT_Y)
                    .signaturePageIndex(PDF_SIGNATURE_PAGE_INDEX)
                    .signedTextWithAddressIndex(36)
                    .signedTextWithoutAddressIndex(36)
                    .sentTextIndex(6)
                    .citizenTextIndex(6)
                    .build()))
        .build();
  }
}
