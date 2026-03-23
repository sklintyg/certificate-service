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

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionMissbrukV2 {

  public static final ElementId QUESTION_MISSBRUK_V2_ID = new ElementId("18");
  public static final FieldId QUESTION_MISSBRUK_V2_FIELD_ID = new FieldId("18.1");

  private QuestionMissbrukV2() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionMissbrukV2(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(QUESTION_MISSBRUK_V2_ID)
        .configuration(
            ElementConfigurationRadioBoolean.builder()
                .id(QUESTION_MISSBRUK_V2_FIELD_ID)
                .name(
                    "Har personen eller har personen haft en diagnos avseende alkohol, andra psykoaktiva substanser eller läkemedel?")
                .selectedText("Ja")
                .unselectedText("Nej")
                .build())
        .validations(List.of(ElementValidationBoolean.builder().mandatory(true).build()))
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryExist(
                    QUESTION_MISSBRUK_V2_ID, QUESTION_MISSBRUK_V2_FIELD_ID)))
        .children(List.of(children))
        .build();
  }
}
