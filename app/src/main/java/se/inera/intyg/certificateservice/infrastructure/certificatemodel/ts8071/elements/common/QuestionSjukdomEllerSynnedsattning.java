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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionSynfunktioner.QUESTION_SYNFUNKTIONER_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionSynfunktioner.QUESTION_SYNFUNKTIONER_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionSjukdomEllerSynnedsattning {

  public static final ElementId QUESTION_SJUKDOM_ELLER_SYNNEDSATTNING_ID = new ElementId("7");
  public static final FieldId QUESTION_SJUKDOM_ELLER_SYNNEDSATTNING_FIELD_ID = new FieldId("7.1");

  private QuestionSjukdomEllerSynnedsattning() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionSjukdomEllerSynnedsattning(
      ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(QUESTION_SJUKDOM_ELLER_SYNNEDSATTNING_ID)
        .configuration(
            ElementConfigurationRadioBoolean.builder()
                .id(QUESTION_SJUKDOM_ELLER_SYNNEDSATTNING_FIELD_ID)
                .description(
                    "Exempel på vanligt förekommande ögonsjukdomar är glaukom, retinopati och retinitis pigmentosa. Exempel på synnedsättning kan vara "
                        + "dubbelseende, syn med enbart ett öga eller plötsligt nedsatt synskärpa.")
                .selectedText("Ja")
                .unselectedText("Nej")
                .name("Finns uppgift om ögonsjukdom eller synnedsättning?")
                .build())
        .validations(List.of(ElementValidationBoolean.builder().mandatory(true).build()))
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryExist(
                    QUESTION_SJUKDOM_ELLER_SYNNEDSATTNING_ID,
                    QUESTION_SJUKDOM_ELLER_SYNNEDSATTNING_FIELD_ID),
                CertificateElementRuleFactory.showIfNot(
                    QUESTION_SYNFUNKTIONER_ID, QUESTION_SYNFUNKTIONER_FIELD_ID)))
        .shouldValidate(ElementDataPredicateFactory.valueBoolean(QUESTION_SYNFUNKTIONER_ID, false))
        .children(List.of(children))
        .build();
  }
}
