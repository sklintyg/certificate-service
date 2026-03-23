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
import se.inera.intyg.certificateservice.application.certificate.dto.UpdateCertificateRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.UpdateCertificateResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.certificate.service.converter.ElementCertificateConverter;
import se.inera.intyg.certificateservice.application.certificate.service.validation.UpdateCertificateRequestValidator;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.common.converter.ResourceLinkConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Revision;
import se.inera.intyg.certificateservice.domain.certificate.service.UpdateCertificateDomainService;
import se.inera.intyg.certificateservice.domain.common.model.ExternalReference;

@Service
@RequiredArgsConstructor
public class UpdateCertificateService {

  private final UpdateCertificateRequestValidator updateCertificateRequestValidator;
  private final UpdateCertificateDomainService updateCertificateDomainService;
  private final ElementCertificateConverter elementCertificateConverter;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final CertificateConverter certificateConverter;
  private final ResourceLinkConverter resourceLinkConverter;

  @Transactional
  public UpdateCertificateResponse update(
      UpdateCertificateRequest updateCertificateRequest, String certificateId) {
    updateCertificateRequestValidator.validate(updateCertificateRequest, certificateId);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            updateCertificateRequest.getPatient(),
            updateCertificateRequest.getUser(),
            updateCertificateRequest.getUnit(),
            updateCertificateRequest.getCareUnit(),
            updateCertificateRequest.getCareProvider());

    final var elementData =
        elementCertificateConverter.convert(updateCertificateRequest.getCertificate());

    final var updatedCertificate =
        updateCertificateDomainService.update(
            new CertificateId(certificateId),
            elementData,
            actionEvaluation,
            new Revision(updateCertificateRequest.getCertificate().getMetadata().getVersion()),
            updateCertificateRequest.getExternalReference() != null
                ? new ExternalReference(updateCertificateRequest.getExternalReference())
                : null);

    return UpdateCertificateResponse.builder()
        .certificate(
            certificateConverter.convert(
                updatedCertificate,
                updatedCertificate.actionsInclude(Optional.of(actionEvaluation)).stream()
                    .map(
                        certificateAction ->
                            resourceLinkConverter.convert(
                                certificateAction,
                                Optional.of(updatedCertificate),
                                actionEvaluation))
                    .toList(),
                actionEvaluation))
        .build();
  }
}
