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
package se.inera.intyg.certificateservice.domain.citizen.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.exception.CitizenCertificateForbidden;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEvent;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEventType;
import se.inera.intyg.certificateservice.domain.event.service.CertificateEventDomainService;

@RequiredArgsConstructor
public class SendCitizenCertificateDomainService {

  private final CertificateRepository certificateRepository;
  private final CertificateEventDomainService certificateEventDomainService;

  public Certificate send(CertificateId certificateId, PersonId personId) {
    final var start = LocalDateTime.now(ZoneId.systemDefault());
    final var certificate = certificateRepository.getById(certificateId);

    if (!certificate.isCertificateIssuedOnPatient(personId)) {
      throw new CitizenCertificateForbidden(
          "Citizen is trying to send certificate with id '%s', but it is not issued on citizen"
              .formatted(certificateId));
    }
    if (Boolean.FALSE.equals(certificate.certificateModel().availableForCitizen())) {
      throw new CitizenCertificateForbidden(
          "Citizen is trying to send certificate with id '%s', but it is not available for citizen"
              .formatted(certificateId));
    }

    certificate.sendByCitizen();

    final var sentCertificate = certificateRepository.save(certificate);

    certificateEventDomainService.publish(
        CertificateEvent.builder()
            .type(CertificateEventType.SENT)
            .start(start)
            .end(LocalDateTime.now(ZoneId.systemDefault()))
            .certificate(sentCertificate)
            .actionEvaluation(null)
            .build());

    return sentCertificate;
  }
}
