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
import se.inera.intyg.certificateservice.application.certificate.dto.DeleteCertificateRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.DeleteCertificateResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.certificate.service.validation.DeleteCertificateRequestValidator;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.common.converter.ResourceLinkConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Revision;
import se.inera.intyg.certificateservice.domain.certificate.service.DeleteCertificateDomainService;

@Service
@RequiredArgsConstructor
public class DeleteCertificateService {

  private final DeleteCertificateRequestValidator deleteCertificateRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final DeleteCertificateDomainService deleteCertificateDomainService;
  private final CertificateConverter certificateConverter;
  private final ResourceLinkConverter resourceLinkConverter;

  @Transactional
  public DeleteCertificateResponse delete(
      DeleteCertificateRequest deleteCertificateRequest, String certificateId, Long version) {
    deleteCertificateRequestValidator.validate(deleteCertificateRequest, certificateId, version);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            deleteCertificateRequest.getUser(),
            deleteCertificateRequest.getUnit(),
            deleteCertificateRequest.getCareUnit(),
            deleteCertificateRequest.getCareProvider());

    final var certificate =
        deleteCertificateDomainService.delete(
            new CertificateId(certificateId), new Revision(version), actionEvaluation);

    return DeleteCertificateResponse.builder()
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
