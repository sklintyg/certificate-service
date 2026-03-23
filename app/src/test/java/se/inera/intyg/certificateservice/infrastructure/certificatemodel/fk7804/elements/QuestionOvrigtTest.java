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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleLimit;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleLimit;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804.FK7804PdfSpecification;

class QuestionOvrigtTest {

  @Test
  void shouldContainCorrectPdfConfiguration() {
    final var elementSpecification = QuestionOvrigt.questionOvrigt();
    final var expected =
        PdfConfigurationText.builder()
            .pdfFieldId(new PdfFieldId("form1[0].Sida3[0].flt_txtOvrigaUpplysningarl[0]"))
            .overflowSheetFieldId(
                new PdfFieldId("form1[0].#subform[4].flt_txtFortsattningsblad[0]"))
            .maxLength(8 * FK7804PdfSpecification.PDF_TEXT_FIELD_ROW_LENGTH)
            .build();
    assertEquals(expected, elementSpecification.pdfConfiguration());
  }

  @Test
  void shouldIncludeId() {
    final var element = QuestionOvrigt.questionOvrigt();
    assertEquals(new ElementId("25"), element.id());
  }

  @Test
  void shouldIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationTextArea.builder()
            .id(new FieldId("25.1"))
            .name("Övriga upplysningar")
            .build();
    final var element = QuestionOvrigt.questionOvrigt();
    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shouldIncludeRules() {
    final var expectedRules =
        List.of(
            ElementRuleLimit.builder()
                .id(new ElementId("25"))
                .type(ElementRuleType.TEXT_LIMIT)
                .limit(new RuleLimit((short) 4000))
                .build());
    final var element = QuestionOvrigt.questionOvrigt();
    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shouldIncludeValidations() {
    final var expectedValidations =
        List.of(ElementValidationText.builder().mandatory(false).limit(4000).build());
    final var element = QuestionOvrigt.questionOvrigt();
    assertEquals(expectedValidations, element.validations());
  }
}
