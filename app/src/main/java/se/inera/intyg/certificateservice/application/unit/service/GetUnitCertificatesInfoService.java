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
package se.inera.intyg.certificateservice.application.unit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.dto.StaffDTO;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.common.CertificatesRequestFactory;
import se.inera.intyg.certificateservice.application.unit.dto.GetUnitCertificatesInfoRequest;
import se.inera.intyg.certificateservice.application.unit.dto.GetUnitCertificatesInfoResponse;
import se.inera.intyg.certificateservice.application.unit.service.validator.GetUnitCertificatesInfoRequestValidator;
import se.inera.intyg.certificateservice.domain.unit.service.GetUnitCertificatesInfoDomainService;

@Service
@RequiredArgsConstructor
public class GetUnitCertificatesInfoService {

  private final GetUnitCertificatesInfoRequestValidator getUnitCertificatesInfoRequestValidator;
  private final GetUnitCertificatesInfoDomainService getUnitCertificatesInfoDomainService;
  private final ActionEvaluationFactory actionEvaluationFactory;

  public GetUnitCertificatesInfoResponse get(
      GetUnitCertificatesInfoRequest getUnitCertificatesInfoRequest) {
    getUnitCertificatesInfoRequestValidator.validate(getUnitCertificatesInfoRequest);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            getUnitCertificatesInfoRequest.getUser(),
            getUnitCertificatesInfoRequest.getUnit(),
            getUnitCertificatesInfoRequest.getCareUnit(),
            getUnitCertificatesInfoRequest.getCareProvider());

    final var staffs =
        getUnitCertificatesInfoDomainService.get(
            CertificatesRequestFactory.create(), actionEvaluation);

    return GetUnitCertificatesInfoResponse.builder()
        .staffs(
            staffs.stream()
                .map(
                    staff ->
                        StaffDTO.builder()
                            .personId(staff.hsaId().id())
                            .firstName(staff.name().firstName())
                            .middleName(staff.name().middleName())
                            .lastName(staff.name().lastName())
                            .fullName(staff.name().fullName())
                            .build())
                .toList())
        .build();
  }
}
