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

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.common.converter.ResourceLinkConverter;
import se.inera.intyg.certificateservice.application.message.dto.GetCertificateFromMessageRequest;
import se.inera.intyg.certificateservice.application.message.dto.GetCertificateFromMessageResponse;
import se.inera.intyg.certificateservice.application.message.service.validator.GetCertificateFromMessageRequestValidator;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.service.GetCertificateDomainService;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.repository.MessageRepository;

@Service
@RequiredArgsConstructor
public class GetCertificateFromMessageService {

  private final ActionEvaluationFactory actionEvaluationFactory;
  private final GetCertificateFromMessageRequestValidator certificateFromMessageRequestValidator;
  private final GetCertificateDomainService getCertificateDomainService;
  private final CertificateConverter certificateConverter;
  private final ResourceLinkConverter resourceLinkConverter;
  private final MessageRepository messageRepository;

  public GetCertificateFromMessageResponse get(
      GetCertificateFromMessageRequest getCertificateRequest, String messageId) {
    certificateFromMessageRequestValidator.validate(getCertificateRequest, messageId);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            getCertificateRequest.getUser(),
            getCertificateRequest.getUnit(),
            getCertificateRequest.getCareUnit(),
            getCertificateRequest.getCareProvider());

    final var message = messageRepository.getById(new MessageId(messageId));

    final var certificate =
        getCertificateDomainService.get(
            new CertificateId(message.certificateId().id()), actionEvaluation);

    return GetCertificateFromMessageResponse.builder()
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
