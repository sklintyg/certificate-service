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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.v1;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionEpilepsi.QUESTION_EPILEPSI_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionEpilepsiMedicin.QUESTION_EPILEPSI_MEDICIN_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionEpilepsiMedicin.QUESTION_EPILEPSI_MEDICIN_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextField;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionEpilepsiMedicinTidpunktV1 {

  public static final ElementId QUESTION_EPILEPSI_MEDICIN_TIDPUNKT_V1_ID = new ElementId("14.7");
  public static final FieldId QUESTION_EPILEPSI_MEDICIN_TIDPUNKT_V1_FIELD_ID = new FieldId("14.7");

  private QuestionEpilepsiMedicinTidpunktV1() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionEpilepsiMedicinTidpunktV1() {
    return ElementSpecification.builder()
        .id(QUESTION_EPILEPSI_MEDICIN_TIDPUNKT_V1_ID)
        .configuration(
            ElementConfigurationTextField.builder()
                .id(QUESTION_EPILEPSI_MEDICIN_TIDPUNKT_V1_FIELD_ID)
                .name("Om läkemedelsbehandling avslutats, ange tidpunkt")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.show(
                    QUESTION_EPILEPSI_MEDICIN_ID, QUESTION_EPILEPSI_MEDICIN_FIELD_ID),
                CertificateElementRuleFactory.limit(
                    QUESTION_EPILEPSI_MEDICIN_TIDPUNKT_V1_ID, (short) 50)))
        .shouldValidate(ElementDataPredicateFactory.valueBoolean(QUESTION_EPILEPSI_MEDICIN_ID))
        .mapping(new ElementMapping(QUESTION_EPILEPSI_ID, null))
        .validations(List.of(ElementValidationText.builder().limit(50).build()))
        .build();
  }
}
