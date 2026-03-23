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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionSmittbararpenning.QUESTION_SMITTBARARPENNING_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionSmittbararpenning.QUESTION_SMITTBARARPENNING_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0002.ARBETSSOKANDE;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0002.FORALDRALEDIG;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0002.NUVARANDE_ARBETE;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0002.STUDIER;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementLayout;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCodeList;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeFactory;

public class QuestionSysselsattning {

  public static final ElementId QUESTION_SYSSELSATTNING_ID = new ElementId("28");
  public static final FieldId QUESTION_SYSSELSATTNING_FIELD_ID = new FieldId("28.1");

  private QuestionSysselsattning() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionSysselsattning(ElementSpecification... children) {
    final var checkboxes =
        List.of(
            CodeFactory.elementConfigurationCode(NUVARANDE_ARBETE),
            CodeFactory.elementConfigurationCode(ARBETSSOKANDE),
            CodeFactory.elementConfigurationCode(FORALDRALEDIG),
            CodeFactory.elementConfigurationCode(STUDIER));

    return ElementSpecification.builder()
        .id(QUESTION_SYSSELSATTNING_ID)
        .configuration(
            ElementConfigurationCheckboxMultipleCode.builder()
                .id(QUESTION_SYSSELSATTNING_FIELD_ID)
                .name("I relation till vilken sysselsättning bedömer du arbetsförmågan?")
                .description(
                    "Om du kryssar i flera val är det viktigt att du tydliggör under \"Övriga upplysningar\" om sjukskrivningens omfattning eller period skiljer sig åt mellan olika sysselsättningar.")
                .elementLayout(ElementLayout.ROWS)
                .list(checkboxes)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryOrExist(
                    QUESTION_SYSSELSATTNING_ID,
                    List.of(
                        new FieldId(ARBETSSOKANDE.code()),
                        new FieldId(FORALDRALEDIG.code()),
                        new FieldId(NUVARANDE_ARBETE.code()),
                        new FieldId(STUDIER.code()))),
                CertificateElementRuleFactory.hide(
                    QUESTION_SMITTBARARPENNING_ID, QUESTION_SMITTBARARPENNING_FIELD_ID)))
        .validations(List.of(ElementValidationCodeList.builder().mandatory(true).build()))
        .shouldValidate(
            ElementDataPredicateFactory.checkboxBoolean(QUESTION_SMITTBARARPENNING_ID, false))
        .children(List.of(children))
        .build();
  }
}
