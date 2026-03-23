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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.elements.QuestionHalsotillstandPsykiska.QUESTION_HALSOTILLSTAND_PSYKISKA_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.elements.QuestionHalsotillstandPsykiska.QUESTION_HALSOTILLSTAND_PSYKISKA_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.elements.QuestionHalsotillstandSomatiska.QUESTION_HALSOTILLSTAND_SOMATISKA_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.elements.QuestionHalsotillstandSomatiska.QUESTION_HALSOTILLSTAND_SOMATISKA_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleMandatoryCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ExpressionOperandType;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCategory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class CategoryHalsotillstand {

  private static final ElementId HALSOTILLSTAND_CATEGORY_ID = new ElementId("KAT_3");

  private CategoryHalsotillstand() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification categoryHalsotillstand(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(HALSOTILLSTAND_CATEGORY_ID)
        .configuration(
            ElementConfigurationCategory.builder().name("Barnets hälsotillstånd").build())
        .children(List.of(children))
        .rules(
            List.of(
                ElementRuleMandatoryCategory.builder()
                    .operandType(ExpressionOperandType.OR)
                    .type(ElementRuleType.CATEGORY_MANDATORY)
                    .elementRuleExpressions(
                        List.of(
                            CertificateElementRuleFactory.mandatory(
                                QUESTION_HALSOTILLSTAND_PSYKISKA_ID,
                                QUESTION_HALSOTILLSTAND_PSYKISKA_FIELD_ID),
                            CertificateElementRuleFactory.mandatory(
                                QUESTION_HALSOTILLSTAND_SOMATISKA_ID,
                                QUESTION_HALSOTILLSTAND_SOMATISKA_FIELD_ID)))
                    .build()))
        .validations(
            List.of(
                ElementValidationCategory.builder()
                    .mandatory(true)
                    .elements(
                        List.of(
                            QUESTION_HALSOTILLSTAND_PSYKISKA_ID,
                            QUESTION_HALSOTILLSTAND_SOMATISKA_ID))
                    .build()))
        .build();
  }
}
