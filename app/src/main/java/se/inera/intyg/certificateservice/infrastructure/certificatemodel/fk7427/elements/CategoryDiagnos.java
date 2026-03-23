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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7427.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7427.elements.QuestionDiagnos.DIAGNOSIS_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7427.elements.QuestionDiagnos.DIAGNOS_1;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7427.elements.QuestionSymtom.QUESTION_SYMTOM_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7427.elements.QuestionSymtom.QUESTION_SYMTOM_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleMandatoryCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ExpressionOperandType;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCategory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class CategoryDiagnos {

  private static final ElementId DIAGNOS_CATEGORY_ID = new ElementId("KAT_2");

  private CategoryDiagnos() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification categoryDiagnos(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(DIAGNOS_CATEGORY_ID)
        .configuration(ElementConfigurationCategory.builder().name("Diagnos").build())
        .rules(
            List.of(
                ElementRuleMandatoryCategory.builder()
                    .operandType(ExpressionOperandType.OR)
                    .type(ElementRuleType.CATEGORY_MANDATORY)
                    .elementRuleExpressions(
                        List.of(
                            CertificateElementRuleFactory.mandatory(DIAGNOSIS_ID, DIAGNOS_1),
                            CertificateElementRuleFactory.mandatory(
                                QUESTION_SYMTOM_ID, QUESTION_SYMTOM_FIELD_ID)))
                    .build()))
        .validations(
            List.of(
                ElementValidationCategory.builder()
                    .mandatory(true)
                    .elements(List.of(DIAGNOSIS_ID, QUESTION_SYMTOM_ID))
                    .build()))
        .children(List.of(children))
        .build();
  }
}
