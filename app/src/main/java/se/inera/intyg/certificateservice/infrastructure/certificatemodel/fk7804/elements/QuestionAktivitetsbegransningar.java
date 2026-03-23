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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804.FK7804PdfSpecification.PDF_TEXT_FIELD_ROW_LENGTH;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804.elements.QuestionSmittbararpenning.QUESTION_SMITTBARARPENNING_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804.elements.QuestionSmittbararpenning.QUESTION_SMITTBARARPENNING_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationIcf;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.IcfCodesPropertyType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationIcfValue;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionAktivitetsbegransningar {

  public static final ElementId QUESTION_AKTIVITETSBEGRANSNING_ID = new ElementId("17");
  public static final FieldId QUESTION_AKTIVITETSBEGRANSNING_FIELD_ID = new FieldId("17.1");

  private QuestionAktivitetsbegransningar() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionAktivitetsbegransningar() {
    return ElementSpecification.builder()
        .id(QUESTION_AKTIVITETSBEGRANSNING_ID)
        .configuration(
            ElementConfigurationIcf.builder()
                .id(QUESTION_AKTIVITETSBEGRANSNING_FIELD_ID)
                .icfCodesPropertyName(IcfCodesPropertyType.AKTIVITETSBEGRANSNINGAR)
                .name(
                    "Beskriv vad du bedömer att patienten har svårt att göra på grund av sin sjukdom. Ange exempel på sådana begränsningar relaterade till de arbetsuppgifter eller annan sysselsättning som du bedömer arbetsförmågan i förhållande till. Ange om möjligt svårighetsgrad.")
                .modalLabel("Välj enbart de svårigheter som påverkar patientens sysselsättning.")
                .collectionsLabel("Svårigheter som påverkar patientens sysselsättning:")
                .placeholder(
                    "Hur begränsar ovanstående patientens sysselsättning och i vilken utsträckning?")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatory(
                    QUESTION_AKTIVITETSBEGRANSNING_ID, QUESTION_AKTIVITETSBEGRANSNING_FIELD_ID),
                CertificateElementRuleFactory.hide(
                    QUESTION_SMITTBARARPENNING_ID, QUESTION_SMITTBARARPENNING_FIELD_ID)))
        .validations(
            List.of(ElementValidationIcfValue.builder().mandatory(true).limit(4000).build()))
        .shouldValidate(
            ElementDataPredicateFactory.checkboxBoolean(QUESTION_SMITTBARARPENNING_ID, false))
        .pdfConfiguration(
            PdfConfigurationText.builder()
                .pdfFieldId(
                    new PdfFieldId("form1[0].Sida2[0].flt_txtBeskrivAktivitetsbegransning[0]"))
                .overflowSheetFieldId(
                    new PdfFieldId("form1[0].#subform[4].flt_txtFortsattningsblad[0]"))
                .maxLength(12 * PDF_TEXT_FIELD_ROW_LENGTH)
                .build())
        .build();
  }
}
