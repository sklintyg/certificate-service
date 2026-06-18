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
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueIcf;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueInteger;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;

@ExtendWith(MockitoExtension.class)
class PdfConfigurationTextTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final PdfFieldId PDF_FIELD_ID = new PdfFieldId("pdfFieldId");
  private static final PdfFieldId OVERFLOW_FIELD_ID = new PdfFieldId("overflowFieldId");
  private static final Integer MAX_LENGTH = 100;
  private static final Integer OFFSET = 5;
  private static final String TEXT = "some text";
  private static final String OVERFLOW_LABEL = "Question label";

  @Mock private ElementSpecification elementSpec;
  @Mock private Certificate certificate;
  @Mock private ElementData elementData;
  @Mock private ElementConfiguration elementConfiguration;

  private final CustomPdfSpecification pdfSpecification =
      CustomPdfSpecification.builder().overflowFieldId(OVERFLOW_FIELD_ID).build();

  private final PdfConfigurationText pdfConfiguration =
      PdfConfigurationText.builder()
          .pdfFieldId(PDF_FIELD_ID)
          .maxLength(MAX_LENGTH)
          .overflowSheetFieldId(OVERFLOW_FIELD_ID)
          .offset(OFFSET)
          .build();

  @ParameterizedTest
  @MethodSource("textElementValues")
  void shallReturnPdfFieldWhenElementValueContainsText(
      ElementValue elementValue, String expectedValue) {
    givenCertificateValue(elementValue);

    assertEquals(
        List.of(expectedPdfField(expectedValue)),
        pdfConfiguration.toPdfFields(elementSpec, certificate, pdfSpecification).toList());
  }

  @Test
  void shallReturnEmptyListWhenElementDataIsMissing() {
    when(elementSpec.id()).thenReturn(ELEMENT_ID);
    when(certificate.getElementDataById(ELEMENT_ID)).thenReturn(Optional.empty());

    assertEquals(
        List.of(),
        pdfConfiguration.toPdfFields(elementSpec, certificate, pdfSpecification).toList());
  }

  @Test
  void shallReturnEmptyListWhenElementDataValueIsNull() {
    when(elementSpec.id()).thenReturn(ELEMENT_ID);
    when(certificate.getElementDataById(ELEMENT_ID)).thenReturn(Optional.of(elementData));
    when(elementData.value()).thenReturn(null);

    assertEquals(
        List.of(),
        pdfConfiguration.toPdfFields(elementSpec, certificate, pdfSpecification).toList());
  }

  private static Stream<Arguments> textElementValues() {
    return Stream.of(
        Arguments.of(ElementValueText.builder().text(TEXT).build(), TEXT),
        Arguments.of(ElementValueInteger.builder().value(5).build(), "5"),
        Arguments.of(ElementValueIcf.builder().text(TEXT).icfCodes(List.of()).build(), TEXT),
        Arguments.of(
            ElementValueIcf.builder().text(TEXT).icfCodes(List.of("A", "B")).build(),
            "A - B some text"));
  }

  private void givenCertificateValue(ElementValue elementValue) {
    when(elementSpec.id()).thenReturn(ELEMENT_ID);
    when(elementSpec.configuration()).thenReturn(elementConfiguration);
    when(elementConfiguration.name()).thenReturn(OVERFLOW_LABEL);
    when(certificate.getElementDataById(ELEMENT_ID)).thenReturn(Optional.of(elementData));
    when(elementData.value()).thenReturn(elementValue);
  }

  private static PdfField expectedPdfField(String value) {
    return PdfField.builder()
        .fieldId(PDF_FIELD_ID)
        .value(value)
        .offset(OFFSET)
        .maxLength(MAX_LENGTH)
        .shouldRemoveLineBreaks(true)
        .overflowConfig(
            OverflowConfig.builder()
                .overflowFieldId(OVERFLOW_FIELD_ID)
                .overflowLabel(OVERFLOW_LABEL)
                .build())
        .build();
  }
}
