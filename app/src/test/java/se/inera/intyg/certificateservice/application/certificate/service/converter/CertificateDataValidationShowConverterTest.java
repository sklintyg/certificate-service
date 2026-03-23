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

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.validation.CertificateDataValidationShow;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleLimit;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;

class CertificateDataValidationShowConverterTest {

  private static final String QUESTION_ID = "questionId";
  private static final String EXPRESSION = "expression";
  private final CertificateDataValidationShowConverter converter =
      new CertificateDataValidationShowConverter();

  @Test
  void shallConvertShowRuleToCertificateDataValidationShow() {
    final var mandatoryRule =
        ElementRuleExpression.builder()
            .type(ElementRuleType.SHOW)
            .id(new ElementId(QUESTION_ID))
            .expression(new RuleExpression(EXPRESSION))
            .build();

    final var result = converter.convert(mandatoryRule);

    assertInstanceOf(CertificateDataValidationShow.class, result);
  }

  @Test
  void shallSetCorrectQuestionIdForShowValidation() {
    final var mandatoryRule =
        ElementRuleExpression.builder()
            .type(ElementRuleType.SHOW)
            .id(new ElementId(QUESTION_ID))
            .expression(new RuleExpression(EXPRESSION))
            .build();

    final var result = converter.convert(mandatoryRule);

    assertEquals(QUESTION_ID, ((CertificateDataValidationShow) result).getQuestionId());
  }

  @Test
  void shallSetCorrectExpressionForShowValidation() {
    final var mandatoryRule =
        ElementRuleExpression.builder()
            .type(ElementRuleType.SHOW)
            .id(new ElementId(QUESTION_ID))
            .expression(new RuleExpression(EXPRESSION))
            .build();

    final var result = converter.convert(mandatoryRule);

    assertEquals(EXPRESSION, ((CertificateDataValidationShow) result).getExpression());
  }

  @Test
  void shallThrowIfWrongType() {
    final var rule = ElementRuleLimit.builder().build();
    assertThrows(IllegalStateException.class, () -> converter.convert(rule));
  }
}
