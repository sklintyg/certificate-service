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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionSvarareAtergangVidOjamnArbetstid.QUESTION_SVARARE_ATERGANG_VID_OJAMN_ARBETSTID_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionSvarareAtergangVidOjamnArbetstid.QUESTION_SVARARE_ATERGANG_VID_OJAMN_ARBETSTID_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionMedicinskaSkalForSvarareAtergang {

  public static final ElementId QUESTION_MEDICINSKA_SKAL_ID = new ElementId("33.2");
  private static final FieldId QUESTION_MEDICINSKA_SKAL_FIELD_ID = new FieldId("33.2");

  private QuestionMedicinskaSkalForSvarareAtergang() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionMedicinskaSkalForSvarareAtergang() {
    return ElementSpecification.builder()
        .id(QUESTION_MEDICINSKA_SKAL_ID)
        .configuration(
            ElementConfigurationTextArea.builder()
                .name(
                    "Beskriv de medicinska skälen till att möjligheterna till återgång i arbete försämras")
                .id(QUESTION_MEDICINSKA_SKAL_FIELD_ID)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatory(
                    QUESTION_MEDICINSKA_SKAL_ID, QUESTION_MEDICINSKA_SKAL_FIELD_ID),
                CertificateElementRuleFactory.limit(QUESTION_MEDICINSKA_SKAL_ID, (short) 4000),
                CertificateElementRuleFactory.show(
                    QUESTION_SVARARE_ATERGANG_VID_OJAMN_ARBETSTID_ID,
                    QUESTION_SVARARE_ATERGANG_VID_OJAMN_ARBETSTID_FIELD_ID)))
        .validations(List.of(ElementValidationText.builder().mandatory(true).limit(4000).build()))
        .shouldValidate(
            ElementDataPredicateFactory.valueBoolean(
                QUESTION_SVARARE_ATERGANG_VID_OJAMN_ARBETSTID_ID, true))
        .mapping(new ElementMapping(QUESTION_SVARARE_ATERGANG_VID_OJAMN_ARBETSTID_ID, null))
        .build();
  }
}
