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

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificatePdfRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificatePdfResponse;
import se.inera.intyg.certificateservice.application.certificate.service.validation.GetCertificatePdfRequestValidator;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.service.GetCertificatePdfDomainService;

@Service
@RequiredArgsConstructor
public class GetCertificatePdfService {

  private final ActionEvaluationFactory actionEvaluationFactory;
  private final GetCertificatePdfRequestValidator getCertificatePdfRequestValidator;
  private final GetCertificatePdfDomainService getCertificatePdfDomainService;

  public GetCertificatePdfResponse get(GetCertificatePdfRequest request, String certificateId) {
    getCertificatePdfRequestValidator.validate(request, certificateId);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            request.getPatient(),
            request.getUser(),
            request.getUnit(),
            request.getCareUnit(),
            request.getCareProvider());

    final var certificatePdf =
        getCertificatePdfDomainService.get(
            new CertificateId(certificateId), actionEvaluation, request.getAdditionalInfoText());

    return GetCertificatePdfResponse.builder()
        .pdfData(certificatePdf.pdfData())
        .fileName(certificatePdf.fileName())
        .build();
  }
}
