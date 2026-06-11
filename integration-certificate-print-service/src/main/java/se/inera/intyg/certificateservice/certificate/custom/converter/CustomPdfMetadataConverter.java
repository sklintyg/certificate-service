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

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.custom.dto.AccessibilityMetadataDTO;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPdfMetadataDTO;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomTextDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;

@Component
public class CustomPdfMetadataConverter {

  static final String DIGITALLY_SIGNED_TEXT =
      "Detta är en utskrift av ett elektroniskt intyg. "
          + "Intyget har signerats elektroniskt av intygsutfärdaren.";
  static final String SENT_TEXT_PREFIX = "Intyget har skickats digitalt till ";
  static final String CITIZEN_VISIBILITY_TEXT = "Du kan se intyget genom att logga in på 1177.se";
  private static final int SENT_TEXT_FONT_SIZE = 22;
  private static final int CITIZEN_VISIBILITY_TEXT_FONT_SIZE = 16;

  public CustomPdfMetadataDTO convert(
      Certificate certificate,
      PdfGeneratorOptions options,
      CustomPdfSpecification spec,
      boolean includeAddress,
      String fileName) {
    return new CustomPdfMetadataDTO(
        buildTextList(certificate, spec, includeAddress),
        new AccessibilityMetadataDTO(fileName),
        buildRightMarginText(certificate, options),
        certificate.status() == Status.DRAFT);
  }

  private static List<CustomTextDTO> buildTextList(
      Certificate certificate, CustomPdfSpecification spec, boolean includeAddress) {
    final var textList = new ArrayList<CustomTextDTO>();

    if (certificate.status() == Status.SIGNED) {
      final var tagIndex =
          includeAddress
              ? spec.signature().signatureWithAddressTagIndex().value()
              : spec.signature().signatureWithoutAddressTagIndex().value();
      textList.add(
          CustomTextDTO.builder()
              .value(DIGITALLY_SIGNED_TEXT)
              .x(spec.signatureTextX())
              .y(spec.signatureTextY())
              .fontSize(spec.signatureTextFontSize())
              .pageIndex(spec.signature().signaturePageIndex())
              .tagIndex(tagIndex)
              .build());
    }

    final var sent = certificate.sent();
    if (sent != null && sent.sentAt() != null) {
      textList.add(
          CustomTextDTO.builder()
              .value(SENT_TEXT_PREFIX + sent.recipient().name())
              .fontSize(SENT_TEXT_FONT_SIZE)
              .build());

      if (Boolean.TRUE.equals(certificate.certificateModel().availableForCitizen())) {
        textList.add(
            CustomTextDTO.builder()
                .value(CITIZEN_VISIBILITY_TEXT)
                .fontSize(CITIZEN_VISIBILITY_TEXT_FONT_SIZE)
                .build());
      }
    }

    return textList;
  }

  private static String buildRightMarginText(Certificate certificate, PdfGeneratorOptions options) {
    if (certificate.status() != Status.SIGNED) {
      return null;
    }
    return "Intygsid: %s. %s".formatted(certificate.id().id(), options.additionalInfoText());
  }
}
