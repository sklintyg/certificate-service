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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7210CertificateBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_PDF_FODELSEDATUM_FIELD_ID;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

@ExtendWith(MockitoExtension.class)
class PdfElementValueGeneratorTest {

  private static final ElementId ELEMENT_ID = new ElementId("1");

  private final MedicalCertificate.MedicalCertificateBuilder certificateBuilder =
      fk7210CertificateBuilder();
  private final ElementSpecification elementSpecification =
      ElementSpecification.builder().id(ELEMENT_ID).build();

  @Mock private PdfDateValueGenerator pdfDateValueGenerator;
  @Mock private PdfTextValueGenerator pdfTextValueGenerator;
  @Mock private CertificateModel certificateModel;

  private PdfElementValueGenerator pdfElementValueGenerator;

  @BeforeEach
  void setUp() {
    doReturn(ElementValueDate.class).when(pdfDateValueGenerator).getType();
    doReturn(ElementValueText.class).when(pdfTextValueGenerator).getType();

    pdfElementValueGenerator =
        new PdfElementValueGenerator(List.of(pdfDateValueGenerator, pdfTextValueGenerator));

    certificateBuilder.certificateModel(certificateModel);
  }

  @Test
  void shouldReturnListOfPdfFields() {
    final var expected =
        List.of(
            PdfField.builder()
                .id(FK7210_PDF_FODELSEDATUM_FIELD_ID.id())
                .value(LocalDate.now().toString())
                .build());

    final var elementValue = ElementValueDate.builder().build();
    final var elementData = ElementData.builder().id(ELEMENT_ID).value(elementValue).build();

    final var certificate = certificateBuilder.elementData(List.of(elementData)).build();

    doReturn(List.of(elementSpecification)).when(certificateModel).elementSpecifications();

    doReturn(expected)
        .when(pdfDateValueGenerator)
        .generate(elementSpecification, ElementValueDate.builder().build());

    final var response = pdfElementValueGenerator.generate(certificate);

    assertEquals(expected, response);
  }

  @Test
  void shouldThrowIllegalStateExceptionIfUnableToFindGeneratorForType() {
    final var elementValue = ElementValueBoolean.builder().build();

    final var certificate =
        certificateBuilder
            .elementData(List.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
            .build();

    doReturn(List.of(elementSpecification)).when(certificateModel).elementSpecifications();

    assertThrows(IllegalStateException.class, () -> pdfElementValueGenerator.generate(certificate));
  }
}
