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
package se.inera.intyg.certificateservice.certificate.custom;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7210CertificateBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModel.FK7210_CERTIFICATE_MODEL;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModel.fk7210certificateModelBuilder;

import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.certificate.custom.converter.CustomPrintRequestConverter;
import se.inera.intyg.certificateservice.certificate.custom.integration.PrintCustomCertificateFromCertificatePrintService;
import se.inera.intyg.certificateservice.certificate.general.dto.PrintCertificateResponseDTO;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.GeneralPdfSpecification;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210TemplatePathProvider;

@ExtendWith(MockitoExtension.class)
class CustomPdfGeneratorTest {

  private static final byte[] RAW_PDF_BYTES = new byte[] {10, 20, 30};
  private static final byte[] ENCODED_PDF_BYTES = Base64.getEncoder().encode(RAW_PDF_BYTES);

  @Mock private PdfTemplateLoader pdfTemplateLoader;
  @Mock private CustomPrintRequestConverter customPrintRequestConverter;

  @Mock
  private PrintCustomCertificateFromCertificatePrintService
      printCustomCertificateFromCertificatePrintService;

  private CustomPdfGenerator customPdfGenerator;

  @BeforeEach
  void setUp() {
    customPdfGenerator =
        new CustomPdfGenerator(
            customPrintRequestConverter,
            printCustomCertificateFromCertificatePrintService,
            pdfTemplateLoader);
  }

  @Nested
  class InvalidPdfSpecification {

    @Test
    void shallThrowWhenPdfSpecificationIsNotCustomPdfSpecification() {
      final var certificate =
          fk7210CertificateBuilder()
              .certificateModel(
                  fk7210certificateModelBuilder()
                      .pdfSpecification(GeneralPdfSpecification.builder().build())
                      .build())
              .build();
      final var options =
          PdfGeneratorOptions.builder()
              .additionalInfoText(FK7210_CERTIFICATE_MODEL.name())
              .citizenFormat(false)
              .hiddenElements(List.of())
              .build();

      assertThrows(
          IllegalArgumentException.class, () -> customPdfGenerator.generate(certificate, options));
    }
  }

  @Nested
  class ValidPdfSpecification {

    private PdfGeneratorOptions options;

    @BeforeEach
    void setUp() {
      options =
          PdfGeneratorOptions.builder()
              .additionalInfoText(FK7210_CERTIFICATE_MODEL.name())
              .citizenFormat(false)
              .hiddenElements(List.of())
              .build();

      when(pdfTemplateLoader.load(anyString())).thenReturn(RAW_PDF_BYTES);
      when(customPrintRequestConverter.convert(any(), any(), any(), any(), anyString()))
          .thenReturn(null);
      when(printCustomCertificateFromCertificatePrintService.print(any()))
          .thenReturn(PrintCertificateResponseDTO.builder().pdfData(ENCODED_PDF_BYTES).build());
    }

    @Test
    void shallReturnDecodedPdfBytes() {
      final var certificate =
          fk7210CertificateBuilder()
              .certificateModel(
                  fk7210certificateModelBuilder()
                      .pdfSpecification(
                          CustomPdfSpecification.builder()
                              .pdfTemplatePathProvider(new FK7210TemplatePathProvider())
                              .build())
                      .build())
              .build();

      final var pdf = customPdfGenerator.generate(certificate, options);

      assertArrayEquals(RAW_PDF_BYTES, pdf.pdfData());
    }

    @Test
    void shallReturnCorrectFileName() {
      final var certificate =
          fk7210CertificateBuilder()
              .certificateModel(
                  fk7210certificateModelBuilder()
                      .pdfSpecification(
                          CustomPdfSpecification.builder()
                              .pdfTemplatePathProvider(new FK7210TemplatePathProvider())
                              .build())
                      .build())
              .build();

      final var pdf = customPdfGenerator.generate(certificate, options);

      assertTrue(pdf.fileName().startsWith(FK7210_CERTIFICATE_MODEL.fileName()));
    }
  }
}
