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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.exception.CertificateActionForbidden;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEvent;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEventType;
import se.inera.intyg.certificateservice.domain.event.service.CertificateEventDomainService;

@RequiredArgsConstructor
public class SendCertificateDomainService {

  private final CertificateRepository certificateRepository;
  private final CertificateEventDomainService certificateEventDomainService;

  public Certificate send(CertificateId certificateId, ActionEvaluation actionEvaluation) {
    final var start = LocalDateTime.now(ZoneId.systemDefault());

    final var certificate = certificateRepository.getById(certificateId);
    if (!certificate.allowTo(CertificateActionType.SEND, Optional.of(actionEvaluation))) {
      throw new CertificateActionForbidden(
          "Not allowed to send certificate for %s".formatted(certificateId),
          certificate.reasonNotAllowed(CertificateActionType.SEND, Optional.of(actionEvaluation)));
    }

    certificate.send(actionEvaluation);

    final var sentCertificate = certificateRepository.save(certificate);

    certificateEventDomainService.publish(
        CertificateEvent.builder()
            .type(CertificateEventType.SENT)
            .start(start)
            .end(LocalDateTime.now(ZoneId.systemDefault()))
            .certificate(sentCertificate)
            .actionEvaluation(actionEvaluation)
            .build());

    return sentCertificate;
  }
}
