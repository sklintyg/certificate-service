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

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.certificateservice.application.certificate.dto.ReplaceCertificateRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.ReplaceCertificateResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.certificate.service.validation.ReplaceCertificateRequestValidator;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.common.converter.ResourceLinkConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.service.ReplaceCertificateDomainService;
import se.inera.intyg.certificateservice.domain.common.model.ExternalReference;

@Service
@RequiredArgsConstructor
public class ReplaceCertificateService {

  private final ReplaceCertificateRequestValidator replaceCertificateRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final ReplaceCertificateDomainService replaceCertificateDomainService;
  private final CertificateConverter certificateConverter;
  private final ResourceLinkConverter resourceLinkConverter;

  @Transactional
  public ReplaceCertificateResponse replace(
      ReplaceCertificateRequest replaceCertificateRequest, String certificateId) {
    replaceCertificateRequestValidator.validate(replaceCertificateRequest, certificateId);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            replaceCertificateRequest.getPatient(),
            replaceCertificateRequest.getUser(),
            replaceCertificateRequest.getUnit(),
            replaceCertificateRequest.getCareUnit(),
            replaceCertificateRequest.getCareProvider());

    final var certificate =
        replaceCertificateDomainService.replace(
            new CertificateId(certificateId),
            actionEvaluation,
            replaceCertificateRequest.getExternalReference() != null
                ? new ExternalReference(replaceCertificateRequest.getExternalReference())
                : null);

    return ReplaceCertificateResponse.builder()
        .certificate(
            certificateConverter.convert(
                certificate,
                certificate.actionsInclude(Optional.of(actionEvaluation)).stream()
                    .map(
                        certificateAction ->
                            resourceLinkConverter.convert(
                                certificateAction, Optional.of(certificate), actionEvaluation))
                    .toList(),
                actionEvaluation))
        .build();
  }
}
