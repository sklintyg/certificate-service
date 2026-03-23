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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3221.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3221.FK3221PdfSpecification.PDF_TEXT_FIELD_LENGTH;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionAktivitetsbegransningar {

  public static final ElementId QUESTION_AKTIVITETSBEGRANSNINGAR_ID = new ElementId("17");
  private static final FieldId QUESTION_AKTIVITETSBEGRANSNINGAR_FIELD_ID = new FieldId("17.1");
  private static final PdfFieldId PDF_FIELD_ID =
      new PdfFieldId("form1[0].#subform[2].flt_txtKonkretEx[0]");

  private QuestionAktivitetsbegransningar() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionAktivitetsbegransningar() {
    return ElementSpecification.builder()
        .id(QUESTION_AKTIVITETSBEGRANSNINGAR_ID)
        .configuration(
            ElementConfigurationTextArea.builder()
                .id(QUESTION_AKTIVITETSBEGRANSNINGAR_FIELD_ID)
                .name("Beskriv vad barnet har svårt att göra på grund av sin funktionsnedsättning")
                .label(
                    "Ge konkreta exempel på aktiviteter i barnets vardag där svårigheter uppstår.")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.limit(
                    QUESTION_AKTIVITETSBEGRANSNINGAR_ID, (short) 4000),
                CertificateElementRuleFactory.mandatory(
                    QUESTION_AKTIVITETSBEGRANSNINGAR_ID,
                    QUESTION_AKTIVITETSBEGRANSNINGAR_FIELD_ID)))
        .validations(List.of(ElementValidationText.builder().mandatory(true).limit(4000).build()))
        .pdfConfiguration(
            PdfConfigurationText.builder()
                .pdfFieldId(PDF_FIELD_ID)
                .maxLength(PDF_TEXT_FIELD_LENGTH * 4)
                .overflowSheetFieldId(
                    new PdfFieldId(("form1[0].#subform[4].flt_txtFortsattningsblad[0]")))
                .build())
        .build();
  }
}
