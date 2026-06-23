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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.FK7804_CERTIFICATE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.ATHENA_REACT_ANDERSSON;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.certificate.custom.dto.PersonIdConfigDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.Appearance;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverflowPageIndex;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndex;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndexProvider;

@ExtendWith(MockitoExtension.class)
class CustomPdfMetadataConverterTest {

  private static final String FILE_NAME = "intyg_om_graviditet_26-01-01_1200";
  private static final String ADDITIONAL_INFO = "Webcert 2.0";
  private static final String CERTIFICATE_ID_VALUE = "CERT-ID";
  private static final PdfFieldId PDF_PATIENT_ID_FIELD_ID = new PdfFieldId("fieldId");

  @Mock private Certificate certificate;
  @Mock private OverlayTextProvider overlayTextProvider;
  @Mock private PdfTagIndexProvider tagIndexProvider;
  @InjectMocks private CustomPdfMetadataConverter converter;

  private CustomPdfSpecification spec;
  private PdfGeneratorOptions options;

  @BeforeEach
  void setUp() {
    spec =
        CustomPdfSpecification.builder()
            .signature(CustomPdfSignature.builder().pdfTagIndexProvider(tagIndexProvider).build())
            .overlayTextProvider(overlayTextProvider)
            .build();

    options =
        PdfGeneratorOptions.builder()
            .additionalInfoText(ADDITIONAL_INFO)
            .citizenFormat(false)
            .hiddenElements(List.of())
            .build();

    when(tagIndexProvider.of(any(), any())).thenReturn(new PdfTagIndex(15));
    when(overlayTextProvider.of(any(), any())).thenReturn(List.of());
  }

  @Test
  void shallSetFileNameAsAccessibilityTitle() {
    when(certificate.status()).thenReturn(Status.DRAFT);

    final var result = converter.convert(certificate, options, spec, FILE_NAME);

    assertEquals(FILE_NAME, result.accessibilityMetadata().title());
  }

  @Test
  void shallSetDraftWatermarkWhenDraft() {
    when(certificate.status()).thenReturn(Status.DRAFT);

    final var result = converter.convert(certificate, options, spec, FILE_NAME);

    assertTrue(result.addDraftWatermark());
  }

  @Test
  void shallNotSetDraftWatermarkWhenSigned() {
    when(certificate.status()).thenReturn(Status.SIGNED);
    when(certificate.id()).thenReturn(new CertificateId(CERTIFICATE_ID_VALUE));

    final var result = converter.convert(certificate, options, spec, FILE_NAME);

    assertFalse(result.addDraftWatermark());
  }

  @Test
  void shallSetOverflowPageIndexIfPresent() {
    final var pdfSpec =
        CustomPdfSpecification.builder()
            .signature(CustomPdfSignature.builder().pdfTagIndexProvider(tagIndexProvider).build())
            .overlayTextProvider(overlayTextProvider)
            .overFlowPageIndex(new OverflowPageIndex(2))
            .patientIdFieldIds(List.of(PDF_PATIENT_ID_FIELD_ID))
            .build();

    final var result = converter.convert(FK7804_CERTIFICATE, options, pdfSpec, FILE_NAME);

    assertEquals(2, result.overflowPageIndex());
  }

  @Test
  void shallSetCustomPdfPersonIdIfOverflowPageIndexIsPresent() {
    final var expectedPersonId =
        new PersonIdConfigDTO(
            PDF_PATIENT_ID_FIELD_ID.id(), ATHENA_REACT_ANDERSSON.id().idWithoutDash());
    final var pdfSpec =
        CustomPdfSpecification.builder()
            .signature(CustomPdfSignature.builder().pdfTagIndexProvider(tagIndexProvider).build())
            .overlayTextProvider(overlayTextProvider)
            .overFlowPageIndex(new OverflowPageIndex(2))
            .patientIdFieldIds(List.of(PDF_PATIENT_ID_FIELD_ID))
            .build();

    final var result = converter.convert(FK7804_CERTIFICATE, options, pdfSpec, FILE_NAME);

    assertEquals(expectedPersonId.fieldId(), result.personId().fieldId());
    assertEquals(
        FK7804_CERTIFICATE.certificateMetaData().patient().id().idWithoutDash(),
        result.personId().value());
  }

  @Test
  void shallNotSetCustomPdfPersonIdIfOverflowPageIndexIsMissing() {
    final var pdfSpec =
        CustomPdfSpecification.builder()
            .signature(CustomPdfSignature.builder().pdfTagIndexProvider(tagIndexProvider).build())
            .overlayTextProvider(overlayTextProvider)
            .patientIdFieldIds(List.of(PDF_PATIENT_ID_FIELD_ID))
            .build();

    final var result = converter.convert(FK7804_CERTIFICATE, options, pdfSpec, FILE_NAME);

    assertNull(result.personId());
  }

  @Test
  void shallSetOverflowPageIndexToNullIfMissing() {
    final var pdfSpec =
        CustomPdfSpecification.builder()
            .signature(CustomPdfSignature.builder().pdfTagIndexProvider(tagIndexProvider).build())
            .overlayTextProvider(overlayTextProvider)
            .build();

    when(certificate.status()).thenReturn(Status.DRAFT);

    final var result = converter.convert(certificate, options, pdfSpec, FILE_NAME);

    assertNull(result.overflowPageIndex());
  }

  @Nested
  class RightMarginText {

    @Test
    void shallSetRightMarginTextWhenSigned() {
      when(certificate.status()).thenReturn(Status.SIGNED);
      when(certificate.id()).thenReturn(new CertificateId(CERTIFICATE_ID_VALUE));

      final var result = converter.convert(certificate, options, spec, FILE_NAME);

      assertEquals(
          "Intygsid: " + CERTIFICATE_ID_VALUE + ". " + ADDITIONAL_INFO, result.rightMarginText());
    }

    @Test
    void shallNotSetRightMarginTextWhenDraft() {
      when(certificate.status()).thenReturn(Status.DRAFT);

      final var result = converter.convert(certificate, options, spec, FILE_NAME);

      assertNull(result.rightMarginText());
    }
  }

  @Nested
  class CustomTexts {

    @Test
    void shallMapOverlayTextsToCustomTextDTOs() {
      final var overlayText =
          OverlayText.builder()
              .value("Electronically signed")
              .x(173)
              .y(523)
              .appearance(new Appearance(8f, null))
              .pageIndex(0)
              .tagIndex(15)
              .build();
      when(overlayTextProvider.of(any(), any())).thenReturn(List.of(overlayText));
      when(certificate.status()).thenReturn(Status.DRAFT);

      final var result = converter.convert(certificate, options, spec, FILE_NAME);

      assertEquals(1, result.customTexts().size());
      assertEquals("Electronically signed", result.customTexts().getFirst().value());
    }
  }
}
