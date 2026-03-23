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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.v1.QuestionKorrigeringAvSynskarpaV1.QUESTION_KORRIGERING_AV_SYNSKARPA_V1_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKorrigeringAvSynskarpa;

public class QuestionKorrigeringAvSynskarpaStyrkaOverV1 {

  public static final ElementId QUESTION_KORRIGERING_AV_SYNSKARPA_STRYKA_UNDER_V1_ID =
      new ElementId("6.4");
  public static final FieldId QUESTION_KORRIGERING_AV_SYNSKARPA_STRYKA_UNDER_V1_FIELD_ID =
      new FieldId("6.4");

  private QuestionKorrigeringAvSynskarpaStyrkaOverV1() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionKorrigeringAvSynskarpaStyrkaOverV1() {
    return ElementSpecification.builder()
        .id(QUESTION_KORRIGERING_AV_SYNSKARPA_STRYKA_UNDER_V1_ID)
        .configuration(
            ElementConfigurationRadioBoolean.builder()
                .id(QUESTION_KORRIGERING_AV_SYNSKARPA_STRYKA_UNDER_V1_FIELD_ID)
                .selectedText("Ja")
                .unselectedText("Nej")
                .name(
                    "Glasögon, något av glasen har en styrka över plus 8 dioptrier. Tolereras korrektionen väl?")
                .build())
        .validations(List.of(ElementValidationBoolean.builder().mandatory(true).build()))
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryExist(
                    QUESTION_KORRIGERING_AV_SYNSKARPA_STRYKA_UNDER_V1_ID,
                    QUESTION_KORRIGERING_AV_SYNSKARPA_STRYKA_UNDER_V1_FIELD_ID),
                CertificateElementRuleFactory.show(
                    QUESTION_KORRIGERING_AV_SYNSKARPA_V1_ID,
                    new FieldId(
                        CodeSystemKorrigeringAvSynskarpa.GLASOGON_MED_STYRKA_OVER_8_DIOPTRIER
                            .code()))))
        .shouldValidate(
            ElementDataPredicateFactory.codeList(
                QUESTION_KORRIGERING_AV_SYNSKARPA_V1_ID,
                List.of(
                    new FieldId(
                        CodeSystemKorrigeringAvSynskarpa.GLASOGON_MED_STYRKA_OVER_8_DIOPTRIER
                            .code()))))
        .mapping(new ElementMapping(QUESTION_KORRIGERING_AV_SYNSKARPA_V1_ID, null))
        .build();
  }
}
