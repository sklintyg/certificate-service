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
import static org.mockito.Mockito.mock;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.junit.jupiter.api.Test;

class PdfFieldSanitizerTest {

  @Test
  void shouldSanitizePdfFieldsCorrectly() {
    var sanitizer = new PdfFieldSanitizer();

    final var originalField = PdfField.builder().value("text\u2013with\u2014dashes").build();

    final var sanatizedField = PdfField.builder().value("text-with-dashes").build();

    PDFont mockFont = mock(PDFont.class);

    final var actual = sanitizer.sanitize(originalField, mockFont);

    assertEquals(sanatizedField, actual);
  }
}
