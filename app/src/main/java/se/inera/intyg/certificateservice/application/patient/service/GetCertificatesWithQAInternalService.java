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
package se.inera.intyg.certificateservice.application.patient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificatesWithQAInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificatesWithQAInternalResponse;
import se.inera.intyg.certificateservice.application.patient.service.validator.CertificatesWithQARequestValidator;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.patient.service.GetCertificatesWithQAInternalDomainService;

@Service
@RequiredArgsConstructor
public class GetCertificatesWithQAInternalService {

  private final CertificatesWithQARequestValidator certificatesWithQARequestValidator;
  private final GetCertificatesWithQAInternalDomainService
      getCertificatesWithQAInternalDomainService;

  public CertificatesWithQAInternalResponse get(CertificatesWithQAInternalRequest request) {
    certificatesWithQARequestValidator.validate(request);

    final var xml =
        getCertificatesWithQAInternalDomainService.get(
            request.getCertificateIds().stream().map(CertificateId::new).toList());

    return CertificatesWithQAInternalResponse.builder().list(xml.base64()).build();
  }
}
