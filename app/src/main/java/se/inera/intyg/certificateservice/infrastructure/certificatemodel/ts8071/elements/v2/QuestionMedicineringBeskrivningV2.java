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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.v2;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionMedicinering.QUESTION_MEDICINERING_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionMedicinering.QUESTION_MEDICINERING_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionMedicineringBeskrivningV2 {

  public static final ElementId QUESTION_MEDICINERING_BESKRIVNING_V2_ID = new ElementId("21.2");
  public static final FieldId QUESTION_MEDICINERING_BESKRIVNING_V2_FIELD_ID = new FieldId("21.2");

  private QuestionMedicineringBeskrivningV2() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionMedicineringBeskrivningV2() {
    return ElementSpecification.builder()
        .id(QUESTION_MEDICINERING_BESKRIVNING_V2_ID)
        .configuration(
            ElementConfigurationTextArea.builder()
                .id(QUESTION_MEDICINERING_BESKRIVNING_V2_FIELD_ID)
                .name("Ange vilken eller vilka mediciner")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.show(
                    QUESTION_MEDICINERING_ID, QUESTION_MEDICINERING_FIELD_ID),
                CertificateElementRuleFactory.mandatory(
                    QUESTION_MEDICINERING_BESKRIVNING_V2_ID,
                    QUESTION_MEDICINERING_BESKRIVNING_V2_FIELD_ID),
                CertificateElementRuleFactory.limit(
                    QUESTION_MEDICINERING_BESKRIVNING_V2_ID, (short) 250)))
        .shouldValidate(ElementDataPredicateFactory.valueBoolean(QUESTION_MEDICINERING_ID))
        .mapping(new ElementMapping(QUESTION_MEDICINERING_ID, null))
        .validations(List.of(ElementValidationText.builder().mandatory(true).limit(250).build()))
        .build();
  }
}
