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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionGrundForMedicinsktUnderlag.QUESTION_GRUND_FOR_MEDICINSKT_UNDERLAG_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0001.ANNAT;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextField;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0001;

public class QuestionAngeVadAnnatAr {

  public static final ElementId QUESTION_ANGE_VAD_ANNAT_AR_ID = new ElementId("10.3");
  public static final FieldId QUESTION_ANGE_VAD_ANNAT_AR_FIELD_ID = new FieldId("10.3");
  private static final FieldId ANNAT_FIELD_ID = new FieldId(ANNAT.code());

  private QuestionAngeVadAnnatAr() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionAngeVadAnnatAr() {
    return ElementSpecification.builder()
        .id(QUESTION_ANGE_VAD_ANNAT_AR_ID)
        .configuration(
            ElementConfigurationTextField.builder()
                .id(QUESTION_ANGE_VAD_ANNAT_AR_FIELD_ID)
                .name("Ange vad annat är")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.show(
                    QUESTION_GRUND_FOR_MEDICINSKT_UNDERLAG_ID, ANNAT_FIELD_ID),
                CertificateElementRuleFactory.mandatory(
                    QUESTION_ANGE_VAD_ANNAT_AR_ID, QUESTION_ANGE_VAD_ANNAT_AR_FIELD_ID),
                CertificateElementRuleFactory.limit(QUESTION_ANGE_VAD_ANNAT_AR_ID, (short) 50)))
        .validations(List.of(ElementValidationText.builder().mandatory(true).limit(50).build()))
        .mapping(
            new ElementMapping(
                QUESTION_GRUND_FOR_MEDICINSKT_UNDERLAG_ID, CodeSystemKvFkmu0001.ANNAT))
        .shouldValidate(
            elementData ->
                elementData.stream()
                    .filter(data -> data.id().equals(QUESTION_GRUND_FOR_MEDICINSKT_UNDERLAG_ID))
                    .map(element -> (ElementValueDateList) element.value())
                    .anyMatch(
                        value ->
                            value.dateList().stream()
                                .anyMatch(valueDate -> valueDate.dateId().equals(ANNAT_FIELD_ID))))
        .build();
  }
}
