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
import se.inera.intyg.certificateservice.application.certificate.dto.DisposeObsoleteDraftsRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.DisposeObsoleteDraftsResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.ListObsoleteDraftsRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.ListObsoleteDraftsResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.service.DisposeObsoleteDraftsDomainService;

@Service
@RequiredArgsConstructor
public class DisposeObsoleteDraftsInternalService {

  private final DisposeObsoleteDraftsDomainService disposeObsoleteDraftsDomainService;
  private final CertificateConverter converter;

  public ListObsoleteDraftsResponse list(ListObsoleteDraftsRequest request) {
    if (request.getCutoffDate() == null) {
      throw new IllegalArgumentException("Cutoff date cannot be null");
    }

    final var drafts = disposeObsoleteDraftsDomainService.list(request.getCutoffDate());

    return ListObsoleteDraftsResponse.builder()
        .certificateIds(drafts.stream().map(CertificateId::id).toList())
        .build();
  }

  @Transactional
  public DisposeObsoleteDraftsResponse delete(DisposeObsoleteDraftsRequest request) {
    if (request.getCertificateId() == null) {
      throw new IllegalArgumentException("Certificate ID cannot be null");
    }

    final var deletedCertificate =
        disposeObsoleteDraftsDomainService.delete(new CertificateId(request.getCertificateId()));

    return DisposeObsoleteDraftsResponse.builder()
        .certificate(converter.convert(deletedCertificate, Collections.emptyList(), null))
        .build();
  }
}
