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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.citizen.dto.GetCitizenCertificateRequest;
import se.inera.intyg.certificateservice.application.citizen.dto.GetCitizenCertificateResponse;
import se.inera.intyg.certificateservice.application.citizen.service.converter.CertificateTextConverter;
import se.inera.intyg.certificateservice.application.citizen.validation.CitizenCertificateRequestValidator;
import se.inera.intyg.certificateservice.application.common.dto.AvailableFunctionDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.citizen.service.GetCitizenCertificateDomainService;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;

@Service
@RequiredArgsConstructor
public class GetCitizenCertificateService {

  private final GetCitizenCertificateDomainService getCitizenCertificateDomainService;
  private final CertificateConverter certificateConverter;
  private final CitizenCertificateRequestValidator citizenCertificateRequestValidator;
  private final CertificateTextConverter certificateTextConverter;

  public GetCitizenCertificateResponse get(
      GetCitizenCertificateRequest getCitizenCertificateRequest, String certificateId) {
    citizenCertificateRequestValidator.validate(
        certificateId, getCitizenCertificateRequest.getPersonId());

    final var certificate =
        getCitizenCertificateDomainService.get(
            new CertificateId(certificateId),
            PersonId.builder()
                .id(getCitizenCertificateRequest.getPersonId().getId())
                .type(getCitizenCertificateRequest.getPersonId().getType().toPersonIdType())
                .build());

    final var availableFunctions = getAvailableFunctions(certificate);

    return GetCitizenCertificateResponse.builder()
        .certificate(certificateConverter.convertForCitizen(certificate, Collections.emptyList()))
        .texts(
            certificate.certificateModel().texts().stream()
                .map(certificateTextConverter::convert)
                .toList())
        .availableFunctions(availableFunctions)
        .build();
  }

  private List<AvailableFunctionDTO> getAvailableFunctions(Certificate certificate) {
    return certificate
        .certificateModel()
        .citizenAvailableFunctionsProvider()
        .of(certificate)
        .stream()
        .map(AvailableFunctionDTO::toDTO)
        .toList();
  }
}
