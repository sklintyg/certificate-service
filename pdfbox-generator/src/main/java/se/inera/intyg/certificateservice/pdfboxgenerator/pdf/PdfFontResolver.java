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

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.factory.TextFieldAppearanceFactory;

@RequiredArgsConstructor
public class PdfFontResolver {

  private final PDAcroForm acroForm;
  private final TextFieldAppearanceFactory textFieldAppearanceFactory;

  public PDFont resolveFont(PdfField field) {

    return textFieldAppearanceFactory
        .create(acroForm.getField(field.getId()))
        .map(appearance -> appearance.getFont(acroForm.getDefaultResources()))
        .orElseGet(this::extractFallbackFont);
  }

  private PDFont extractFallbackFont() {
    try {
      var resources = acroForm.getDefaultResources();
      var fontName = resources.getFontNames().iterator().next();
      return resources.getFont(fontName);
    } catch (IOException e) {
      throw new IllegalStateException("Could not resolve fallback font", e);
    }
  }
}
