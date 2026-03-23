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
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleLimit;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleLimit;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804.FK7804PdfSpecification;

class QuestionArbetsformagaLangreAnBeslutsstodTest {

  private static final ElementId ELEMENT_ID = new ElementId("37");

  @Test
  void shouldContainCorrectPdfConfiguration() {
    final var elementSpecification =
        QuestionArbetsformagaLangreAnBeslutsstod.questionArbetsformagaLangreAnBeslutsstod();
    final var expected =
        PdfConfigurationText.builder()
            .pdfFieldId(new PdfFieldId("form1[0].Sida3[0].flt_txtArbetsförmaga[0]"))
            .overflowSheetFieldId(
                new PdfFieldId("form1[0].#subform[4].flt_txtFortsattningsblad[0]"))
            .maxLength(3 * FK7804PdfSpecification.PDF_TEXT_FIELD_ROW_LENGTH)
            .build();
    assertEquals(expected, elementSpecification.pdfConfiguration());
  }

  @Test
  void shouldIncludeId() {
    final var element =
        QuestionArbetsformagaLangreAnBeslutsstod.questionArbetsformagaLangreAnBeslutsstod();
    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shouldIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationTextArea.builder()
            .name(
                "Patientens arbetsförmåga bedöms nedsatt längre tid än den som Socialstyrelsens försäkringsmedicinska beslutsstöd anger, därför att")
            .id(new FieldId("37.1"))
            .description(
                """
            <ul><li>Om sjukdomen inte följer förväntat förlopp ska det framgå på vilket sätt.</li><li>Om det inträffar komplikationer som gör att det tar längre tid att återfå arbetsförmågan ska du beskriva detta.</li><li>Om sjukskrivningslängden påverkas av flera sjukdomar, så kallad samsjuklighet, ska du beskriva detta.</li></ul>
            """)
            .build();

    final var element =
        QuestionArbetsformagaLangreAnBeslutsstod.questionArbetsformagaLangreAnBeslutsstod();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shouldIncludeRules() {
    final var expectedRules =
        List.of(
            ElementRuleLimit.builder()
                .id(new ElementId("37"))
                .type(ElementRuleType.TEXT_LIMIT)
                .limit(new RuleLimit((short) 4000))
                .build(),
            ElementRuleExpression.builder()
                .id(new ElementId("27"))
                .type(ElementRuleType.HIDE)
                .expression(new RuleExpression("$27.1"))
                .build());

    final var element =
        QuestionArbetsformagaLangreAnBeslutsstod.questionArbetsformagaLangreAnBeslutsstod();

    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shouldIncludeValidations() {
    final var expectedValidations =
        List.of(ElementValidationText.builder().mandatory(false).limit(4000).build());

    final var element =
        QuestionArbetsformagaLangreAnBeslutsstod.questionArbetsformagaLangreAnBeslutsstod();

    assertEquals(expectedValidations, element.validations());
  }
}
