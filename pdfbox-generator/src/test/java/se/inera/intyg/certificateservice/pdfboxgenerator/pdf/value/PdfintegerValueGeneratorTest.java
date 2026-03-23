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
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueInteger;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

class PdfintegerValueGeneratorTest {

  private static final String FIELD_ID = "form1[0].#subform[0].flt_txtInteger[0]";
  private static final Integer VALUE = 42;

  private static final PdfintegerValueGenerator pdfintegerValueGenerator =
      new PdfintegerValueGenerator();

  @Test
  void shouldReturnType() {
    assertEquals(ElementValueInteger.class, pdfintegerValueGenerator.getType());
  }

  @Test
  void shouldSetValueIfElementDataWithIntegerValue() {
    final var expected =
        List.of(PdfField.builder().id(FIELD_ID).value(VALUE.toString()).offset(10).build());

    final var elementSpecification =
        ElementSpecification.builder()
            .pdfConfiguration(
                PdfConfigurationText.builder()
                    .pdfFieldId(new PdfFieldId(FIELD_ID))
                    .offset(10)
                    .build())
            .build();

    final var elementValue = ElementValueInteger.builder().value(VALUE).build();

    final var result = pdfintegerValueGenerator.generate(elementSpecification, elementValue);

    assertEquals(expected, result);
  }

  @Test
  void shouldReturnEmptyListIfElementDataWithoutIntegerValue() {
    final var elementSpecification =
        ElementSpecification.builder()
            .pdfConfiguration(
                PdfConfigurationText.builder().pdfFieldId(new PdfFieldId(FIELD_ID)).build())
            .build();

    final var elementValue = ElementValueInteger.builder().build();

    final var result = pdfintegerValueGenerator.generate(elementSpecification, elementValue);

    assertEquals(Collections.emptyList(), result);
  }
}
