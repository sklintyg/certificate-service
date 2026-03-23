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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDateRange;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDateRange;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationDateRange;

class QuestionPeriodVardEllerTillsynTest {

  private static final ElementId ELEMENT_ID = new ElementId("62.6");

  @Test
  void shouldIncludeId() {
    final var element = QuestionPeriodVardEllerTillsyn.questionPeriodVardEllerTillsyn();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shouldIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationDateRange.builder()
            .name("Under vilken period behöver barnet vård eller tillsyn?")
            .labelFrom("Fr.o.m")
            .labelTo("T.o.m")
            .id(new FieldId("62.6"))
            .build();

    final var element = QuestionPeriodVardEllerTillsyn.questionPeriodVardEllerTillsyn();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shouldIncludeRules() {
    final var expectedRules =
        List.of(
            ElementRuleExpression.builder()
                .id(new ElementId("62.6"))
                .type(ElementRuleType.MANDATORY)
                .expression(new RuleExpression("$62.6"))
                .build());

    final var element = QuestionPeriodVardEllerTillsyn.questionPeriodVardEllerTillsyn();

    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shouldIncludeValidation() {
    final var expectedValidation = ElementValidationDateRange.builder().mandatory(true).build();

    final var element = QuestionPeriodVardEllerTillsyn.questionPeriodVardEllerTillsyn();

    assertEquals(List.of(expectedValidation), element.validations());
  }

  @Test
  void shouldIncludePdfConfiguration() {
    final var expectedPdfConfiguration =
        PdfConfigurationDateRange.builder()
            .from(new PdfFieldId("form1[0].#subform[2].flt_datumFranMed[0]"))
            .to(new PdfFieldId("form1[0].#subform[2].flt_datumTillMed[0]"))
            .build();

    final var element = QuestionPeriodVardEllerTillsyn.questionPeriodVardEllerTillsyn();

    assertEquals(expectedPdfConfiguration, element.pdfConfiguration());
  }

  @Test
  void shouldHaveIncludeWhenRenewingFalse() {
    final var element = QuestionPeriodVardEllerTillsyn.questionPeriodVardEllerTillsyn();
    assertFalse(element.includeWhenRenewing());
  }
}
