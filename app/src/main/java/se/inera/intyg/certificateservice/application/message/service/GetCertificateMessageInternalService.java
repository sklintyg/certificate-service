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
package se.inera.intyg.certificateservice.application.message.service;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.message.dto.GetCertificateMessageInternalResponse;
import se.inera.intyg.certificateservice.application.message.service.converter.QuestionConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;

@Service
@RequiredArgsConstructor
public class GetCertificateMessageInternalService {

  private final CertificateRepository certificateRepository;
  private final QuestionConverter questionConverter;

  public GetCertificateMessageInternalResponse get(String certificateId) {
    final var exists = certificateRepository.exists(new CertificateId(certificateId));
    if (!exists) {
      return GetCertificateMessageInternalResponse.builder()
          .questions(Collections.emptyList())
          .build();
    }

    final var certificate = certificateRepository.getById(new CertificateId(certificateId));
    return GetCertificateMessageInternalResponse.builder()
        .questions(
            certificate.messages().stream()
                .map(message -> questionConverter.convert(message, Collections.emptyList()))
                .toList())
        .build();
  }
}
