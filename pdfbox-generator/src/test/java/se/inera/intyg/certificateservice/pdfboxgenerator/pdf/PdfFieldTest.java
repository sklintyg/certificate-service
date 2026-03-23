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
package se.inera.intyg.certificateservice.pdfboxgenerator.pdf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PdfFieldTest {

  @Nested
  class SanatizedValue {

    PDFont font;

    @BeforeEach
    void setup() {
      font = new PDType1Font(FontName.HELVETICA);
    }

    @Test
    void shouldReturnEmptySanitizedValueWhenValueIsNull() {
      final var field = PdfField.builder().id("Field1").value(null).build();

      assertEquals("", field.sanitizedValue(font));
    }

    @Test
    void shouldSanitizeValue() {
      final var field = PdfField.builder().id("Field1").value("Line1\u20A5Line2").build();

      assertEquals("Line1 Line2", field.sanitizedValue(font));
    }

    @Test
    void shouldReturnEmptySanitizedValueWhenValueIsEmpty() {
      final var field = PdfField.builder().id("Field1").value("").build();

      assertEquals("", field.sanitizedValue(font));
    }
  }

  @Nested
  class IsPatientField {

    @Test
    void shouldReturnTrueWhenPatientFieldIsTrue() {
      final var field = PdfField.builder().id("Field1").patientField(true).build();

      assertTrue(
          field.isPatientField(),
          "Expected isPatientField to return true when patientField is true");
    }

    @Test
    void shouldReturnFalseWhenPatientFieldIsFalse() {
      final var field = PdfField.builder().id("Field1").patientField(false).build();

      assertFalse(
          field.isPatientField(),
          "Expected isPatientField to return false when patientField is false");
    }

    @Test
    void shouldReturnFalseWhenPatientFieldIsNull() {
      final var field = PdfField.builder().id("Field1").patientField(null).build();

      assertFalse(
          field.isPatientField(),
          "Expected isPatientField to return false when patientField is null");
    }
  }
}
