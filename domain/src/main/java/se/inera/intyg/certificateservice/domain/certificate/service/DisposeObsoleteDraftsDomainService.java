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
package se.inera.intyg.certificateservice.domain.certificate.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.model.CertificatesRequest;

@RequiredArgsConstructor
public class DisposeObsoleteDraftsDomainService {

  private final CertificateRepository certificateRepository;

  public List<CertificateId> list(LocalDateTime cutoffDate) {
    return certificateRepository.findIdsByCreatedBeforeAndStatusIn(
        CertificatesRequest.builder().createdTo(cutoffDate).statuses(Status.unsigned()).build());
  }

  public Certificate delete(CertificateId certificateId) {
    final var certificate = certificateRepository.getById(certificateId);

    if (certificate == null) {
      throw new IllegalStateException(
          String.format(
              "Certificate with id %s doesnt exist and cannot be deleted", certificateId));
    }

    if (!Status.unsigned().contains(certificate.status())) {
      throw new IllegalStateException(
          String.format(
              "Cannot delete certificate with id %s wrong status %s",
              certificate.id().id(), certificate.status()));
    }

    certificateRepository.remove(List.of(certificateId));

    return certificate;
  }
}
