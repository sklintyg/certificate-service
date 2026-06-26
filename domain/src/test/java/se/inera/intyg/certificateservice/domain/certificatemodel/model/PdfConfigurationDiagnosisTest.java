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
package se.inera.intyg.certificateservice.domain.certificatemodel.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosis;

class PdfConfigurationDiagnosisTest {

  private static final FieldId DIAGNOSIS_ID = new FieldId("diagnos1");
  private static final PdfFieldId NAME_FIELD = new PdfFieldId("form.diag.name[0]");
  private static final PdfFieldId CODE_1 = new PdfFieldId("form.diag.code[0]");
  private static final PdfFieldId CODE_2 = new PdfFieldId("form.diag.code[1]");
  private static final PdfFieldId CODE_3 = new PdfFieldId("form.diag.code[2]");
  private static final PdfFieldId OVERFLOW = new PdfFieldId("form.diag.overflowConfig");
  private static final String APPEARANCE = "/ArialMT 9.00 Tf 0 g";

  @Test
  void shallReturnNameFieldWithAppearanceAndOneFieldPerCodeCharacter() {
    final var config =
        PdfConfigurationDiagnosis.builder()
            .pdfNameFieldId(NAME_FIELD)
            .pdfCodeFieldIds(List.of(CODE_1, CODE_2, CODE_3))
            .build();
    final var diagnosis =
        ElementValueDiagnosis.builder()
            .id(DIAGNOSIS_ID)
            .terminology("ICD-10")
            .code("A12")
            .description("Akut bronkit")
            .build();
    final var overflowConfig =
        OverflowConfig.builder().overflowFieldId(OVERFLOW).overflowLabel("name").build();

    final var expected =
        List.of(
            PdfField.builder()
                .fieldId(NAME_FIELD)
                .value("Akut bronkit")
                .appearance(APPEARANCE)
                .shouldRemoveLineBreaks(true)
                .overflowConfig(overflowConfig)
                .build(),
            PdfField.builder().fieldId(CODE_1).value("A").build(),
            PdfField.builder().fieldId(CODE_2).value("1").build(),
            PdfField.builder().fieldId(CODE_3).value("2").build());

    assertEquals(expected, config.toPdfFields(diagnosis, APPEARANCE, 172, overflowConfig).toList());
  }

  @Test
  void shallOmitCodeFieldsWhenCodeIsNull() {
    final var config =
        PdfConfigurationDiagnosis.builder()
            .pdfNameFieldId(NAME_FIELD)
            .pdfCodeFieldIds(List.of(CODE_1, CODE_2, CODE_3))
            .build();
    final var diagnosis =
        ElementValueDiagnosis.builder()
            .id(DIAGNOSIS_ID)
            .terminology("ICD-10")
            .code(null)
            .description("Endast text")
            .build();
    final var overflowConfig =
        OverflowConfig.builder().overflowFieldId(OVERFLOW).overflowLabel("name").build();

    final var expected =
        List.of(
            PdfField.builder()
                .fieldId(NAME_FIELD)
                .value("Endast text")
                .appearance(APPEARANCE)
                .shouldRemoveLineBreaks(true)
                .overflowConfig(overflowConfig)
                .build());

    assertEquals(expected, config.toPdfFields(diagnosis, APPEARANCE, 172, overflowConfig).toList());
  }

  @Test
  void shallBuildDescriptionValueWithinMaxLength() {
    final var config =
        PdfConfigurationDiagnosis.builder()
            .pdfNameFieldId(NAME_FIELD)
            .pdfCodeFieldIds(List.of(CODE_1, CODE_2, CODE_3))
            .build();
    final var diagnosis =
        ElementValueDiagnosis.builder()
            .id(DIAGNOSIS_ID)
            .terminology("ICD-10")
            .code("A12")
            .description("En beskrivning som är för lång")
            .build();
    final var overflowConfig =
        OverflowConfig.builder().overflowFieldId(OVERFLOW).overflowLabel("name").build();

    final var expected =
        List.of(
            PdfField.builder()
                .fieldId(NAME_FIELD)
                .value("En beskrivning som är för l...")
                .appearance(APPEARANCE)
                .shouldRemoveLineBreaks(true)
                .overflowConfig(overflowConfig)
                .build(),
            PdfField.builder().fieldId(CODE_1).value("A").build(),
            PdfField.builder().fieldId(CODE_2).value("1").build(),
            PdfField.builder().fieldId(CODE_3).value("2").build());

    assertEquals(
        expected,
        config
            .toPdfFields(
                diagnosis, APPEARANCE, "En beskrivning som är för lång".length(), overflowConfig)
            .toList());
  }
}
