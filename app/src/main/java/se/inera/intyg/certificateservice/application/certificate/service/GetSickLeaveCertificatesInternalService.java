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
import se.inera.intyg.certificateservice.application.certificate.dto.GetSickLeaveCertificatesInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.GetSickLeaveCertificatesInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.SickLeaveConverter;
import se.inera.intyg.certificateservice.application.common.CertificatesRequestFactory;
import se.inera.intyg.certificateservice.domain.certificate.service.GetSickLeaveCertificatesDomainService;

@Service
@RequiredArgsConstructor
public class GetSickLeaveCertificatesInternalService {

  private final GetSickLeaveCertificatesDomainService getSickLeaveCertificatesDomainService;
  private final SickLeaveConverter sickLeaveConverter;

  public GetSickLeaveCertificatesInternalResponse get(
      GetSickLeaveCertificatesInternalRequest request) {
    if (request.getPersonId() == null
        || request.getPersonId().getId() == null
        || request.getPersonId().getId().isBlank()) {
      throw new IllegalArgumentException("Patient id cannot be null or empty");
    }

    final var sickLeaves =
        getSickLeaveCertificatesDomainService.get(CertificatesRequestFactory.convert(request));

    return GetSickLeaveCertificatesInternalResponse.builder()
        .certificates(
            sickLeaves.stream().map(sickLeaveConverter::toSickLeaveCertificateItem).toList())
        .build();
  }
}
