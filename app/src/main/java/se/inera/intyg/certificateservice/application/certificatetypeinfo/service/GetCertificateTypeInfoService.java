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
package se.inera.intyg.certificateservice.application.certificatetypeinfo.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.GetCertificateTypeInfoRequest;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.GetCertificateTypeInfoResponse;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.service.converter.CertificateTypeInfoConverter;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.service.validator.CertificateTypeInfoValidator;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.domain.certificatemodel.service.ListAvailableCertificateModelsDomainService;

@Service
@RequiredArgsConstructor
public class GetCertificateTypeInfoService {

  private final CertificateTypeInfoValidator certificateTypeInfoValidator;
  private final CertificateTypeInfoConverter certificateTypeInfoConverter;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final ListAvailableCertificateModelsDomainService availableCertificateModelsDomainService;

  public GetCertificateTypeInfoResponse getActiveCertificateTypeInfos(
      GetCertificateTypeInfoRequest getCertificateTypeInfoRequest) {
    certificateTypeInfoValidator.validate(getCertificateTypeInfoRequest);
    final var actionEvaluation =
        actionEvaluationFactory.create(
            getCertificateTypeInfoRequest.getPatient(),
            getCertificateTypeInfoRequest.getUser(),
            getCertificateTypeInfoRequest.getUnit(),
            getCertificateTypeInfoRequest.getCareUnit(),
            getCertificateTypeInfoRequest.getCareProvider());

    final var certificateModels =
        availableCertificateModelsDomainService.getLatestVersions(actionEvaluation);
    return GetCertificateTypeInfoResponse.builder()
        .list(
            certificateModels.stream()
                .map(
                    certificateModel ->
                        certificateTypeInfoConverter.convert(
                            certificateModel,
                            certificateModel.actionsInclude(Optional.of(actionEvaluation)),
                            actionEvaluation))
                .toList())
        .build();
  }
}
