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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.element;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Period;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationDate;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.elements.QuestionBeraknatFodelsedatum;

class QuestionBeraknatFodelsedatumTest {

  private static final ElementId ELEMENT_ID = new ElementId("54");

  @Test
  void shallIncludeId() {
    final var element = QuestionBeraknatFodelsedatum.questionBeraknatFodelsedatum();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationDate.builder()
            .name("Datum")
            .id(new FieldId("54.1"))
            .min(Period.ofDays(0))
            .max(Period.ofYears(1))
            .build();

    final var element = QuestionBeraknatFodelsedatum.questionBeraknatFodelsedatum();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shallIncludeRules() {
    final var expectedRules =
        List.of(
            ElementRuleExpression.builder()
                .id(new ElementId("54"))
                .type(ElementRuleType.MANDATORY)
                .expression(new RuleExpression("$54.1"))
                .build());

    final var element = QuestionBeraknatFodelsedatum.questionBeraknatFodelsedatum();

    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shallIncludeValidations() {
    final var expectedValidations =
        List.of(
            ElementValidationDate.builder()
                .mandatory(true)
                .min(Period.ofDays(0))
                .max(Period.ofYears(1))
                .build());

    final var element = QuestionBeraknatFodelsedatum.questionBeraknatFodelsedatum();

    assertEquals(expectedValidations, element.validations());
  }

  @Test
  void shallIncludePdfConfiguration() {
    final var expected =
        PdfConfigurationDate.builder()
            .pdfFieldId(new PdfFieldId("form1[0].#subform[0].flt_dat[0]"))
            .build();

    final var element = QuestionBeraknatFodelsedatum.questionBeraknatFodelsedatum();

    assertEquals(expected, element.pdfConfiguration());
  }
}
