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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModelConstants.FK_RECIPIENT;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.CITIZEN_VISIBILITY_TEXT;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.CITIZEN_VISIBILITY_TEXT_FONT_SIZE;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.CITIZEN_VISIBILITY_TEXT_X;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.CITIZEN_VISIBILITY_TEXT_Y;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.DIGITALLY_SIGNED_TEXT;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.PDF_SIGNATURE_PAGE_INDEX;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.PDF_SIGNATURE_TEXT_FONT_SIZE;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.PDF_SIGNATURE_TEXT_X;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.PDF_SIGNATURE_TEXT_Y;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.SENT_TEXT_FONT_SIZE;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.SENT_TEXT_PREFIX;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.SENT_TEXT_X;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210OverlayTextProvider.SENT_TEXT_Y;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Sent;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.Appearance;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FontStyle;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndex;
import se.inera.intyg.certificateservice.domain.common.model.Recipient;
import se.inera.intyg.certificateservice.domain.common.model.RecipientId;

@ExtendWith(MockitoExtension.class)
class FK7210OverlayTextProviderTest {

  private static final String RECIPIENT_NAME = "Försäkringskassan";
  private static final PdfTagIndex TAG_WITH_ADDRESS = new PdfTagIndex(15);
  private static final PdfTagIndex TAG_WITHOUT_ADDRESS = new PdfTagIndex(7);

  @Mock private Certificate certificate;
  @Mock private CertificateModel certificateModel;

  private final FK7210OverlayTextProvider provider = new FK7210OverlayTextProvider();

  private PdfGeneratorOptions defaultOptions() {
    return PdfGeneratorOptions.builder()
        .additionalInfoText("Webcert")
        .citizenFormat(false)
        .hiddenElements(List.of())
        .build();
  }

  @Test
  void shallReturnEmptyListWhenDraftAndNotSent() {
    when(certificate.status()).thenReturn(Status.DRAFT);
    when(certificate.sent()).thenReturn(null);

    final var texts = provider.of(certificate, TAG_WITH_ADDRESS, defaultOptions());

    assertTrue(texts.isEmpty());
  }

  @Test
  void shallReturnDigitallySignedTextWhenSignedAndNotSent() {
    when(certificate.status()).thenReturn(Status.SIGNED);
    when(certificate.sent()).thenReturn(null);

    final var texts = provider.of(certificate, TAG_WITH_ADDRESS, defaultOptions());

    assertEquals(1, texts.size());
    final var text = texts.getFirst();
    assertAll(
        () -> assertEquals(DIGITALLY_SIGNED_TEXT, text.value()),
        () -> assertEquals(PDF_SIGNATURE_TEXT_X, text.x()),
        () -> assertEquals(PDF_SIGNATURE_TEXT_Y, text.y()),
        () ->
            assertEquals(
                new Appearance(PDF_SIGNATURE_TEXT_FONT_SIZE, FontStyle.BOLD), text.appearance()),
        () -> assertEquals(PDF_SIGNATURE_PAGE_INDEX, text.pageIndex()),
        () -> assertEquals(TAG_WITH_ADDRESS.value(), text.tagIndex()));
  }

  @Test
  void shallUseTagFromProviderForDigitalSignatureText() {
    when(certificate.status()).thenReturn(Status.SIGNED);
    when(certificate.sent()).thenReturn(null);

    final var texts = provider.of(certificate, TAG_WITHOUT_ADDRESS, defaultOptions());

    assertEquals(TAG_WITHOUT_ADDRESS.value(), texts.getFirst().tagIndex());
  }

  @Test
  void shallReturnDigitallySignedAndSentTextWhenSignedAndSent() {
    when(certificate.status()).thenReturn(Status.SIGNED);
    when(certificate.sent())
        .thenReturn(Sent.builder().sentAt(LocalDateTime.now()).recipient(FK_RECIPIENT).build());
    when(certificate.certificateModel()).thenReturn(certificateModel);
    when(certificateModel.availableForCitizen()).thenReturn(false);

    final var texts = provider.of(certificate, TAG_WITHOUT_ADDRESS, defaultOptions());

    assertEquals(2, texts.size());
    assertAll(
        () -> assertEquals(DIGITALLY_SIGNED_TEXT, texts.getFirst().value()),
        () -> assertEquals(SENT_TEXT_PREFIX + RECIPIENT_NAME, texts.get(1).value()),
        () -> assertEquals(SENT_TEXT_X, texts.get(1).x()),
        () -> assertEquals(SENT_TEXT_Y, texts.get(1).y()),
        () -> assertEquals(new Appearance(SENT_TEXT_FONT_SIZE), texts.get(1).appearance()));
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

    final var texts = provider.of(certificate, TAG_WITHOUT_ADDRESS, defaultOptions());

    assertEquals(3, texts.size());
    assertAll(
        () -> assertEquals(DIGITALLY_SIGNED_TEXT, texts.getFirst().value()),
        () -> assertEquals(SENT_TEXT_PREFIX + RECIPIENT_NAME, texts.get(1).value()),
        () -> assertEquals(CITIZEN_VISIBILITY_TEXT, texts.get(2).value()),
        () -> assertEquals(CITIZEN_VISIBILITY_TEXT_X, texts.get(2).x()),
        () -> assertEquals(CITIZEN_VISIBILITY_TEXT_Y, texts.get(2).y()),
        () ->
            assertEquals(
                new Appearance(CITIZEN_VISIBILITY_TEXT_FONT_SIZE), texts.get(2).appearance()));
  }
}
