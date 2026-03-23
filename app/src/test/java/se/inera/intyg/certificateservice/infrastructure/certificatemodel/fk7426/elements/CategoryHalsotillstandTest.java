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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.elements.QuestionHalsotillstandPsykiska.QUESTION_HALSOTILLSTAND_PSYKISKA_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.elements.QuestionHalsotillstandSomatiska.QUESTION_HALSOTILLSTAND_SOMATISKA_ID;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleMandatoryCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ExpressionOperandType;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCategory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

class CategoryHalsotillstandTest {

  private static final ElementId ELEMENT_ID = new ElementId("KAT_3");

  @Test
  void shallIncludeId() {
    final var element = CategoryHalsotillstand.categoryHalsotillstand();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCategory.builder().name("Barnets hälsotillstånd").build();

    final var element = CategoryHalsotillstand.categoryHalsotillstand();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shallIncludeRules() {
    final var expectedRules =
        List.of(
            ElementRuleMandatoryCategory.builder()
                .operandType(ExpressionOperandType.OR)
                .type(ElementRuleType.CATEGORY_MANDATORY)
                .elementRuleExpressions(
                    List.of(
                        CertificateElementRuleFactory.mandatory(
                            QuestionHalsotillstandPsykiska.QUESTION_HALSOTILLSTAND_PSYKISKA_ID,
                            QuestionHalsotillstandPsykiska
                                .QUESTION_HALSOTILLSTAND_PSYKISKA_FIELD_ID),
                        CertificateElementRuleFactory.mandatory(
                            QuestionHalsotillstandSomatiska.QUESTION_HALSOTILLSTAND_SOMATISKA_ID,
                            QuestionHalsotillstandSomatiska
                                .QUESTION_HALSOTILLSTAND_SOMATISKA_FIELD_ID)))
                .build());

    final var element = CategoryHalsotillstand.categoryHalsotillstand();

    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shallIncludeValidations() {
    final var expectedValidations =
        List.of(
            ElementValidationCategory.builder()
                .mandatory(true)
                .elements(
                    List.of(
                        QUESTION_HALSOTILLSTAND_PSYKISKA_ID, QUESTION_HALSOTILLSTAND_SOMATISKA_ID))
                .build());

    final var element = CategoryHalsotillstand.categoryHalsotillstand();

    assertEquals(expectedValidations, element.validations());
  }
}
