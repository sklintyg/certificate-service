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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.message.dto.SaveMessageRequest;
import se.inera.intyg.certificateservice.application.message.dto.SaveMessageResponse;
import se.inera.intyg.certificateservice.application.message.service.converter.QuestionConverter;
import se.inera.intyg.certificateservice.application.message.service.validator.SaveMessageRequestValidator;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.message.model.Content;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.model.MessageType;
import se.inera.intyg.certificateservice.domain.message.service.SaveMessageDomainService;

@Service
@RequiredArgsConstructor
public class SaveMessageService {

  private final SaveMessageRequestValidator saveMessageRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final SaveMessageDomainService saveMessageDomainService;
  private final QuestionConverter questionConverter;
  private final CertificateRepository certificateRepository;

  @Transactional
  public SaveMessageResponse save(SaveMessageRequest request, String messageId) {
    saveMessageRequestValidator.validate(request, messageId);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            request.getUser(), request.getUnit(), request.getCareUnit(), request.getCareProvider());

    final var question = request.getQuestion();
    final var messageType = MessageType.valueOf(question.getType().name());

    final var certificate =
        certificateRepository.getById(new CertificateId(question.getCertificateId()));

    final var updatedMessage =
        saveMessageDomainService.save(
            certificate,
            new MessageId(messageId),
            new Content(question.getMessage()),
            actionEvaluation,
            messageType);

    return SaveMessageResponse.builder()
        .question(
            questionConverter.convert(
                updatedMessage, updatedMessage.actions(actionEvaluation, certificate)))
        .build();
  }
}
