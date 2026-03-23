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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDComboBox;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.apache.pdfbox.pdmodel.interactive.form.PDVariableText;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.factory.TextFieldAppearanceFactory;

@ExtendWith(MockitoExtension.class)
class PdfFontResolverTest {

  @Mock private PDAcroForm acroForm;

  @Mock private TextFieldAppearanceFactory textFieldAppearanceFactory;

  @Mock private TextFieldAppearance textFieldAppearance;

  @InjectMocks private PdfFontResolver pdfFontResolver;

  @Test
  void shouldResolveFontFromTextField() {
    final var pdfField = PdfField.builder().id("fieldId").build();
    final var textField = mock(PDTextField.class);
    final var font = mock(PDFont.class);

    when(acroForm.getField("fieldId")).thenReturn(textField);
    when(textFieldAppearanceFactory.create(textField)).thenReturn(Optional.of(textFieldAppearance));
    when(textFieldAppearance.getFont(any())).thenReturn(font);

    final var actual = pdfFontResolver.resolveFont(pdfField);

    assertEquals(font, actual);
  }

  @Test
  void shouldResolveFontFromVariableText() {
    final var pdfField = PdfField.builder().id("fieldId").build();
    final var textField = mock(PDVariableText.class);
    final var font = mock(PDFont.class);

    when(acroForm.getField("fieldId")).thenReturn(textField);
    when(textFieldAppearanceFactory.create(textField)).thenReturn(Optional.of(textFieldAppearance));
    when(textFieldAppearance.getFont(any())).thenReturn(font);

    final var actual = pdfFontResolver.resolveFont(pdfField);

    assertEquals(font, actual);
  }

  @Test
  void shouldResolveFallbackFontWhenNoTextFieldAppearance() throws IOException {
    final var pdfField = PdfField.builder().id("fieldId").build();
    final var comboBox = mock(PDComboBox.class);
    final var font = mock(PDFont.class);
    final var resources = mock(PDResources.class);

    when(acroForm.getField("fieldId")).thenReturn(comboBox);
    when(textFieldAppearanceFactory.create(comboBox)).thenReturn(Optional.empty());
    when(acroForm.getDefaultResources()).thenReturn(resources);
    when(resources.getFontNames()).thenReturn(Set.of(COSName.getPDFName("F1")));
    when(resources.getFont(COSName.getPDFName("F1"))).thenReturn(font);

    final var actual = pdfFontResolver.resolveFont(pdfField);

    assertEquals(font, actual);
  }
}
