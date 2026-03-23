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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionSjukdomEllerSynnedsattning.QUESTION_SJUKDOM_ELLER_SYNNEDSATTNING_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionSjukdomshistorik.QUESTION_SJUKDOMSHISTORIK_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionSjukdomshistorik.QUESTION_SJUKDOMSHISTORIK_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionSjukdomshistorikBeskrivningV2 {

  public static final ElementId QUESTION_SJUKDOMSHISTORIK_BESKRIVNING_V2_ID = new ElementId("7.4");
  public static final FieldId QUESTION_SJUKDOMSHISTORIK_BESKRIVNING_V2_FIELD_ID =
      new FieldId("7.4");
  private static final int TEXT_LIMIT = 250;

  private QuestionSjukdomshistorikBeskrivningV2() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionSjukdomshistorikBeskrivningV2() {
    return ElementSpecification.builder()
        .id(QUESTION_SJUKDOMSHISTORIK_BESKRIVNING_V2_ID)
        .configuration(
            ElementConfigurationTextArea.builder()
                .id(QUESTION_SJUKDOMSHISTORIK_BESKRIVNING_V2_FIELD_ID)
                .name("Ange vad")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.show(
                    QUESTION_SJUKDOMSHISTORIK_ID, QUESTION_SJUKDOMSHISTORIK_FIELD_ID),
                CertificateElementRuleFactory.mandatory(
                    QUESTION_SJUKDOMSHISTORIK_BESKRIVNING_V2_ID,
                    QUESTION_SJUKDOMSHISTORIK_BESKRIVNING_V2_FIELD_ID),
                CertificateElementRuleFactory.limit(
                    QUESTION_SJUKDOMSHISTORIK_BESKRIVNING_V2_ID, (short) TEXT_LIMIT)))
        .shouldValidate(ElementDataPredicateFactory.valueBoolean(QUESTION_SJUKDOMSHISTORIK_ID))
        .mapping(new ElementMapping(QUESTION_SJUKDOM_ELLER_SYNNEDSATTNING_ID, null))
        .validations(
            List.of(ElementValidationText.builder().mandatory(true).limit(TEXT_LIMIT).build()))
        .build();
  }
}
