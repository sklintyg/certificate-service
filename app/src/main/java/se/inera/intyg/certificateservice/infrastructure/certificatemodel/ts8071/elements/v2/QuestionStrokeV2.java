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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionHjartsjukdom.QUESTION_HJARTSJUKDOM_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionHjartsjukdom.QUESTION_HJARTSJUKDOM_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionStrokeV2 {

  public static final ElementId QUESTION_STROKE_V2_ID = new ElementId("11.9");
  public static final FieldId QUESTION_STROKE_V2_FIELD_ID = new FieldId("11.9");

  private QuestionStrokeV2() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionStrokeV2(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(QUESTION_STROKE_V2_ID)
        .configuration(
            ElementConfigurationRadioBoolean.builder()
                .id(QUESTION_STROKE_V2_FIELD_ID)
                .selectedText("Ja")
                .unselectedText("Nej")
                .name(
                    "Har personen haft en stroke eller finns tecken på hjärnskada efter trauma, stroke eller annan sjukdom i centrala nervsystemet?")
                .build())
        .validations(List.of(ElementValidationBoolean.builder().mandatory(true).build()))
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryExist(
                    QUESTION_STROKE_V2_ID, QUESTION_STROKE_V2_FIELD_ID),
                CertificateElementRuleFactory.show(
                    QUESTION_HJARTSJUKDOM_ID, QUESTION_HJARTSJUKDOM_FIELD_ID)))
        .mapping(new ElementMapping(QUESTION_HJARTSJUKDOM_ID, null))
        .shouldValidate(ElementDataPredicateFactory.valueBoolean(QUESTION_HJARTSJUKDOM_ID))
        .children(List.of(children))
        .build();
  }
}
