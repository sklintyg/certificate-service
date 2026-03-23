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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionHjartsjukdom.QUESTION_HJARTSJUKDOM_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.v2.QuestionStrokeV2.QUESTION_STROKE_V2_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.v2.QuestionStrokeV2.QUESTION_STROKE_V2_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementLayout;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCode;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvTs001;

public class QuestionStrokePaverkanV2 {

  public static final ElementId QUESTION_STROKE_PAVARKAN_V2_ID = new ElementId("11.10");
  public static final FieldId QUESTION_STROKE_PAVARKAN_V2_FIELD_ID = new FieldId("11.10");

  private QuestionStrokePaverkanV2() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionStrokePaverkanV2(ElementSpecification... children) {
    final var radioMultipleCodes =
        List.of(
            CodeFactory.elementConfigurationCode(CodeSystemKvTs001.JA),
            CodeFactory.elementConfigurationCode(CodeSystemKvTs001.NEJ),
            CodeFactory.elementConfigurationCode(CodeSystemKvTs001.VET_INTE));

    return ElementSpecification.builder()
        .id(QUESTION_STROKE_PAVARKAN_V2_ID)
        .configuration(
            ElementConfigurationRadioMultipleCode.builder()
                .id(QUESTION_STROKE_PAVARKAN_V2_FIELD_ID)
                .name(
                    "Om stroke förekommit, har det inträffat eller påverkat syncentrum (occipitalloben eller synnerven)?")
                .elementLayout(ElementLayout.ROWS)
                .list(radioMultipleCodes)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryOrExist(
                    QUESTION_STROKE_PAVARKAN_V2_ID,
                    radioMultipleCodes.stream().map(ElementConfigurationCode::id).toList()),
                CertificateElementRuleFactory.show(
                    QUESTION_STROKE_V2_ID, QUESTION_STROKE_V2_FIELD_ID)))
        .shouldValidate(ElementDataPredicateFactory.valueBoolean(QUESTION_STROKE_V2_ID))
        .mapping(new ElementMapping(QUESTION_HJARTSJUKDOM_ID, null))
        .validations(List.of(ElementValidationCode.builder().mandatory(true).build()))
        .children(List.of(children))
        .build();
  }
}
