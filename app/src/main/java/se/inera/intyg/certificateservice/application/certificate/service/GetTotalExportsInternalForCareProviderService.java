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

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.dto.TotalExportsInternalResponse;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;

@Service
@RequiredArgsConstructor
public class GetTotalExportsInternalForCareProviderService {

  private final CertificateRepository certificateRepository;

  public TotalExportsInternalResponse get(String careProviderId) {
    final var certificateExportPage =
        certificateRepository.getExportByCareProviderId(new HsaId(careProviderId), 0, 1);

    return TotalExportsInternalResponse.builder()
        .totalCertificates(certificateExportPage.total())
        .totalRevokedCertificates(certificateExportPage.totalRevoked())
        .build();
  }
}
