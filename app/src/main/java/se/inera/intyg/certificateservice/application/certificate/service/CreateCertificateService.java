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
import se.inera.intyg.certificateservice.application.certificate.dto.CreateCertificateRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.CreateCertificateResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.certificate.service.validation.CreateCertificateRequestValidator;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.common.converter.ResourceLinkConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.Xml;
import se.inera.intyg.certificateservice.domain.certificate.service.CreateCertificateDomainService;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModelId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateVersion;
import se.inera.intyg.certificateservice.domain.common.model.ExternalReference;

@Service
@RequiredArgsConstructor
public class CreateCertificateService {

  private final CreateCertificateRequestValidator createCertificateRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final CreateCertificateDomainService createCertificateDomainService;
  private final CertificateConverter certificateConverter;
  private final ResourceLinkConverter resourceLinkConverter;

  @Transactional
  public CreateCertificateResponse create(CreateCertificateRequest createCertificateRequest) {
    createCertificateRequestValidator.validate(createCertificateRequest);
    final var actionEvaluation =
        actionEvaluationFactory.create(
            createCertificateRequest.getPatient(),
            createCertificateRequest.getUser(),
            createCertificateRequest.getUnit(),
            createCertificateRequest.getCareUnit(),
            createCertificateRequest.getCareProvider());

    final var certificate =
        createCertificateDomainService.create(
            certificateModelId(createCertificateRequest),
            actionEvaluation,
            createCertificateRequest.getExternalReference() != null
                ? new ExternalReference(createCertificateRequest.getExternalReference())
                : null,
            createCertificateRequest.getPrefillXml() != null
                ? new Xml(createCertificateRequest.getPrefillXml().value())
                : null);

    return CreateCertificateResponse.builder()
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

  private static CertificateModelId certificateModelId(
      CreateCertificateRequest createCertificateRequest) {
    return CertificateModelId.builder()
        .type(new CertificateType(createCertificateRequest.getCertificateModelId().getType()))
        .version(
            new CertificateVersion(createCertificateRequest.getCertificateModelId().getVersion()))
        .build();
  }
}
