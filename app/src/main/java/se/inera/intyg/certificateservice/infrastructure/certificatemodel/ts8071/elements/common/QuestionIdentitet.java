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
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvIdKontroll;

public class QuestionIdentitet {

  public static final ElementId QUESTION_IDENTITET_ID = new ElementId("3");
  public static final FieldId QUESTION_IDENTITET_FIELD_ID = new FieldId("3.1");

  private QuestionIdentitet() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionIdentitet(ElementSpecification... children) {
    final var radioMultipleCodes =
        List.of(
            CodeFactory.elementConfigurationCode(CodeSystemKvIdKontroll.IDK1),
            CodeFactory.elementConfigurationCode(CodeSystemKvIdKontroll.IDK2),
            CodeFactory.elementConfigurationCode(CodeSystemKvIdKontroll.IDK3),
            CodeFactory.elementConfigurationCode(CodeSystemKvIdKontroll.IDK4),
            CodeFactory.elementConfigurationCode(CodeSystemKvIdKontroll.IDK5),
            CodeFactory.elementConfigurationCode(CodeSystemKvIdKontroll.IDK6));

    return ElementSpecification.builder()
        .id(QUESTION_IDENTITET_ID)
        .configuration(
            ElementConfigurationRadioMultipleCode.builder()
                .id(QUESTION_IDENTITET_FIELD_ID)
                .name("Identitet styrkt genom")
                .elementLayout(ElementLayout.COLUMNS)
                .list(radioMultipleCodes)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryOrExist(
                    QUESTION_IDENTITET_ID,
                    radioMultipleCodes.stream().map(ElementConfigurationCode::id).toList())))
        .validations(List.of(ElementValidationCode.builder().mandatory(true).build()))
        .children(List.of(children))
        .build();
  }
}
