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

import java.util.List;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.text.TextSplitRenderSpec;

public class PdfValueGeneratorUtil {

  private static final String OVERFLOW_MESSAGE = "... Se fortsättningsblad!";
  private static final String SMALL_OVERFLOW_MESSAGE = "...";

  private PdfValueGeneratorUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static List<String> splitByLimit(TextSplitRenderSpec textSplitRenderSpec) {
    final var informationText =
        textSplitRenderSpec.getInformationMessage() != null
            ? textSplitRenderSpec.getInformationMessage()
            : getInformationText(textSplitRenderSpec.getLimit());

    final var updatedLimit = textSplitRenderSpec.getLimit() - informationText.length();
    final var noLineBreak =
        textSplitRenderSpec.isShouldRemoveLineBreaks()
            ? textSplitRenderSpec.getFieldText().replace("\n", "")
            : textSplitRenderSpec.getFieldText();
    final var words = noLineBreak.split(" ");

    final var firstPart = new StringBuilder();
    final var secondPart = new StringBuilder();
    boolean limitReached = false;

    for (String word : words) {
      if (!limitReached && firstPart.length() + word.length() + 1 <= updatedLimit) {
        if (!firstPart.isEmpty()) {
          firstPart.append(" ");
        }
        firstPart.append(word);
      } else {
        if (!secondPart.isEmpty()) {
          secondPart.append(" ");
        }
        secondPart.append(word);
        limitReached = true;
      }
    }
    return List.of(firstPart + " " + informationText, SMALL_OVERFLOW_MESSAGE + " " + secondPart);
  }

  private static String getInformationText(Integer limit) {
    return limit <= OVERFLOW_MESSAGE.length() ? SMALL_OVERFLOW_MESSAGE : OVERFLOW_MESSAGE;
  }
}
