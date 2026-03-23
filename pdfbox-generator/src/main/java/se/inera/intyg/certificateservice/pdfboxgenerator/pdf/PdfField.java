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

import lombok.Builder;
import lombok.Value;
import lombok.With;
import org.apache.pdfbox.pdmodel.font.PDFont;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.text.TextUtil;

@Value
@Builder
public class PdfField {

  String id;
  @With String value;
  @Builder.Default Boolean append = false;
  @Builder.Default Boolean patientField = false;
  @Builder.Default Boolean unitField = false;
  @With String appearance;
  @Builder.Default Integer offset = 0;

  public String sanitizedValue(PDFont font) {
    if (this.value == null || this.value.isEmpty()) {
      return "";
    }

    return TextUtil.sanitizeText(value, font);
  }

  public String normalizedValue(PDFont font) {
    if (this.value == null || this.value.isEmpty()) {
      return "";
    }

    return TextUtil.normalizePrintableCharacters(value, font);
  }

  public boolean isPatientField() {
    return Boolean.TRUE.equals(patientField);
  }
}
