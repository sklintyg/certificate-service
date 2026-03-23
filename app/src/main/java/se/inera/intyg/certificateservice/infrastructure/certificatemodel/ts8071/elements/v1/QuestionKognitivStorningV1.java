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

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionKognitivStorningV1 {

  public static final ElementId QUESTION_KOGNITIV_STORNING_V1_ID = new ElementId("16");
  public static final FieldId QUESTION_KOGNITIV_STORNING_V1_FIELD_ID = new FieldId("16.1");

  private QuestionKognitivStorningV1() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionKognitivStorningV1(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(QUESTION_KOGNITIV_STORNING_V1_ID)
        .configuration(
            ElementConfigurationRadioBoolean.builder()
                .id(QUESTION_KOGNITIV_STORNING_V1_FIELD_ID)
                .selectedText("Ja")
                .unselectedText("Nej")
                .name("Har personen diagnos allvarlig kognitiv störning?")
                .build())
        .validations(List.of(ElementValidationBoolean.builder().mandatory(true).build()))
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryExist(
                    QUESTION_KOGNITIV_STORNING_V1_ID, QUESTION_KOGNITIV_STORNING_V1_FIELD_ID)))
        .children(List.of(children))
        .build();
  }
}
