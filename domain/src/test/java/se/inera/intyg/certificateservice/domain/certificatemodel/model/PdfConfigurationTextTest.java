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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueIcf;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueInteger;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;

@ExtendWith(MockitoExtension.class)
class PdfConfigurationTextTest {
  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final PdfFieldId PDF_FIELD_ID = new PdfFieldId("pdfFieldId");
  private static final PdfFieldId OVERFLOW_SHEET_FIELD_ID = new PdfFieldId("overflowSheetFieldId");
  private static final Integer MAX_LENGTH = 100;
  private static final Integer OFFSET = 5;
  private static final String TEXT = "some text";

  @Mock private final ElementSpecification elementSpec = mock(ElementSpecification.class);
  @Mock private final Certificate certificate = mock(Certificate.class);
  @Mock private final ElementData elementData = mock(ElementData.class);

  private final PdfConfigurationText pdfConfiguration =
      PdfConfigurationText.builder()
          .pdfFieldId(PDF_FIELD_ID)
          .maxLength(MAX_LENGTH)
          .overflowSheetFieldId(OVERFLOW_SHEET_FIELD_ID)
          .offset(OFFSET)
          .build();

  @Test
  void shallReturnPdfFieldWhenElementValueTextWithText() {
    final var elementValueText = mock(ElementValueText.class);

    when(elementSpec.id()).thenReturn(ELEMENT_ID);
    when(certificate.getElementDataById(ELEMENT_ID)).thenReturn(Optional.of(elementData));
    when(elementData.value()).thenReturn(elementValueText);
    when(elementValueText.text()).thenReturn(TEXT);

    final var expected =
        List.of(PdfField.builder().fieldId(PDF_FIELD_ID).value(TEXT).offset(OFFSET).build());

    assertEquals(expected, pdfConfiguration.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallReturnPdfFieldWhenElementValueIcfWithText() {
    final var elementValueIcf = mock(ElementValueIcf.class);

    when(elementSpec.id()).thenReturn(ELEMENT_ID);
    when(certificate.getElementDataById(ELEMENT_ID)).thenReturn(Optional.of(elementData));
    when(elementData.value()).thenReturn(elementValueIcf);
    when(elementValueIcf.text()).thenReturn(TEXT);

    final var expected =
        List.of(PdfField.builder().fieldId(PDF_FIELD_ID).value(TEXT).offset(OFFSET).build());

    assertEquals(expected, pdfConfiguration.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallReturnPdfFieldWhenElementValueIntegerWithValue() {
    final var elementValueInteger = mock(ElementValueInteger.class);

    when(elementSpec.id()).thenReturn(ELEMENT_ID);
    when(certificate.getElementDataById(ELEMENT_ID)).thenReturn(Optional.of(elementData));
    when(elementData.value()).thenReturn(elementValueInteger);
    when(elementValueInteger.value()).thenReturn(5);

    final var expected =
        List.of(PdfField.builder().fieldId(PDF_FIELD_ID).value("5").offset(OFFSET).build());

    assertEquals(expected, pdfConfiguration.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallReturnEmptyListWhenElementValueIsNull() {
    final var elementValueText = mock(ElementValueText.class);

    when(elementSpec.id()).thenReturn(ELEMENT_ID);
    when(certificate.getElementDataById(ELEMENT_ID)).thenReturn(Optional.of(elementData));
    when(elementData.value()).thenReturn(elementValueText);
    when(elementValueText.text()).thenReturn(null);

    assertEquals(List.of(), pdfConfiguration.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallReturnEmptyListWhenElementDataIsMissing() {
    when(elementSpec.id()).thenReturn(ELEMENT_ID);
    when(certificate.getElementDataById(ELEMENT_ID)).thenReturn(Optional.empty());

    assertEquals(List.of(), pdfConfiguration.toPdfFields(elementSpec, certificate).toList());
  }
}
