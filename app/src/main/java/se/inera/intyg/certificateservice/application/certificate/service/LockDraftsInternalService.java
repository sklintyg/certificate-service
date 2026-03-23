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
package se.inera.intyg.certificateservice.application.certificate.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.dto.LockDraftsRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.LockDraftsResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.domain.certificate.service.LockCertificateDomainService;

@Service
@RequiredArgsConstructor
public class LockDraftsInternalService {

  private final LockCertificateDomainService lockCertificateDomainService;
  private final CertificateConverter converter;

  @Transactional
  public LockDraftsResponse lock(LockDraftsRequest request) {
    if (request.getCutoffDate() == null) {
      throw new IllegalArgumentException("Cutoff date cannot be null");
    }

    final var lockedCertificates = lockCertificateDomainService.lock(request.getCutoffDate());

    return LockDraftsResponse.builder()
        .certificates(
            lockedCertificates.stream()
                .map(draft -> converter.convert(draft, Collections.emptyList(), null))
                .toList())
        .build();
  }
}
