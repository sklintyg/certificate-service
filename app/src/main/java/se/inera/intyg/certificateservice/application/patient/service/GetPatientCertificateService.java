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

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.common.converter.ResourceLinkConverter;
import se.inera.intyg.certificateservice.application.patient.dto.GetPatientCertificatesRequest;
import se.inera.intyg.certificateservice.application.patient.dto.GetPatientCertificatesResponse;
import se.inera.intyg.certificateservice.application.patient.service.validator.GetPatientCertificatesRequestValidator;
import se.inera.intyg.certificateservice.domain.patient.service.GetPatientCertificatesDomainService;

@Service
@RequiredArgsConstructor
public class GetPatientCertificateService {

  private final GetPatientCertificatesRequestValidator getPatientCertificatesRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final GetPatientCertificatesDomainService getPatientCertificatesDomainService;
  private final CertificateConverter certificateConverter;
  private final ResourceLinkConverter resourceLinkConverter;

  public GetPatientCertificatesResponse get(
      GetPatientCertificatesRequest getPatientCertificatesRequest) {
    getPatientCertificatesRequestValidator.validate(getPatientCertificatesRequest);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            getPatientCertificatesRequest.getPatient(),
            getPatientCertificatesRequest.getUser(),
            getPatientCertificatesRequest.getUnit(),
            getPatientCertificatesRequest.getCareUnit(),
            getPatientCertificatesRequest.getCareProvider());

    final var certificates = getPatientCertificatesDomainService.get(actionEvaluation);

    return GetPatientCertificatesResponse.builder()
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
