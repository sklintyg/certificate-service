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
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PlaceholderCertificateRequest;
import se.inera.intyg.certificateservice.domain.certificatemodel.repository.CertificateModelRepository;
import se.inera.intyg.certificateservice.domain.common.model.ExternalReference;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEvent;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEventType;
import se.inera.intyg.certificateservice.domain.event.service.CertificateEventDomainService;

@RequiredArgsConstructor
public class RenewExternalCertificateDomainService {

  private final CertificateModelRepository certificateModelRepository;
  private final CertificateRepository certificateRepository;
  private final CertificateEventDomainService certificateEventDomainService;
  private final PrefillProcessor prefillProcessor;

  public Certificate renew(
      ActionEvaluation actionEvaluation,
      ExternalReference externalReference,
      PlaceholderCertificateRequest request) {
    final var start = LocalDateTime.now(ZoneId.systemDefault());

    final var certificateModel =
        certificateModelRepository.getActiveById(request.certificateModelId());
    final var certificate = certificateRepository.createFromPlaceholder(request, certificateModel);

    certificate.updateMetadata(actionEvaluation);
    certificate.externalReference(externalReference);
    certificate.prefill(request.prefillXml(), prefillProcessor);

    final var savedCertificate = certificateRepository.save(certificate);

    certificateEventDomainService.publish(
        CertificateEvent.builder()
            .type(CertificateEventType.RENEW)
            .start(start)
            .end(LocalDateTime.now(ZoneId.systemDefault()))
            .certificate(savedCertificate)
            .actionEvaluation(actionEvaluation)
            .build());

    return savedCertificate;
  }
}
