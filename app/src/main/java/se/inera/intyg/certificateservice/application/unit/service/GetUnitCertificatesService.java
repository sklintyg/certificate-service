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

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.common.CertificatesRequestFactory;
import se.inera.intyg.certificateservice.application.common.converter.ResourceLinkConverter;
import se.inera.intyg.certificateservice.application.unit.dto.GetUnitCertificatesRequest;
import se.inera.intyg.certificateservice.application.unit.dto.GetUnitCertificatesResponse;
import se.inera.intyg.certificateservice.application.unit.service.validator.GetUnitCertificatesRequestValidator;
import se.inera.intyg.certificateservice.domain.unit.service.GetUnitCertificatesDomainService;

@Service
@RequiredArgsConstructor
public class GetUnitCertificatesService {

  private final GetUnitCertificatesRequestValidator getUnitCertificatesRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final GetUnitCertificatesDomainService getUnitCertificatesDomainService;
  private final CertificateConverter certificateConverter;
  private final ResourceLinkConverter resourceLinkConverter;

  public GetUnitCertificatesResponse get(GetUnitCertificatesRequest getUnitCertificatesRequest) {
    getUnitCertificatesRequestValidator.validate(getUnitCertificatesRequest);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            getUnitCertificatesRequest.getPatient(),
            getUnitCertificatesRequest.getUser(),
            getUnitCertificatesRequest.getUnit(),
            getUnitCertificatesRequest.getCareUnit(),
            getUnitCertificatesRequest.getCareProvider());

    final var certificatesRequest =
        CertificatesRequestFactory.create(
            getUnitCertificatesRequest.getCertificatesQueryCriteria());

    final var certificates =
        getUnitCertificatesDomainService.get(certificatesRequest, actionEvaluation);

    return GetUnitCertificatesResponse.builder()
        .certificates(
            certificates.stream()
                .map(
                    certificate ->
                        certificateConverter.convert(
                            certificate,
                            certificate.actionsInclude(Optional.of(actionEvaluation)).stream()
                                .map(
                                    certificateAction ->
                                        resourceLinkConverter.convert(
                                            certificateAction,
                                            Optional.of(certificate),
                                            actionEvaluation))
                                .toList(),
                            actionEvaluation))
                .toList())
        .build();
  }
}
