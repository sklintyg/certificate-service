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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.certificate.custom.converter.CustomPrintRequestConverter;
import se.inera.intyg.certificateservice.certificate.custom.integration.PrintCustomCertificateFromCertificatePrintService;
import se.inera.intyg.certificateservice.certificate.general.dto.PrintCertificateResponseDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Sent;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.GeneralPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndex;

@ExtendWith(MockitoExtension.class)
class CustomPdfGeneratorTest {

  private static final String ADDITIONAL_INFO_TEXT = "Webcert 2.0";
  private static final String TEMPLATE_WITH_ADDRESS = "fk7210/pdf/fk7210_v1.pdf";
  private static final String TEMPLATE_NO_ADDRESS = "fk7210/pdf/fk7210_v1_no_address.pdf";

  @Mock private CustomPrintRequestConverter customPrintRequestConverter;

  @Mock
  private PrintCustomCertificateFromCertificatePrintService
      printCustomCertificateFromCertificatePrintService;

  @Mock private Certificate certificate;
  @Mock private CertificateModel certificateModel;

  @Nested
  class WhenInvalidSpec {

    @Test
    void shallThrowWhenPdfSpecificationIsNotCustomPdfSpecification() {
      when(certificate.certificateModel()).thenReturn(certificateModel);
      when(certificateModel.pdfSpecification())
          .thenReturn(GeneralPdfSpecification.builder().build());

      final var generator =
          new CustomPdfGenerator(
              customPrintRequestConverter, printCustomCertificateFromCertificatePrintService);

      assertThrows(
          IllegalArgumentException.class,
          () ->
              generator.generate(
                  certificate,
                  PdfGeneratorOptions.builder()
                      .additionalInfoText(ADDITIONAL_INFO_TEXT)
                      .citizenFormat(false)
                      .hiddenElements(List.of())
                      .build()));
    }
  }

  @Nested
  class WhenCustomPdfSpecification {

    private static final byte[] TEMPLATE_BYTES = new byte[] {1, 2, 3};
    private static final byte[] RAW_PDF_BYTES = new byte[] {10, 20, 30};
    private static final byte[] ENCODED_PDF_BYTES = Base64.getEncoder().encode(RAW_PDF_BYTES);

    private CustomPdfGenerator customPdfGenerator;
    private CustomPdfSpecification spec;
    private PdfGeneratorOptions options;

    @BeforeEach
    void setUp() {
      customPdfGenerator =
          spy(
              new CustomPdfGenerator(
                  customPrintRequestConverter, printCustomCertificateFromCertificatePrintService));

      spec = buildSpec();
      options =
          PdfGeneratorOptions.builder()
              .additionalInfoText(ADDITIONAL_INFO_TEXT)
              .citizenFormat(false)
              .hiddenElements(List.of())
              .build();

      when(certificate.certificateModel()).thenReturn(certificateModel);
      when(certificateModel.pdfSpecification()).thenReturn(spec);
      when(certificateModel.name()).thenReturn("Intyg om graviditet");
      doReturn(TEMPLATE_BYTES).when(customPdfGenerator).loadTemplate(anyString());
      when(customPrintRequestConverter.convert(any(), any(), any(), any(), anyString()))
          .thenReturn(null);
      when(printCustomCertificateFromCertificatePrintService.print(any()))
          .thenReturn(PrintCertificateResponseDTO.builder().pdfData(ENCODED_PDF_BYTES).build());
    }

    @Test
    void shallDecodeBase64PdfDataFromResponse() {
      final var pdf = customPdfGenerator.generate(certificate, options);

      assertArrayEquals(RAW_PDF_BYTES, pdf.pdfData());
    }

    @Test
    void shallReturnFilenameWithNormalizedCertificateName() {
      final var pdf = customPdfGenerator.generate(certificate, options);

      assertTrue(pdf.fileName().startsWith("intyg_om_graviditet_"));
    }

    @Test
    void shallLoadWithAddressTemplateWhenNotCitizenAndNotSent() {
      final var templateCaptor = ArgumentCaptor.forClass(String.class);
      customPdfGenerator.generate(certificate, options);

      verify(customPdfGenerator).loadTemplate(templateCaptor.capture());
      assertEquals(TEMPLATE_WITH_ADDRESS, templateCaptor.getValue());
    }

    @Test
    void shallLoadNoAddressTemplateWhenCitizenFormat() {
      final var citizenOptions =
          PdfGeneratorOptions.builder()
              .additionalInfoText(ADDITIONAL_INFO_TEXT)
              .citizenFormat(true)
              .hiddenElements(List.of())
              .build();
      final var templateCaptor = ArgumentCaptor.forClass(String.class);

      customPdfGenerator.generate(certificate, citizenOptions);

      verify(customPdfGenerator).loadTemplate(templateCaptor.capture());
      assertEquals(TEMPLATE_NO_ADDRESS, templateCaptor.getValue());
    }

    @Test
    void shallLoadNoAddressTemplateWhenSent() {
      when(certificate.sent()).thenReturn(Sent.builder().sentAt(LocalDateTime.now()).build());
      final var templateCaptor = ArgumentCaptor.forClass(String.class);

      customPdfGenerator.generate(certificate, options);

      verify(customPdfGenerator).loadTemplate(templateCaptor.capture());
      assertEquals(TEMPLATE_NO_ADDRESS, templateCaptor.getValue());
    }

    @Test
    void shallPassTemplateToConverter() {
      customPdfGenerator.generate(certificate, options);

      verify(customPrintRequestConverter)
          .convert(eq(certificate), eq(options), eq(spec), eq(TEMPLATE_BYTES), anyString());
    }

    private static CustomPdfSpecification buildSpec() {
      return CustomPdfSpecification.builder()
          .pdfTemplatePath(TEMPLATE_WITH_ADDRESS)
          .pdfNoAddressTemplatePath(TEMPLATE_NO_ADDRESS)
          .patientIdFieldIds(List.of(new PdfFieldId("form1[0].#subform[0].flt_txtPersonNr[0]")))
          .signature(
              PdfSignature.builder()
                  .signedDateFieldId(new PdfFieldId("form1[0].#subform[0].flt_datUnderskrift[0]"))
                  .signedByNameFieldId(
                      new PdfFieldId("form1[0].#subform[0].flt_txtNamnfortydligande[0]"))
                  .paTitleFieldId(new PdfFieldId("form1[0].#subform[0].flt_txtBefattning[0]"))
                  .specialtyFieldId(
                      new PdfFieldId("form1[0].#subform[0].flt_txtEventuellSpecialistkompetens[0]"))
                  .hsaIdFieldId(new PdfFieldId("form1[0].#subform[0].flt_txtLakarensHSA-ID[0]"))
                  .workplaceCodeFieldId(
                      new PdfFieldId("form1[0].#subform[0].flt_txtArbetsplatskod[0]"))
                  .contactInformation(
                      new PdfFieldId(
                          "form1[0].#subform[0].flt_txtVardgivarensNamnAdressTelefon[0]"))
                  .signatureWithAddressTagIndex(new PdfTagIndex(15))
                  .signatureWithoutAddressTagIndex(new PdfTagIndex(7))
                  .signaturePageIndex(0)
                  .build())
          .signatureTextX(100)
          .signatureTextY(50)
          .signatureTextFontSize(8)
          .build();
    }
  }
}
