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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider.CITIZEN_VISIBILITY_TEXT;
import static se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider.CITIZEN_VISIBILITY_TEXT_FONT_SIZE;
import static se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider.CITIZEN_VISIBILITY_TEXT_X;
import static se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider.CITIZEN_VISIBILITY_TEXT_Y;
import static se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider.DIGITALLY_SIGNED_TEXT;
import static se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider.PDF_SIGNATURE_TEXT_FONT_SIZE;
import static se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayTextProvider.SENT_TEXT_PREFIX;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModelConstants.FK_RECIPIENT;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Sent;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.common.model.Recipient;
import se.inera.intyg.certificateservice.domain.common.model.RecipientId;

@ExtendWith(MockitoExtension.class)
class OverlayTextProviderTest {

  private static final String RECIPIENT_NAME = "Försäkringskassan";
  private static final int SIGNED_TEXT_WITH_ADDRESS_INDEX = 15;
  private static final int SIGNED_TEXT_WITHOUT_ADDRESS_INDEX = 7;
  private static final int SENT_TEXT_INDEX = 3;
  private static final int CITIZEN_TEXT_INDEX = 4;

  @Mock private Certificate certificate;
  @Mock private CertificateModel certificateModel;

  private final OverlayDetails overlayDetails =
      OverlayDetails.builder()
          .signatureTextX(173f)
          .signatureTextY(523f)
          .signaturePageIndex(0)
          .signedTextWithAddressIndex(SIGNED_TEXT_WITH_ADDRESS_INDEX)
          .signedTextWithoutAddressIndex(SIGNED_TEXT_WITHOUT_ADDRESS_INDEX)
          .sentTextIndex(SENT_TEXT_INDEX)
          .citizenTextIndex(CITIZEN_TEXT_INDEX)
          .build();

  private final OverlayTextProvider provider = new OverlayTextProvider(overlayDetails);

  private final PdfGeneratorOptions optionsWithAddress =
      PdfGeneratorOptions.builder().citizenFormat(false).build();

  private final PdfGeneratorOptions optionsCitizenFormat =
      PdfGeneratorOptions.builder().citizenFormat(true).build();

  @Test
  void shallReturnEmptyListWhenDraftAndNotSent() {
    when(certificate.status()).thenReturn(Status.DRAFT);
    when(certificate.sent()).thenReturn(null);

    final var texts = provider.of(certificate, optionsWithAddress);

    assertTrue(texts.isEmpty());
  }

  @Test
  void shallReturnDigitallySignedTextWhenSignedAndNotSent() {
    when(certificate.status()).thenReturn(Status.SIGNED);
    when(certificate.sent()).thenReturn(null);

    final var texts = provider.of(certificate, optionsWithAddress);

    assertEquals(1, texts.size());
    final var text = texts.getFirst();
    assertAll(
        () -> assertEquals(DIGITALLY_SIGNED_TEXT, text.value()),
        () -> assertEquals(173f, text.x()),
        () -> assertEquals(523f, text.y()),
        () ->
            assertEquals(
                new Appearance(PDF_SIGNATURE_TEXT_FONT_SIZE, FontStyle.BOLD), text.appearance()),
        () -> assertEquals(overlayDetails.signaturePageIndex(), text.pageIndex()),
        () -> assertEquals(SIGNED_TEXT_WITH_ADDRESS_INDEX, text.tagIndex()));
  }

  @Test
  void shallUseWithoutAddressIndexWhenCitizenFormat() {
    when(certificate.status()).thenReturn(Status.SIGNED);
    when(certificate.sent()).thenReturn(null);

    final var texts = provider.of(certificate, optionsCitizenFormat);

    assertEquals(SIGNED_TEXT_WITHOUT_ADDRESS_INDEX, texts.getFirst().tagIndex());
  }

  @Test
  void shallUseWithoutAddressIndexWhenSent() {
    when(certificate.status()).thenReturn(Status.SIGNED);
    when(certificate.sent())
        .thenReturn(Sent.builder().sentAt(LocalDateTime.now()).recipient(FK_RECIPIENT).build());
    when(certificate.certificateModel()).thenReturn(certificateModel);
    when(certificateModel.availableForCitizen()).thenReturn(false);

    final var texts = provider.of(certificate, optionsWithAddress);

    assertEquals(2, texts.size());
    assertEquals(SIGNED_TEXT_WITHOUT_ADDRESS_INDEX, texts.get(1).tagIndex());
  }

  @Test
  void shallReturnDigitallySignedAndSentTextWhenSignedAndSent() {
    when(certificate.status()).thenReturn(Status.SIGNED);
    when(certificate.sent())
        .thenReturn(Sent.builder().sentAt(LocalDateTime.now()).recipient(FK_RECIPIENT).build());
    when(certificate.certificateModel()).thenReturn(certificateModel);
    when(certificateModel.availableForCitizen()).thenReturn(false);

    final var texts = provider.of(certificate, optionsWithAddress);

    assertEquals(2, texts.size());
    assertAll(
        () -> assertEquals(SENT_TEXT_PREFIX + RECIPIENT_NAME, texts.getFirst().value()),
        () -> assertEquals(SENT_TEXT_INDEX, texts.getFirst().tagIndex()),
        () -> assertEquals(DIGITALLY_SIGNED_TEXT, texts.get(1).value()),
        () -> assertEquals(overlayDetails.signatureTextX(), texts.get(1).x()),
        () -> assertEquals(overlayDetails.signatureTextY(), texts.get(1).y()),
        () ->
            assertEquals(
                new Appearance(PDF_SIGNATURE_TEXT_FONT_SIZE, FontStyle.BOLD),
                texts.get(1).appearance()));
  }

  @Test
  void shallReturnAllThreeTextsWhenSignedSentAndAvailableForCitizen() {
    when(certificate.status()).thenReturn(Status.SIGNED);
    when(certificate.sent())
        .thenReturn(
            Sent.builder()
                .sentAt(LocalDateTime.now())
                .recipient(new Recipient(new RecipientId("fk"), RECIPIENT_NAME, "address"))
                .build());
    when(certificate.certificateModel()).thenReturn(certificateModel);
    when(certificateModel.availableForCitizen()).thenReturn(true);

    final var texts = provider.of(certificate, optionsWithAddress);

    assertEquals(3, texts.size());
    assertAll(
        () -> assertEquals(SENT_TEXT_PREFIX + RECIPIENT_NAME, texts.getFirst().value()),
        () -> assertEquals(SENT_TEXT_INDEX, texts.getFirst().tagIndex()),
        () -> assertEquals(CITIZEN_VISIBILITY_TEXT, texts.get(1).value()),
        () -> assertEquals(CITIZEN_VISIBILITY_TEXT_X, texts.get(1).x()),
        () -> assertEquals(CITIZEN_VISIBILITY_TEXT_Y, texts.get(1).y()),
        () -> assertEquals(CITIZEN_TEXT_INDEX, texts.get(1).tagIndex()),
        () -> assertEquals(DIGITALLY_SIGNED_TEXT, texts.get(2).value()),
        () ->
            assertEquals(
                new Appearance(CITIZEN_VISIBILITY_TEXT_FONT_SIZE), texts.get(1).appearance()));
  }
}
