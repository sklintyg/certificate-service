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
package se.inera.intyg.certificateservice.application.citizen.service;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.citizen.dto.SendCitizenCertificateRequest;
import se.inera.intyg.certificateservice.application.citizen.dto.SendCitizenCertificateResponse;
import se.inera.intyg.certificateservice.application.citizen.validation.CitizenCertificateRequestValidator;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.citizen.service.SendCitizenCertificateDomainService;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;

@Service
@RequiredArgsConstructor
public class SendCitizenCertificateService {

  private final SendCitizenCertificateDomainService sendCitizenCertificateDomainService;
  private final CitizenCertificateRequestValidator citizenCertificateRequestValidator;
  private final CertificateConverter certificateConverter;

  public SendCitizenCertificateResponse send(
      SendCitizenCertificateRequest request, String certificateId) {
    citizenCertificateRequestValidator.validate(certificateId, request.getPersonId());
    final var certificate =
        sendCitizenCertificateDomainService.send(
            new CertificateId(certificateId),
            PersonId.builder()
                .id(request.getPersonId().getId())
                .type(request.getPersonId().getType().toPersonIdType())
                .build());

    return SendCitizenCertificateResponse.builder()
        .citizenCertificate(
            certificateConverter.convertForCitizen(certificate, Collections.emptyList()))
        .build();
  }
}
