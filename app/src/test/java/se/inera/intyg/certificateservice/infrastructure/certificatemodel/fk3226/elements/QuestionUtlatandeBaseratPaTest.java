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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.elements.QuestionUtlatandeBaseratPa.questionUtlatandeBaseratPa;

import java.time.Period;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CheckboxDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDateCheckbox;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDateList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationDateList;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0001;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.v1.FK3226V1PrintTagProvider;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.v1_1.FK3226V1_1PrintTagProvider;

class QuestionUtlatandeBaseratPaTest {

  private static final ElementId ELEMENT_ID = new ElementId("1");

  @Test
  void shallIncludeId() {
    final var element = questionUtlatandeBaseratPa(new FK3226V1PrintTagProvider());

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCheckboxMultipleDate.builder()
            .name("Utlåtandet är baserat på")
            .id(new FieldId("1.1"))
            .dates(
                List.of(
                    CheckboxDate.builder()
                        .id(new FieldId("undersokningAvPatienten"))
                        .label(CodeSystemKvFkmu0001.UNDERSOKNING.displayName())
                        .code(CodeSystemKvFkmu0001.UNDERSOKNING)
                        .min(null)
                        .max(Period.ofDays(0))
                        .build(),
                    CheckboxDate.builder()
                        .id(new FieldId("journaluppgifter"))
                        .label(CodeSystemKvFkmu0001.JOURNALUPPGIFTER.displayName())
                        .code(CodeSystemKvFkmu0001.JOURNALUPPGIFTER)
                        .min(null)
                        .max(Period.ofDays(0))
                        .build(),
                    CheckboxDate.builder()
                        .id(new FieldId("annat"))
                        .label(CodeSystemKvFkmu0001.ANNAT.displayName())
                        .code(CodeSystemKvFkmu0001.ANNAT)
                        .min(null)
                        .max(Period.ofDays(0))
                        .build()))
            .build();

    final var element = questionUtlatandeBaseratPa(new FK3226V1PrintTagProvider());

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shallIncludeRules() {
    final var expectedRules =
        List.of(
            ElementRuleExpression.builder()
                .id(ELEMENT_ID)
                .type(ElementRuleType.MANDATORY)
                .expression(
                    new RuleExpression("$undersokningAvPatienten || $journaluppgifter || $annat"))
                .build());

    final var element = questionUtlatandeBaseratPa(new FK3226V1PrintTagProvider());

    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shallIncludeValidations() {
    final var expectedValidations =
        List.of(ElementValidationDateList.builder().mandatory(true).max(Period.ofDays(0)).build());

    final var element = questionUtlatandeBaseratPa(new FK3226V1PrintTagProvider());

    assertEquals(expectedValidations, element.validations());
  }

  @Test
  void shallNotIncludeWhenRenewing() {
    final var element = questionUtlatandeBaseratPa(new FK3226V1PrintTagProvider());

    assertFalse(element.includeWhenRenewing());
  }

  @Nested
  class V1 {

    @Test
    void shallIncludePdfConfiguration() {
      final var expected =
          PdfConfigurationDateList.builder()
              .dateCheckboxes(
                  Map.of(
                      new FieldId("undersokningAvPatienten"),
                      PdfConfigurationDateCheckbox.builder()
                          .checkboxFieldId(
                              new PdfFieldId("form1[0].#subform[0].ksr_UndersokningPatient[0]"))
                          .dateFieldId(new PdfFieldId("form1[0].#subform[0].flt_datUl_1[0]"))
                          .build(),
                      new FieldId("journaluppgifter"),
                      PdfConfigurationDateCheckbox.builder()
                          .checkboxFieldId(
                              new PdfFieldId("form1[0].#subform[0].ksr_Journaluppgifter[0]"))
                          .dateFieldId(new PdfFieldId("form1[0].#subform[0].flt_datUl_2[0]"))
                          .build(),
                      new FieldId("annat"),
                      PdfConfigurationDateCheckbox.builder()
                          .checkboxFieldId(new PdfFieldId("form1[0].#subform[0].ksr_Annat[0]"))
                          .dateFieldId(new PdfFieldId("form1[0].#subform[0].flt_datUl_3[0]"))
                          .build()))
              .build();

      final var element = questionUtlatandeBaseratPa(new FK3226V1PrintTagProvider());

      assertEquals(expected, element.pdfConfiguration());
    }
  }

  @Nested
  class V1_1 {

    @Test
    void shallIncludePdfConfiguration() {
      final var expected =
          PdfConfigurationDateList.builder()
              .dateCheckboxes(
                  Map.of(
                      new FieldId("undersokningAvPatienten"),
                      PdfConfigurationDateCheckbox.builder()
                          .checkboxFieldId(
                              new PdfFieldId("form1[0].#subform[0].ksr_UndersokningPatient[0]"))
                          .dateFieldId(
                              new PdfFieldId("form1[0].#subform[0].flt_datumUnsersokning[0]"))
                          .build(),
                      new FieldId("journaluppgifter"),
                      PdfConfigurationDateCheckbox.builder()
                          .checkboxFieldId(
                              new PdfFieldId("form1[0].#subform[0].ksr_Journaluppgifter[0]"))
                          .dateFieldId(
                              new PdfFieldId("form1[0].#subform[0].flt_datumJournaluppgifter[0]"))
                          .build(),
                      new FieldId("annat"),
                      PdfConfigurationDateCheckbox.builder()
                          .checkboxFieldId(new PdfFieldId("form1[0].#subform[0].ksr_Annat[0]"))
                          .dateFieldId(new PdfFieldId("form1[0].#subform[0].flt_datumAnnat[0]"))
                          .build()))
              .build();

      final var element = questionUtlatandeBaseratPa(new FK3226V1_1PrintTagProvider());

      assertEquals(expected, element.pdfConfiguration());
    }
  }
}
