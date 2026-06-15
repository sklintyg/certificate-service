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

import java.util.ArrayList;
import java.util.List;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;

public class OverlayTextProvider {

  // Signature text position derived from the signed-date field rectangle in the PDF template:
  // x = upperRightX (113.385) + 60 = 173, y = lowerLeftY (521.274) + 2 = 523
  static final float PDF_SIGNATURE_TEXT_X = 173f;
  static final float PDF_SIGNATURE_TEXT_Y = 523f;
  static final float PDF_SIGNATURE_TEXT_FONT_SIZE = 8f;
  static final int PDF_SIGNATURE_PAGE_INDEX = 0;
  static final String DIGITALLY_SIGNED_TEXT =
      "Detta är en utskrift av ett elektroniskt intyg. "
          + "Intyget har signerats elektroniskt av intygsutfärdaren.";

  static final float SENT_TEXT_X = 40f;
  static final float SENT_TEXT_Y = 685f;
  static final float SENT_TEXT_FONT_SIZE = 22f;
  static final String SENT_TEXT_PREFIX = "Intyget har skickats digitalt till ";

  static final float CITIZEN_VISIBILITY_TEXT_X = 40f;
  static final float CITIZEN_VISIBILITY_TEXT_Y = 665f;
  static final float CITIZEN_VISIBILITY_TEXT_FONT_SIZE = 16f;
  static final String CITIZEN_VISIBILITY_TEXT = "Du kan se intyget genom att logga in på 1177.se";

  public List<OverlayText> of(Certificate certificate, PdfTagIndex tagIndex) {
    final var texts = new ArrayList<OverlayText>();

    if (certificate.status() == Status.SIGNED) {
      texts.add(digitallySignedText(tagIndex));
    }

    final var sent = certificate.sent();
    if (sent != null && sent.sentAt() != null) {
      texts.add(sentText(sent.recipient().name()));

      if (Boolean.TRUE.equals(certificate.certificateModel().availableForCitizen())) {
        texts.add(citizenVisibilityText());
      }
    }

    return texts;
  }

  private static OverlayText digitallySignedText(PdfTagIndex pdfTagIndex) {
    return OverlayText.builder()
        .value(DIGITALLY_SIGNED_TEXT)
        .x(PDF_SIGNATURE_TEXT_X)
        .y(PDF_SIGNATURE_TEXT_Y)
        .appearance(new Appearance(PDF_SIGNATURE_TEXT_FONT_SIZE, FontStyle.BOLD))
        .pageIndex(PDF_SIGNATURE_PAGE_INDEX)
        .tagIndex(pdfTagIndex.value())
        .build();
  }

  private static OverlayText sentText(String recipientName) {
    return OverlayText.builder()
        .value(SENT_TEXT_PREFIX + recipientName)
        .x(SENT_TEXT_X)
        .y(SENT_TEXT_Y)
        .appearance(new Appearance(SENT_TEXT_FONT_SIZE))
        .pageIndex(0)
        .build();
  }

  private static OverlayText citizenVisibilityText() {
    return OverlayText.builder()
        .value(CITIZEN_VISIBILITY_TEXT)
        .x(CITIZEN_VISIBILITY_TEXT_X)
        .y(CITIZEN_VISIBILITY_TEXT_Y)
        .appearance(new Appearance(CITIZEN_VISIBILITY_TEXT_FONT_SIZE))
        .pageIndex(0)
        .build();
  }
}
