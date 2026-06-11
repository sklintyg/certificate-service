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
package se.inera.intyg.certificateservice.certificate.custom.converter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateservice.certificate.custom.converter.CustomPdfMetadataConverter.CITIZEN_VISIBILITY_TEXT;
import static se.inera.intyg.certificateservice.certificate.custom.converter.CustomPdfMetadataConverter.DIGITALLY_SIGNED_TEXT;
import static se.inera.intyg.certificateservice.certificate.custom.converter.CustomPdfMetadataConverter.SENT_TEXT_PREFIX;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Sent;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndex;
import se.inera.intyg.certificateservice.domain.common.model.Recipient;
import se.inera.intyg.certificateservice.domain.common.model.RecipientId;

@ExtendWith(MockitoExtension.class)
class CustomPdfMetadataConverterTest {

  private static final String FILE_NAME = "intyg_om_graviditet_26-01-01_1200";
  private static final String ADDITIONAL_INFO = "Webcert 2.0";
  private static final String CERTIFICATE_ID_VALUE = "CERT-ID";
  private static final String RECIPIENT_NAME = "Försäkringskassan";

  @InjectMocks private CustomPdfMetadataConverter converter;

  @Mock private Certificate certificate;
  @Mock private CertificateModel certificateModel;

  private CustomPdfSpecification spec;
  private PdfGeneratorOptions options;

  @BeforeEach
  void setUp() {
    spec =
        CustomPdfSpecification.builder()
            .pdfTemplatePath("fk7210/pdf/fk7210_v1.pdf")
            .pdfNoAddressTemplatePath("fk7210/pdf/fk7210_v1_no_address.pdf")
            .patientIdFieldIds(List.of())
            .signature(
                PdfSignature.builder()
                    .signedDateFieldId(new PdfFieldId("date"))
                    .signedByNameFieldId(new PdfFieldId("name"))
                    .paTitleFieldId(new PdfFieldId("pa"))
                    .specialtyFieldId(new PdfFieldId("spec"))
                    .hsaIdFieldId(new PdfFieldId("hsa"))
                    .workplaceCodeFieldId(new PdfFieldId("wp"))
                    .contactInformation(new PdfFieldId("contact"))
                    .signatureWithAddressTagIndex(new PdfTagIndex(15))
                    .signatureWithoutAddressTagIndex(new PdfTagIndex(7))
                    .signaturePageIndex(0)
                    .build())
            .signatureTextX(100)
            .signatureTextY(50)
            .signatureTextFontSize(8)
            .build();

    options =
        PdfGeneratorOptions.builder()
            .additionalInfoText(ADDITIONAL_INFO)
            .citizenFormat(false)
            .hiddenElements(List.of())
            .build();
  }

  @Test
  void shallUseFileNameAsAccessibilityTitle() {
    when(certificate.status()).thenReturn(Status.DRAFT);

    final var result = converter.convert(certificate, options, spec, true, FILE_NAME);

    assertEquals(FILE_NAME, result.accessibilityMetadataDTO().title());
  }

  @Test
  void shallSetAddDraftWatermarkTrueWhenDraft() {
    when(certificate.status()).thenReturn(Status.DRAFT);

    final var result = converter.convert(certificate, options, spec, true, FILE_NAME);

    assertTrue(result.addDraftWatermark());
  }

  @Test
  void shallSetAddDraftWatermarkFalseWhenSigned() {
    when(certificate.status()).thenReturn(Status.SIGNED);
    when(certificate.id()).thenReturn(new CertificateId(CERTIFICATE_ID_VALUE));

    final var result = converter.convert(certificate, options, spec, true, FILE_NAME);

    org.junit.jupiter.api.Assertions.assertFalse(result.addDraftWatermark());
  }

  @Nested
  class RightMarginText {

    @Test
    void shallSetRightMarginTextWhenSigned() {
      when(certificate.status()).thenReturn(Status.SIGNED);
      when(certificate.id()).thenReturn(new CertificateId(CERTIFICATE_ID_VALUE));

      final var result = converter.convert(certificate, options, spec, true, FILE_NAME);

      assertEquals(
          "Intygsid: " + CERTIFICATE_ID_VALUE + ". " + ADDITIONAL_INFO, result.rightMarginText());
    }

    @Test
    void shallNotSetRightMarginTextWhenDraft() {
      when(certificate.status()).thenReturn(Status.DRAFT);

      final var result = converter.convert(certificate, options, spec, true, FILE_NAME);

      assertNull(result.rightMarginText());
    }
  }

  @Nested
  class DigitalSignatureText {

    @Test
    void shallAddDigitalSignatureTextWithAddressTagWhenSignedAndIncludeAddress() {
      when(certificate.status()).thenReturn(Status.SIGNED);
      when(certificate.id()).thenReturn(new CertificateId(CERTIFICATE_ID_VALUE));

      final var result = converter.convert(certificate, options, spec, true, FILE_NAME);

      final var sigText =
          result.customTextDTOList().stream()
              .filter(t -> DIGITALLY_SIGNED_TEXT.equals(t.value()))
              .findFirst();
      assertTrue(sigText.isPresent());
      assertAll(
          () -> assertEquals(100, sigText.get().x()),
          () -> assertEquals(50, sigText.get().y()),
          () -> assertEquals(8, sigText.get().fontSize()),
          () -> assertEquals(0, sigText.get().pageIndex()),
          () -> assertEquals(15, sigText.get().tagIndex()));
    }

    @Test
    void shallUseWithoutAddressTagWhenNotIncludeAddress() {
      when(certificate.status()).thenReturn(Status.SIGNED);
      when(certificate.id()).thenReturn(new CertificateId(CERTIFICATE_ID_VALUE));

      final var result = converter.convert(certificate, options, spec, false, FILE_NAME);

      final var sigText =
          result.customTextDTOList().stream()
              .filter(t -> DIGITALLY_SIGNED_TEXT.equals(t.value()))
              .findFirst();
      assertTrue(sigText.isPresent());
      assertEquals(7, sigText.get().tagIndex());
    }

    @Test
    void shallNotAddDigitalSignatureTextWhenDraft() {
      when(certificate.status()).thenReturn(Status.DRAFT);

      final var result = converter.convert(certificate, options, spec, true, FILE_NAME);

      assertTrue(
          result.customTextDTOList().stream()
              .noneMatch(t -> DIGITALLY_SIGNED_TEXT.equals(t.value())));
    }
  }

  @Nested
  class SentTexts {

    private final Sent sentCertificate =
        Sent.builder()
            .recipient(new Recipient(new RecipientId("FK"), RECIPIENT_NAME, "FKASSA"))
            .sentAt(LocalDateTime.now())
            .build();

    @Test
    void shallNotAddSentTextWhenNotSent() {
      when(certificate.status()).thenReturn(Status.DRAFT);
      // certificate.sent() returns null by default — no sent block entered

      final var result = converter.convert(certificate, options, spec, true, FILE_NAME);

      assertTrue(
          result.customTextDTOList().stream()
              .noneMatch(t -> t.value().startsWith(SENT_TEXT_PREFIX)));
    }

    @Test
    void shallAddSentTextWhenCertificateIsSent() {
      when(certificate.status()).thenReturn(Status.DRAFT);
      when(certificate.certificateModel()).thenReturn(certificateModel);
      when(certificate.sent()).thenReturn(sentCertificate);

      final var result = converter.convert(certificate, options, spec, true, FILE_NAME);

      assertTrue(
          result.customTextDTOList().stream()
              .anyMatch(t -> t.value().equals(SENT_TEXT_PREFIX + RECIPIENT_NAME)));
    }

    @Test
    void shallAddCitizenVisibilityTextWhenSentAndAvailableForCitizen() {
      when(certificate.status()).thenReturn(Status.DRAFT);
      when(certificate.certificateModel()).thenReturn(certificateModel);
      when(certificateModel.availableForCitizen()).thenReturn(true);
      when(certificate.sent()).thenReturn(sentCertificate);

      final var result = converter.convert(certificate, options, spec, true, FILE_NAME);

      assertTrue(
          result.customTextDTOList().stream()
              .anyMatch(t -> CITIZEN_VISIBILITY_TEXT.equals(t.value())));
    }

    @Test
    void shallNotAddCitizenVisibilityTextWhenNotAvailableForCitizen() {
      when(certificate.status()).thenReturn(Status.DRAFT);
      when(certificate.certificateModel()).thenReturn(certificateModel);
      when(certificateModel.availableForCitizen()).thenReturn(false);
      when(certificate.sent()).thenReturn(sentCertificate);

      final var result = converter.convert(certificate, options, spec, true, FILE_NAME);

      assertTrue(
          result.customTextDTOList().stream()
              .noneMatch(t -> CITIZEN_VISIBILITY_TEXT.equals(t.value())));
    }
  }
}
