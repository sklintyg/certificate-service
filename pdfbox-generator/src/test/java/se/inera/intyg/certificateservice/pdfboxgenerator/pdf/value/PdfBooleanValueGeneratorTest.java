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
package se.inera.intyg.certificateservice.pdfboxgenerator.pdf.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfRadioOption;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

class PdfBooleanValueGeneratorTest {

  private static final FieldId FIELD_ID = new FieldId("52.5");
  private static final String YES_PDF_FIELD_ID = "form1[0].#subform[1].ksr_Ja[0]";
  private static final String NO_PDF_FIELD_ID = "form1[0].#subform[1].ksr_Nej[0]";
  private static final String CHECKBOX_VALUE = "1";

  private static final PdfBooleanValueGenerator pdfBooleanValueGenerator =
      new PdfBooleanValueGenerator();

  @Test
  void shouldReturnType() {
    assertEquals(ElementValueBoolean.class, pdfBooleanValueGenerator.getType());
  }

  @Nested
  class PdfConfigurationBooleanTest {

    @Test
    void shouldSetValueIfElementDataWithBooleanValueTrue() {
      final var expected =
          List.of(PdfField.builder().id(YES_PDF_FIELD_ID).value(CHECKBOX_VALUE).build());

      final var elementSpecification =
          ElementSpecification.builder()
              .pdfConfiguration(
                  PdfConfigurationBoolean.builder()
                      .checkboxTrue(new PdfFieldId(YES_PDF_FIELD_ID))
                      .checkboxFalse(new PdfFieldId(NO_PDF_FIELD_ID))
                      .build())
              .build();

      final var elementValue =
          ElementValueBoolean.builder().booleanId(FIELD_ID).value(true).build();

      final var result = pdfBooleanValueGenerator.generate(elementSpecification, elementValue);

      assertEquals(expected, result);
    }

    @Test
    void shouldSetValueIfElementDataWithBooleanValueFalse() {
      final var expected =
          List.of(PdfField.builder().id(NO_PDF_FIELD_ID).value(CHECKBOX_VALUE).build());

      final var elementSpecification =
          ElementSpecification.builder()
              .pdfConfiguration(
                  PdfConfigurationBoolean.builder()
                      .checkboxTrue(new PdfFieldId(YES_PDF_FIELD_ID))
                      .checkboxFalse(new PdfFieldId(NO_PDF_FIELD_ID))
                      .build())
              .build();

      final var elementValue =
          ElementValueBoolean.builder().booleanId(FIELD_ID).value(false).build();

      final var result = pdfBooleanValueGenerator.generate(elementSpecification, elementValue);

      assertEquals(expected, result);
    }

    @Test
    void shouldReturnEmptyIfValueIsNull() {
      final var elementSpecification =
          ElementSpecification.builder()
              .pdfConfiguration(
                  PdfConfigurationBoolean.builder()
                      .checkboxTrue(new PdfFieldId(YES_PDF_FIELD_ID))
                      .checkboxFalse(new PdfFieldId(NO_PDF_FIELD_ID))
                      .build())
              .build();

      final var elementValue = ElementValueBoolean.builder().booleanId(FIELD_ID).build();

      final var result = pdfBooleanValueGenerator.generate(elementSpecification, elementValue);

      assertEquals(Collections.emptyList(), result);
    }

    @Test
    void shouldHandleValueFalseAndCheckboxFalseNull() {
      final var elementSpecification =
          ElementSpecification.builder()
              .pdfConfiguration(
                  PdfConfigurationBoolean.builder()
                      .checkboxTrue(new PdfFieldId(YES_PDF_FIELD_ID))
                      .build())
              .build();

      final var elementValue =
          ElementValueBoolean.builder().booleanId(FIELD_ID).value(false).build();

      final var result = pdfBooleanValueGenerator.generate(elementSpecification, elementValue);

      assertEquals(Collections.emptyList(), result);
    }
  }

  @Nested
  class PdfConfigurationRadioBooleanTest {

    private static final String RADIO_FIELD_ID = "form1[0].#subform[1].radioField[0]";
    private static final String OPTION_TRUE_VALUE = "Yes";
    private static final String OPTION_FALSE_VALUE = "No";

    @Test
    void shouldSetValueIfElementDataWithBooleanValueTrue() {
      final var expected =
          List.of(PdfField.builder().id(RADIO_FIELD_ID).value(OPTION_TRUE_VALUE).build());

      final var elementSpecification =
          ElementSpecification.builder()
              .pdfConfiguration(
                  PdfConfigurationRadioBoolean.builder()
                      .pdfFieldId(new PdfFieldId(RADIO_FIELD_ID))
                      .optionTrue(new PdfRadioOption(OPTION_TRUE_VALUE))
                      .optionFalse(new PdfRadioOption(OPTION_FALSE_VALUE))
                      .build())
              .build();

      final var elementValue =
          ElementValueBoolean.builder().booleanId(FIELD_ID).value(true).build();

      final var result = pdfBooleanValueGenerator.generate(elementSpecification, elementValue);

      assertEquals(expected, result);
    }

    @Test
    void shouldSetValueIfElementDataWithBooleanValueFalse() {
      final var expected =
          List.of(PdfField.builder().id(RADIO_FIELD_ID).value(OPTION_FALSE_VALUE).build());

      final var elementSpecification =
          ElementSpecification.builder()
              .pdfConfiguration(
                  PdfConfigurationRadioBoolean.builder()
                      .pdfFieldId(new PdfFieldId(RADIO_FIELD_ID))
                      .optionTrue(new PdfRadioOption(OPTION_TRUE_VALUE))
                      .optionFalse(new PdfRadioOption(OPTION_FALSE_VALUE))
                      .build())
              .build();

      final var elementValue =
          ElementValueBoolean.builder().booleanId(FIELD_ID).value(false).build();

      final var result = pdfBooleanValueGenerator.generate(elementSpecification, elementValue);

      assertEquals(expected, result);
    }

    @Test
    void shouldReturnEmptyIfValueIsNull() {
      final var elementSpecification =
          ElementSpecification.builder()
              .pdfConfiguration(
                  PdfConfigurationRadioBoolean.builder()
                      .pdfFieldId(new PdfFieldId(RADIO_FIELD_ID))
                      .optionTrue(new PdfRadioOption(OPTION_TRUE_VALUE))
                      .optionFalse(new PdfRadioOption(OPTION_FALSE_VALUE))
                      .build())
              .build();

      final var elementValue = ElementValueBoolean.builder().booleanId(FIELD_ID).build();

      final var result = pdfBooleanValueGenerator.generate(elementSpecification, elementValue);

      assertEquals(Collections.emptyList(), result);
    }
  }
}
