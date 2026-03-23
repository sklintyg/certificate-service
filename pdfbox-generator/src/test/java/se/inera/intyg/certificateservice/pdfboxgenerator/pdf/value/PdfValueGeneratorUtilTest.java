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

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.text.TextSplitRenderSpec;

class PdfValueGeneratorUtilTest {

  @Test
  void shouldSplitTextUsingLongInformationTextIfLimitIsOver25() {
    final var value = "This is the value if was planning on testing for splitting text.";
    final var expected =
        List.of(
            "This is ... Se fortsättningsblad!",
            "... the value if was planning on testing for splitting text.");

    assertEquals(
        expected,
        PdfValueGeneratorUtil.splitByLimit(
            TextSplitRenderSpec.builder().limit(35).fieldText(value).build()));
  }

  @Test
  void shouldSplitTextUsingShortInformationTextIfLimitIsUnder25() {
    final var value = "This is the value if was planning on testing for splitting text.";
    final var expected =
        List.of("This is ...", "... the value if was planning on testing for splitting text.");

    assertEquals(
        expected,
        PdfValueGeneratorUtil.splitByLimit(
            TextSplitRenderSpec.builder().limit(10).fieldText(value).build()));
  }

  @Test
  void shouldSplitTextUsingMessageIfProvided() {
    final var value = "This is the value if was planning on testing for splitting text.";
    final var message = "xxx";
    final var expected =
        List.of("This is xxx", "... the value if was planning on testing for splitting text.");

    assertEquals(
        expected,
        PdfValueGeneratorUtil.splitByLimit(
            TextSplitRenderSpec.builder()
                .limit(10)
                .fieldText(value)
                .informationMessage(message)
                .build()));
  }
}
