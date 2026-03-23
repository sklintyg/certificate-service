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
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionAtgarderSomKanFramjaAtergang {

  public static final ElementId QUESTION_ATGARDER_ID = new ElementId("44");
  private static final FieldId QUESTION_ATGARDER_FIELD_ID = new FieldId("44.1");

  private QuestionAtgarderSomKanFramjaAtergang() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionAtgarderSomKanFramjaAtergang() {
    return ElementSpecification.builder()
        .id(QUESTION_ATGARDER_ID)
        .configuration(
            ElementConfigurationTextArea.builder()
                .id(QUESTION_ATGARDER_FIELD_ID)
                .name(
                    "Här kan du beskriva andra åtgärder än åtgärder inom hälso- och sjukvården som kan främja återgången i arbete")
                .label(
                    "Beskriv gärna hur åtgärderna kan främja återgången i arbete eller annan aktuell sysselsättning.")
                .description(
                    """
                    Åtgärderna kan exempelvis handla om att patienten har en regelbunden kontakt med arbetsplatsen. Det kan också vara arbetsanpassning, som anpassning av arbetstider, arbetsuppgifter eller arbetsplatsen.

                    Du kan även föreslå att patienten får arbetsträna vilket innebär att vara på en arbetsplats och delta
                    i verksamheten utan krav på produktivitet.\s

                    Tänk på att det är Försäkringskassan eller Arbetsförmedlingen som beslutar om arbetsträning. De föreslagna åtgärderna är exempel på möjliga åtgärder och det kan finnas flera åtgärder som är lämpliga.
                    """)
                .build())
        .pdfConfiguration(
            PdfConfigurationText.builder()
                .pdfFieldId(
                    new PdfFieldId(
                        "form1[0].Sida3[0].flt_txtArbetslivsinriktadAtgarderUnderlatta[0]"))
                .overflowSheetFieldId(
                    new PdfFieldId("form1[0].#subform[4].flt_txtFortsattningsblad[0]"))
                .maxLength(7 * PDF_TEXT_FIELD_ROW_LENGTH)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.limit(QUESTION_ATGARDER_ID, (short) 4000),
                CertificateElementRuleFactory.hide(
                    QUESTION_SMITTBARARPENNING_ID, QUESTION_SMITTBARARPENNING_FIELD_ID)))
        .validations(List.of(ElementValidationText.builder().mandatory(false).limit(4000).build()))
        .shouldValidate(
            ElementDataPredicateFactory.checkboxBoolean(QUESTION_SMITTBARARPENNING_ID, false))
        .build();
  }
}
