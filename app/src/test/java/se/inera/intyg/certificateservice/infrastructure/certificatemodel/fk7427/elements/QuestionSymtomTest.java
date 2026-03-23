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
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7427.FK7427PdfSpecification.ROW_MAX_LENGTH;

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

class QuestionSymtomTest {

  private static final ElementId ELEMENT_ID = new ElementId("55");

  @Test
  void shallIncludeId() {
    final var element = QuestionSymtom.questionSymtom();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationTextArea.builder()
            .name("Fyll i vilka symtom barnet har om diagnos inte är fastställd")
            .id(new FieldId("55.1"))
            .build();

    final var element = QuestionSymtom.questionSymtom();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shallIncludeRules() {
    final var expectedRules =
        List.of(
            ElementRuleLimit.builder()
                .id(new ElementId("55"))
                .type(ElementRuleType.TEXT_LIMIT)
                .limit(new RuleLimit((short) 4000))
                .build());

    final var element = QuestionSymtom.questionSymtom();

    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shallIncludeValidations() {
    final var expectedValidations = List.of(ElementValidationText.builder().limit(4000).build());

    final var element = QuestionSymtom.questionSymtom();

    assertEquals(expectedValidations, element.validations());
  }

  @Test
  void shallIncludePdfConfiguration() {
    final var expected =
        PdfConfigurationText.builder()
            .pdfFieldId(new PdfFieldId("form1[0].#subform[0].flt_txtFlerradig[0]"))
            .maxLength(ROW_MAX_LENGTH * 6)
            .overflowSheetFieldId(
                new PdfFieldId("form1[0].#subform[3].flt_txtFortsattningsblad[0]"))
            .build();

    final var element = QuestionSymtom.questionSymtom();

    assertEquals(expected, element.pdfConfiguration());
  }
}
