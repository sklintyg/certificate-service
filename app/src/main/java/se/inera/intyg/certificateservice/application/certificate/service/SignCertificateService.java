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
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.dto.SignCertificateRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.SignCertificateResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.certificate.service.validation.SignCertificateRequestValidator;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.common.converter.ResourceLinkConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Revision;
import se.inera.intyg.certificateservice.domain.certificate.model.Signature;
import se.inera.intyg.certificateservice.domain.certificate.service.SignCertificateDomainService;

@Service
@RequiredArgsConstructor
public class SignCertificateService {

  private final ActionEvaluationFactory actionEvaluationFactory;
  private final SignCertificateRequestValidator signCertificateRequestValidator;
  private final CertificateConverter certificateConverter;
  private final ResourceLinkConverter resourceLinkConverter;
  private final SignCertificateDomainService signCertificateDomainService;

  @Transactional
  public SignCertificateResponse sign(
      SignCertificateRequest signCertificateRequest, String certificateId, Long version) {
    signCertificateRequestValidator.validate(signCertificateRequest, certificateId, version);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            signCertificateRequest.getUser(),
            signCertificateRequest.getUnit(),
            signCertificateRequest.getCareUnit(),
            signCertificateRequest.getCareProvider());

    final var signedCertificate =
        signCertificateDomainService.sign(
            new CertificateId(certificateId),
            new Revision(version),
            new Signature(
                new String(
                    Base64.getDecoder().decode(signCertificateRequest.getSignatureXml()),
                    StandardCharsets.UTF_8)),
            actionEvaluation);

    return SignCertificateResponse.builder()
        .certificate(
            certificateConverter.convert(
                signedCertificate,
                signedCertificate.actionsInclude(Optional.of(actionEvaluation)).stream()
                    .map(
                        certificateAction ->
                            resourceLinkConverter.convert(
                                certificateAction,
                                Optional.of(signedCertificate),
                                actionEvaluation))
                    .toList(),
                actionEvaluation))
        .build();
  }
}
