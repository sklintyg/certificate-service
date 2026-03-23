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
package se.inera.intyg.certificateservice.application.message.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.message.dto.GetCertificateMessageRequest;
import se.inera.intyg.certificateservice.application.message.dto.GetCertificateMessageResponse;
import se.inera.intyg.certificateservice.application.message.service.converter.QuestionConverter;
import se.inera.intyg.certificateservice.application.message.service.validator.GetCertificateMessageRequestValidator;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.service.GetCertificateDomainService;

@Service
@RequiredArgsConstructor
public class GetCertificateMessageService {

  private final GetCertificateMessageRequestValidator getCertificateMessageRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final GetCertificateDomainService getCertificateDomainService;
  private final QuestionConverter questionConverter;

  public GetCertificateMessageResponse get(
      GetCertificateMessageRequest getCertificateMessageRequest, String certificateId) {
    getCertificateMessageRequestValidator.validate(getCertificateMessageRequest, certificateId);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            getCertificateMessageRequest.getPatient(),
            getCertificateMessageRequest.getUser(),
            getCertificateMessageRequest.getUnit(),
            getCertificateMessageRequest.getCareUnit(),
            getCertificateMessageRequest.getCareProvider());

    final var certificate =
        getCertificateDomainService.get(new CertificateId(certificateId), actionEvaluation);

    return GetCertificateMessageResponse.builder()
        .questions(
            certificate.messages().stream()
                .map(
                    message ->
                        questionConverter.convert(
                            message, message.actions(actionEvaluation, certificate)))
                .toList())
        .build();
  }
}
