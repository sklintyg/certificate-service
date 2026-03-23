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
package se.inera.intyg.certificateservice.application.citizen.service.converter;

import java.util.Collections;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.citizen.dto.CertificateLinkDTO;
import se.inera.intyg.certificateservice.application.citizen.dto.CertificateTextDTO;
import se.inera.intyg.certificateservice.application.citizen.dto.CertificateTextTypeDTO;
import se.inera.intyg.certificateservice.domain.common.model.CertificateText;
import se.inera.intyg.certificateservice.domain.common.model.CertificateTextType;

@Component()
public class CertificateTextConverter {

  public CertificateTextDTO convert(CertificateText certificateText) {
    return CertificateTextDTO.builder()
        .text(certificateText.text())
        .type(convertCertificateTextType(certificateText.type()))
        .links(
            certificateText.links() == null
                ? Collections.emptyList()
                : certificateText.links().stream()
                    .map(
                        link ->
                            CertificateLinkDTO.builder()
                                .url(link.url())
                                .id(link.id())
                                .name(link.name())
                                .build())
                    .toList())
        .build();
  }

  private CertificateTextTypeDTO convertCertificateTextType(
      CertificateTextType certificateTextType) {
    if (certificateTextType == CertificateTextType.PREAMBLE_TEXT) {
      return CertificateTextTypeDTO.PREAMBLE_TEXT;
    }
    throw new IllegalArgumentException(
        "Certificate text type '%s' is not supported".formatted(certificateTextType));
  }
}
