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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionEpilepsi.QUESTION_EPILEPSI_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionEpilepsiMedicin.QUESTION_EPILEPSI_MEDICIN_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionEpilepsiMedicin.QUESTION_EPILEPSI_MEDICIN_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionEpilepsiMedicinBeskrivning {

  public static final ElementId QUESTION_EPILEPSI_MEDICIN_BESKRIVNING_ID = new ElementId("14.6");
  public static final FieldId QUESTION_EPILEPSI_MEDICIN_BESKRIVNING_FIELD_ID = new FieldId("14.6");

  private QuestionEpilepsiMedicinBeskrivning() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionEpilepsiMedicinBeskrivning() {
    return ElementSpecification.builder()
        .id(QUESTION_EPILEPSI_MEDICIN_BESKRIVNING_ID)
        .configuration(
            ElementConfigurationTextArea.builder()
                .id(QUESTION_EPILEPSI_MEDICIN_BESKRIVNING_FIELD_ID)
                .name("Ange vilket läkemedel")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.show(
                    QUESTION_EPILEPSI_MEDICIN_ID, QUESTION_EPILEPSI_MEDICIN_FIELD_ID),
                CertificateElementRuleFactory.mandatory(
                    QUESTION_EPILEPSI_MEDICIN_BESKRIVNING_ID,
                    QUESTION_EPILEPSI_MEDICIN_BESKRIVNING_FIELD_ID),
                CertificateElementRuleFactory.limit(
                    QUESTION_EPILEPSI_MEDICIN_BESKRIVNING_ID, (short) 250)))
        .shouldValidate(ElementDataPredicateFactory.valueBoolean(QUESTION_EPILEPSI_MEDICIN_ID))
        .mapping(new ElementMapping(QUESTION_EPILEPSI_ID, null))
        .validations(List.of(ElementValidationText.builder().mandatory(true).limit(250).build()))
        .build();
  }
}
