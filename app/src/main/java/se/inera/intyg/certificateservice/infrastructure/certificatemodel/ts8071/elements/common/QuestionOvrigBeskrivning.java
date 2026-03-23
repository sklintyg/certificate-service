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
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionOvrigBeskrivning {

  public static final ElementId QUESTION_OVRIG_BESKRIVNING_ID = new ElementId("22");
  public static final FieldId QUESTION_OVRIG_BESKRIVNING_FIELD_ID = new FieldId("22.1");

  private QuestionOvrigBeskrivning() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionOvrigBeskrivning() {
    return ElementSpecification.builder()
        .id(QUESTION_OVRIG_BESKRIVNING_ID)
        .configuration(
            ElementConfigurationTextArea.builder()
                .id(QUESTION_OVRIG_BESKRIVNING_FIELD_ID)
                .name("Ange övriga upplysningar")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.limit(QUESTION_OVRIG_BESKRIVNING_ID, (short) 400)))
        .validations(List.of(ElementValidationText.builder().limit(400).build()))
        .build();
  }
}
