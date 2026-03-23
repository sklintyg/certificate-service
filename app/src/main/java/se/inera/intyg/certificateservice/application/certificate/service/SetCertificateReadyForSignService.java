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

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateReadyForSignRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateReadyForSignResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.certificate.service.validation.SetCertificateReadyForSignRequestValidator;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.common.converter.ResourceLinkConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.service.SetCertificateReadyForSignDomainService;

@Service
@RequiredArgsConstructor
public class SetCertificateReadyForSignService {

  private final SetCertificateReadyForSignDomainService setCertificateReadyForSignDomainService;
  private final SetCertificateReadyForSignRequestValidator
      setCertificateReadyForSignRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final CertificateConverter certificateConverter;
  private final ResourceLinkConverter resourceLinkConverter;

  @Transactional
  public CertificateReadyForSignResponse set(
      CertificateReadyForSignRequest request, String certificateId) {
    setCertificateReadyForSignRequestValidator.validate(request, certificateId);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            request.getUser(), request.getUnit(), request.getCareUnit(), request.getCareProvider());

    final var certificate =
        setCertificateReadyForSignDomainService.readyForSign(
            new CertificateId(certificateId), actionEvaluation);

    return CertificateReadyForSignResponse.builder()
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
