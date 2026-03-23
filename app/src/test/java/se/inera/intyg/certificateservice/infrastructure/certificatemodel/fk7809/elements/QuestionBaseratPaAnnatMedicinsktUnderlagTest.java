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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7809.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;

class QuestionBaseratPaAnnatMedicinsktUnderlagTest {

  private static final ElementId ELEMENT_ID = new ElementId("3");

  @Test
  void shallIncludeId() {
    final var element =
        QuestionBaseratPaAnnatMedicinsktUnderlag.questionBaseratPaAnnatMedicinsktUnderlag();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationRadioBoolean.builder()
            .name("Är utlåtandet även baserat på andra medicinska utredningar eller underlag?")
            .id(new FieldId("3.1"))
            .selectedText("Ja")
            .unselectedText("Nej")
            .build();

    final var element =
        QuestionBaseratPaAnnatMedicinsktUnderlag.questionBaseratPaAnnatMedicinsktUnderlag();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shallIncludeRules() {
    final var expectedRule =
        List.of(
            ElementRuleExpression.builder()
                .id(ELEMENT_ID)
                .type(ElementRuleType.MANDATORY)
                .expression(new RuleExpression("exists($3.1)"))
                .build());

    final var element =
        QuestionBaseratPaAnnatMedicinsktUnderlag.questionBaseratPaAnnatMedicinsktUnderlag();

    assertEquals(expectedRule, element.rules());
  }

  @Test
  void shallIncludeValidation() {
    final var expectedValidations =
        List.of(ElementValidationBoolean.builder().mandatory(true).build());

    final var element =
        QuestionBaseratPaAnnatMedicinsktUnderlag.questionBaseratPaAnnatMedicinsktUnderlag();

    assertEquals(expectedValidations, element.validations());
  }

  @Test
  void shallIncludePdfConfiguration() {
    final var expected =
        PdfConfigurationBoolean.builder()
            .checkboxTrue(new PdfFieldId("form1[0].#subform[0].ksr_JaFyll[0]"))
            .checkboxFalse(new PdfFieldId("form1[0].#subform[0].ksr_2_1[0]"))
            .build();

    final var element =
        QuestionBaseratPaAnnatMedicinsktUnderlag.questionBaseratPaAnnatMedicinsktUnderlag();

    assertEquals(expected, element.pdfConfiguration());
  }
}
