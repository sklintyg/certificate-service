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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationInteger;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationInteger;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionPeriodProcentBedomning {

  public static final ElementId QUESTION_PERIOD_PROCENT_BEDOMNING_ID = new ElementId("7");
  public static final FieldId QUESTION_PERIOD_PROCENT_BEDOMNING_FIELD_ID = new FieldId("7.1");

  private QuestionPeriodProcentBedomning() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionPeriodProcentBedomning() {
    return ElementSpecification.builder()
        .id(QUESTION_PERIOD_PROCENT_BEDOMNING_ID)
        .configuration(
            ElementConfigurationInteger.builder()
                .id(QUESTION_PERIOD_PROCENT_BEDOMNING_FIELD_ID)
                .name("Patientens arbetsförmåga bedöms vara nedsatt med (ange antal procent)")
                .min(1)
                .max(100)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatory(
                    QUESTION_PERIOD_PROCENT_BEDOMNING_ID,
                    QUESTION_PERIOD_PROCENT_BEDOMNING_FIELD_ID)))
        .validations(
            List.of(ElementValidationInteger.builder().mandatory(true).min(1).max(100).build()))
        .build();
  }
}
