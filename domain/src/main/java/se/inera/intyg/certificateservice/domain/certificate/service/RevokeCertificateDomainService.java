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

import static se.inera.intyg.certificateservice.domain.certificate.model.RelationType.COMPLEMENT;

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
import se.inera.intyg.certificateservice.domain.common.model.RevokedInformation;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEvent;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEventType;
import se.inera.intyg.certificateservice.domain.event.service.CertificateEventDomainService;
import se.inera.intyg.certificateservice.domain.message.model.MessageStatus;
import se.inera.intyg.certificateservice.domain.message.model.MessageType;
import se.inera.intyg.certificateservice.domain.message.service.SetMessagesToHandleDomainService;
import se.inera.intyg.certificateservice.domain.message.service.SetMessagesToUnhandledDomainService;

@RequiredArgsConstructor
public class RevokeCertificateDomainService {

  private final CertificateRepository certificateRepository;
  private final CertificateEventDomainService certificateEventDomainService;
  private final SetMessagesToHandleDomainService setMessagesToHandleDomainService;
  private final SetMessagesToUnhandledDomainService setMessagesToUnhandledDomainService;

  public Certificate revoke(
      CertificateId certificateId,
      ActionEvaluation actionEvaluation,
      RevokedInformation revokedInformation) {
    final var start = LocalDateTime.now(ZoneId.systemDefault());

    final var certificate = certificateRepository.getById(certificateId);
    if (!certificate.allowTo(CertificateActionType.REVOKE, Optional.of(actionEvaluation))) {
      throw new CertificateActionForbidden(
          "Not allowed to revoke certificate for %s".formatted(certificateId),
          certificate.reasonNotAllowed(
              CertificateActionType.REVOKE, Optional.of(actionEvaluation)));
    }

    certificate.revoke(actionEvaluation, revokedInformation);

    final var revokedCertificate = certificateRepository.save(certificate);

    setMessagesToHandleDomainService.handle(revokedCertificate.messages());

    if (revokedCertificate.hasParent(COMPLEMENT)) {
      setMessagesToUnhandledDomainService.unhandled(
          revokedCertificate.parent().certificate().messages().stream()
              .filter(message -> message.type() == MessageType.COMPLEMENT)
              .filter(message -> MessageStatus.HANDLED.equals(message.status()))
              .toList());
    }

    certificateEventDomainService.publish(
        CertificateEvent.builder()
            .type(CertificateEventType.REVOKED)
            .start(start)
            .end(LocalDateTime.now(ZoneId.systemDefault()))
            .certificate(revokedCertificate)
            .actionEvaluation(actionEvaluation)
            .build());

    return revokedCertificate;
  }
}
