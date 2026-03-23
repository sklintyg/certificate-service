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
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementLayout;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCode;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvTs002;

public class QuestionBedomning {

  public static final ElementId QUESTION_BEDOMNING_ID = new ElementId("23");
  public static final FieldId QUESTION_BEDOMNING_FIELD_ID = new FieldId("23.1");

  private QuestionBedomning() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionBedomning(ElementSpecification... children) {
    final var radioMultipleCodes =
        List.of(
            CodeFactory.elementConfigurationCode(CodeSystemKvTs002.YES),
            CodeFactory.elementConfigurationCode(CodeSystemKvTs002.NO),
            CodeFactory.elementConfigurationCode(CodeSystemKvTs002.NO_DECISION));

    return ElementSpecification.builder()
        .id(QUESTION_BEDOMNING_ID)
        .configuration(
            ElementConfigurationRadioMultipleCode.builder()
                .id(QUESTION_BEDOMNING_FIELD_ID)
                .name(
                    "Bedöms personen utifrån Transportstyrelsens föreskrifter och allmänna råd (TSFS 2010:125) om medicinska krav för innehav av körkort m.m. ha en sjukdom eller medicinskt tillstånd som innebär en trafiksäkerhetsrisk?")
                .elementLayout(ElementLayout.ROWS)
                .list(radioMultipleCodes)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryOrExist(
                    QUESTION_BEDOMNING_ID,
                    radioMultipleCodes.stream().map(ElementConfigurationCode::id).toList())))
        .validations(List.of(ElementValidationCode.builder().mandatory(true).build()))
        .children(List.of(children))
        .build();
  }
}
