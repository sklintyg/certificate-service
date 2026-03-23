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
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.elements.CategoryDiagnos.categoryDiagnos;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7427.elements.QuestionDiagnos.DIAGNOSIS_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7427.elements.QuestionSymtom.QUESTION_SYMTOM_ID;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleMandatoryCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ExpressionOperandType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCategory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

class CategoryDiagnosTest {

  private static final ElementId ELEMENT_ID = new ElementId("KAT_2");

  @Test
  void shallIncludeId() {
    final var element = categoryDiagnos();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCategory.builder().name("Diagnos").build();

    final var element = categoryDiagnos();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shallIncludeChildren() {
    final var child = ElementSpecification.builder().id(new ElementId("CHILD_1")).build();

    final var element = categoryDiagnos(child);

    assertEquals(List.of(child), element.children());
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
                            new ElementId("58"), new FieldId("huvuddiagnos")),
                        CertificateElementRuleFactory.mandatory(
                            new ElementId("55"), new FieldId("55.1"))))
                .build());

    final var element = categoryDiagnos();

    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shallIncludeValidations() {
    final var expectedValidations =
        List.of(
            ElementValidationCategory.builder()
                .mandatory(true)
                .elements(List.of(DIAGNOSIS_ID, QUESTION_SYMTOM_ID))
                .build());

    final var element = categoryDiagnos();

    assertEquals(expectedValidations, element.validations());
  }
}
