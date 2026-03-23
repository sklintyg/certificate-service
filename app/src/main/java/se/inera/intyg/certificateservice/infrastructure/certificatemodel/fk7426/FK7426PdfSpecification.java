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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverflowPageIndex;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfMcid;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndex;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.TemplatePdfSpecification;

public class FK7426PdfSpecification {

  public static final int ROW_MAX_LENGTH = 52;
  public static final String PDF_FK_7426_PDF = "fk7426/pdf/fk7426_v1.pdf";
  public static final String PDF_FK_7426_PDF_NO_ADDRESS = "fk7426/pdf/fk7426_v1_no_address.pdf";
  public static final PdfMcid PDF_MCID = new PdfMcid(300);
  private static final int PDF_SIGNATURE_PAGE_INDEX = 2;
  private static final PdfTagIndex PDF_SIGNATURE_TAG_INDEX = new PdfTagIndex(31);
  private static final List<PdfFieldId> PDF_PATIENT_ID_FIELD_IDS =
      List.of(
          new PdfFieldId("form1[0].#subform[0].flt_txtBarnPersonNr[0]"),
          new PdfFieldId("form1[0].#subform[2].flt_txtBarnPersonNr[1]"),
          new PdfFieldId("form1[0].#subform[3].flt_txtBarnPersonNr[2]"),
          new PdfFieldId("form1[0].#subform[4].flt_txtBarnPersonNr[3]"));
  private static final PdfFieldId PDF_SIGNED_DATE_FIELD_ID =
      new PdfFieldId("form1[0].#subform[3].flt_datUnderskrift[0]");
  private static final PdfFieldId PDF_SIGNED_BY_NAME_FIELD_ID =
      new PdfFieldId("form1[0].#subform[3].flt_txtNamnfortydligande[0]");
  private static final PdfFieldId PDF_SIGNED_BY_PA_TITLE =
      new PdfFieldId("form1[0].#subform[3].flt_txtBefattning[0]");
  private static final PdfFieldId PDF_SIGNED_BY_SPECIALTY =
      new PdfFieldId("form1[0].#subform[3].flt_txtEventuellSpecialistkompetens[0]");
  private static final PdfFieldId PDF_HSA_ID_FIELD_ID =
      new PdfFieldId("form1[0].#subform[3].flt_txtLakarensHSA-ID[0]");
  private static final PdfFieldId PDF_WORKPLACE_CODE_FIELD_ID =
      new PdfFieldId("form1[0].#subform[3].flt_txtArbetsplatskod[0]");
  private static final PdfFieldId PDF_CONTACT_INFORMATION =
      new PdfFieldId("form1[0].#subform[3].flt_txtVardgivarensNamnAdressTelefon[0]");
  public static final PdfFieldId FORTSATTNINGSBLAD_ID =
      new PdfFieldId("form1[0].#subform[4].flt_txtFortsattningsblad[0]");

  private FK7426PdfSpecification() {
    throw new IllegalStateException("Utility class");
  }

  public static TemplatePdfSpecification create() {
    return TemplatePdfSpecification.builder()
        .pdfTemplatePath(PDF_FK_7426_PDF)
        .pdfNoAddressTemplatePath(PDF_FK_7426_PDF_NO_ADDRESS)
        .patientIdFieldIds(PDF_PATIENT_ID_FIELD_IDS)
        .pdfMcid(PDF_MCID)
        .signature(
            PdfSignature.builder()
                .signatureWithAddressTagIndex(PDF_SIGNATURE_TAG_INDEX)
                .signatureWithoutAddressTagIndex(PDF_SIGNATURE_TAG_INDEX)
                .signaturePageIndex(PDF_SIGNATURE_PAGE_INDEX)
                .signedDateFieldId(PDF_SIGNED_DATE_FIELD_ID)
                .signedByNameFieldId(PDF_SIGNED_BY_NAME_FIELD_ID)
                .paTitleFieldId(PDF_SIGNED_BY_PA_TITLE)
                .specialtyFieldId(PDF_SIGNED_BY_SPECIALTY)
                .hsaIdFieldId(PDF_HSA_ID_FIELD_ID)
                .workplaceCodeFieldId(PDF_WORKPLACE_CODE_FIELD_ID)
                .contactInformation(PDF_CONTACT_INFORMATION)
                .build())
        .hasPageNbr(false)
        .overFlowPageIndex(new OverflowPageIndex(3))
        .untaggedWatermarks(
            List.of("74260501", "FK 7426 (001 F 001) Fastställd av Försäkringskassan"))
        .build();
  }
}
