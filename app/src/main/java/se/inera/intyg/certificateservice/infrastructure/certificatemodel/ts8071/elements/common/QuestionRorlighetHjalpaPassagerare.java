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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvIntygetGallerFor.FORLANG_GR_II_III;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvIntygetGallerFor.GR_II_III;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvIntygetGallerFor.TAXI;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionIntygetAvser.QUESTION_INTYGET_AVSER_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionRorlighet.QUESTION_RORLIGHET_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionRorlighetHjalpaPassagerare {

  public static final ElementId QUESTION_RORLIGHET_HJALPA_PASSAGERARE_ID = new ElementId("10.3");
  public static final FieldId QUESTION_RORLIGHET_HJALPA_PASSAGERARE_FIELD_ID = new FieldId("10.3");

  private QuestionRorlighetHjalpaPassagerare() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionRorlighetHjalpaPassagerare(
      ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(QUESTION_RORLIGHET_HJALPA_PASSAGERARE_ID)
        .configuration(
            ElementConfigurationRadioBoolean.builder()
                .id(QUESTION_RORLIGHET_HJALPA_PASSAGERARE_FIELD_ID)
                .selectedText("Ja")
                .unselectedText("Nej")
                .name(
                    "Har personen en nedsättning av rörelseförmågan som gör att personen inte kan "
                        + "hjälpa passagerare in och ut ur fordonet samt med bilbälte?")
                .build())
        .validations(List.of(ElementValidationBoolean.builder().mandatory(true).build()))
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryExist(
                    QUESTION_RORLIGHET_HJALPA_PASSAGERARE_ID,
                    QUESTION_RORLIGHET_HJALPA_PASSAGERARE_FIELD_ID),
                CertificateElementRuleFactory.show(
                    QUESTION_INTYGET_AVSER_ID,
                    new RuleExpression(
                        String.format(
                            "exists(%s) || exists(%s) || exists(%s)",
                            GR_II_III.code(), FORLANG_GR_II_III.code(), TAXI.code())))))
        .shouldValidate(
            ElementDataPredicateFactory.codeList(
                QUESTION_INTYGET_AVSER_ID,
                List.of(
                    new FieldId(GR_II_III.code()),
                    new FieldId(FORLANG_GR_II_III.code()),
                    new FieldId(TAXI.code()))))
        .mapping(new ElementMapping(QUESTION_RORLIGHET_ID, null))
        .children(List.of(children))
        .build();
  }
}
