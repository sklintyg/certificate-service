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
import se.inera.intyg.certificateservice.application.citizen.dto.GetCitizenCertificateListRequest;
import se.inera.intyg.certificateservice.application.citizen.dto.GetCitizenCertificateListResponse;
import se.inera.intyg.certificateservice.application.citizen.validation.CitizenCertificateRequestValidator;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.model.CertificatesRequest;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;

@Service
@RequiredArgsConstructor
public class GetCitizenCertificateListService {

  private final CertificateConverter certificateConverter;
  private final CitizenCertificateRequestValidator citizenCertificateRequestValidator;
  private final CertificateRepository certificateRepository;

  public GetCitizenCertificateListResponse get(
      GetCitizenCertificateListRequest getCitizenCertificateListRequest) {
    citizenCertificateRequestValidator.validate(getCitizenCertificateListRequest.getPersonId());

    final var certificates =
        certificateRepository.findByCertificatesRequest(
            CertificatesRequest.builder()
                .personId(
                    PersonId.builder()
                        .id(getCitizenCertificateListRequest.getPersonId().getId())
                        .type(
                            getCitizenCertificateListRequest
                                .getPersonId()
                                .getType()
                                .toPersonIdType())
                        .build())
                .statuses(List.of(Status.SIGNED))
                .build());

    certificateRepository.updateCertificateMetadataFromSignInstances(certificates);

    return GetCitizenCertificateListResponse.builder()
        .citizenCertificates(
            certificates.stream()
                .filter(certificate -> certificate.certificateModel().availableForCitizen())
                .filter(
                    certificate ->
                        !certificate.certificateMetaData().patient().testIndicated().value())
                .map(
                    certificate ->
                        certificateConverter.convert(certificate, Collections.emptyList(), null))
                .toList())
        .build();
  }
}
