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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.validation.CertificateDataValidationDisableSubElement;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleLimit;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;

class CertificateDataValidationDisableSubElementConverterTest {

  private static final String QUESTION_ID = "questionId";
  private static final String EXPRESSION = "expression";
  private final CertificateDataValidationDisableSubElementConverter converter =
      new CertificateDataValidationDisableSubElementConverter();

  @Test
  void shallConvertDisableRuleToCertificateDataValidationDisable() {
    final var disableRule =
        ElementRuleExpression.builder()
            .type(ElementRuleType.DISABLE_SUB_ELEMENT)
            .id(new ElementId(QUESTION_ID))
            .expression(new RuleExpression(EXPRESSION))
            .affectedSubElements(List.of(new FieldId("F1"), new FieldId("F2")))
            .build();

    final var result = converter.convert(disableRule);

    assertInstanceOf(CertificateDataValidationDisableSubElement.class, result);
  }

  @Test
  void shallConvertAffectedIds() {
    final var disableRule =
        ElementRuleExpression.builder()
            .type(ElementRuleType.DISABLE_SUB_ELEMENT)
            .id(new ElementId(QUESTION_ID))
            .expression(new RuleExpression(EXPRESSION))
            .affectedSubElements(List.of(new FieldId("F1"), new FieldId("F2")))
            .build();

    final var result = converter.convert(disableRule);

    assertEquals(
        List.of("F1", "F2"), ((CertificateDataValidationDisableSubElement) result).getId());
  }

  @Test
  void shallSetCorrectQuestionIdForDisableValidation() {
    final var disableRule =
        ElementRuleExpression.builder()
            .type(ElementRuleType.DISABLE_SUB_ELEMENT)
            .id(new ElementId(QUESTION_ID))
            .expression(new RuleExpression(EXPRESSION))
            .build();

    final var result = converter.convert(disableRule);

    assertEquals(
        QUESTION_ID, ((CertificateDataValidationDisableSubElement) result).getQuestionId());
  }

  @Test
  void shallSetCorrectExpressionForDisableValidation() {
    final var disableRule =
        ElementRuleExpression.builder()
            .type(ElementRuleType.DISABLE)
            .id(new ElementId(QUESTION_ID))
            .expression(new RuleExpression(EXPRESSION))
            .build();

    final var result = converter.convert(disableRule);

    assertEquals(EXPRESSION, ((CertificateDataValidationDisableSubElement) result).getExpression());
  }

  @Test
  void shallThrowIfWrongType() {
    final var rule = ElementRuleLimit.builder().build();
    assertThrows(IllegalStateException.class, () -> converter.convert(rule));
  }
}
