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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.validation.CertificateDataValidationCategoryMandatory;
import se.inera.intyg.certificateservice.application.certificate.dto.validation.ExpressionTypeEnum;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleMandatoryCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ExpressionOperandType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;

class CertificateDataValidationCategoryMandatoryConverterTest {

  private static final String EXPECTED_ID = "expectedId";
  private static final String EXPECTED_EXPRESSION = "expectedExpression";
  private CertificateDataValidationCategoryMandatoryConverter converter;

  @BeforeEach
  void setUp() {
    converter = new CertificateDataValidationCategoryMandatoryConverter();
  }

  @Test
  void shallReturnType() {
    assertEquals(ElementRuleType.CATEGORY_MANDATORY, converter.getType());
  }

  @Test
  void shallThrowIfWrongTypeOfRule() {
    final var elementRuleExpression =
        ElementRuleExpression.builder().type(ElementRuleType.SHOW).build();

    assertThrows(IllegalArgumentException.class, () -> converter.convert(elementRuleExpression));
  }

  @Test
  void shallReturnCertificateDataValidationCategoryMandatoryWithExpressionType() {
    final var elementRuleMandatoryCategory =
        ElementRuleMandatoryCategory.builder().operandType(ExpressionOperandType.OR).build();
    final var result =
        (CertificateDataValidationCategoryMandatory)
            converter.convert(elementRuleMandatoryCategory);
    assertEquals(ExpressionTypeEnum.OR, result.getExpressionType());
  }

  @Test
  void shallReturnCertificateDataValidationCategoryMandatoryWithExpression() {
    final var elementRuleMandatoryCategory =
        ElementRuleMandatoryCategory.builder()
            .operandType(ExpressionOperandType.OR)
            .elementRuleExpressions(
                List.of(
                    ElementRuleExpression.builder()
                        .id(new ElementId(EXPECTED_ID))
                        .expression(new RuleExpression(EXPECTED_EXPRESSION))
                        .build()))
            .build();
    final var result =
        (CertificateDataValidationCategoryMandatory)
            converter.convert(elementRuleMandatoryCategory);

    assertAll(
        () -> assertEquals(EXPECTED_ID, result.getQuestions().getFirst().getQuestionId()),
        () -> assertEquals(EXPECTED_EXPRESSION, result.getQuestions().getFirst().getExpression()));
  }
}
