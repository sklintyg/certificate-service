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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionPrognos.QUESTION_PROGNOS_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationInteger;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationInteger;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0006;

public class QuestionAntalManader {

  public static final ElementId QUESTION_ANTAL_MANADER_ID = new ElementId("39.4");
  public static final FieldId QUESTION_ANTAL_MANADER_FIELD_ID = new FieldId("39.4");

  private QuestionAntalManader() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionAntalManader() {
    return ElementSpecification.builder()
        .id(QUESTION_ANTAL_MANADER_ID)
        .includeWhenRenewing(false)
        .configuration(
            ElementConfigurationInteger.builder()
                .id(QUESTION_ANTAL_MANADER_FIELD_ID)
                .name("Ange antal månader")
                .min(1)
                .max(99)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatory(
                    QUESTION_ANTAL_MANADER_ID, QUESTION_ANTAL_MANADER_FIELD_ID),
                CertificateElementRuleFactory.show(
                    QUESTION_PROGNOS_ID,
                    new FieldId(CodeSystemKvFkmu0006.ATER_X_ANTAL_MANADER.code()))))
        .validations(
            List.of(ElementValidationInteger.builder().mandatory(true).min(1).max(99).build()))
        .shouldValidate(
            ElementDataPredicateFactory.codes(
                QUESTION_PROGNOS_ID,
                List.of(new FieldId(CodeSystemKvFkmu0006.ATER_X_ANTAL_MANADER.code()))))
        .mapping(new ElementMapping(QUESTION_PROGNOS_ID, CodeSystemKvFkmu0006.ATER_X_ANTAL_MANADER))
        .includeWhenRenewing(false)
        .build();
  }
}
