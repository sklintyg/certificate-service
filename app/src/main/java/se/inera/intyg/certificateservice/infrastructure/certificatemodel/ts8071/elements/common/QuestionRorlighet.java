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

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionRorlighet {

  public static final ElementId QUESTION_RORLIGHET_ID = new ElementId("10");
  public static final FieldId QUESTION_RORLIGHET_FIELD_ID = new FieldId("10.1");

  private QuestionRorlighet() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionRorlighet(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(QUESTION_RORLIGHET_ID)
        .configuration(
            ElementConfigurationRadioBoolean.builder()
                .id(QUESTION_RORLIGHET_FIELD_ID)
                .description(
                    "Här avses till exempel nedsättningar av rörelseförmågan till följd av trauma, amputation, ryggmärgsskada, stroke eller annan sjukdom.")
                .selectedText("Ja")
                .unselectedText("Nej")
                .name(
                    "Har personen någon sjukdom eller funktionsnedsättning som påverkar rörligheten och som medför att fordon inte kan köras på ett trafiksäkert sätt?")
                .build())
        .validations(List.of(ElementValidationBoolean.builder().mandatory(true).build()))
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryExist(
                    QUESTION_RORLIGHET_ID, QUESTION_RORLIGHET_FIELD_ID)))
        .children(List.of(children))
        .build();
  }
}
