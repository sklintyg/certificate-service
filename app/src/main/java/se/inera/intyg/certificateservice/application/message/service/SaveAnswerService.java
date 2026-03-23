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
import se.inera.intyg.certificateservice.application.message.dto.SaveAnswerRequest;
import se.inera.intyg.certificateservice.application.message.dto.SaveAnswerResponse;
import se.inera.intyg.certificateservice.application.message.service.converter.QuestionConverter;
import se.inera.intyg.certificateservice.application.message.service.validator.SaveAnswerRequestValidator;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.message.model.Content;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.repository.MessageRepository;
import se.inera.intyg.certificateservice.domain.message.service.SaveAnswerDomainService;

@Service
@RequiredArgsConstructor
public class SaveAnswerService {

  private final SaveAnswerRequestValidator saveAnswerRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final SaveAnswerDomainService saveAnswerDomainService;
  private final QuestionConverter questionConverter;
  private final CertificateRepository certificateRepository;
  private final MessageRepository messageRepository;

  @Transactional
  public SaveAnswerResponse save(SaveAnswerRequest request, String messageId) {
    saveAnswerRequestValidator.validate(request, messageId);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            request.getUser(), request.getUnit(), request.getCareUnit(), request.getCareProvider());

    final var message = messageRepository.getById(new MessageId(messageId));

    final var certificate = certificateRepository.getById(message.certificateId());

    final var messageWithAnswer =
        saveAnswerDomainService.save(
            message, certificate, actionEvaluation, new Content(request.getContent()));

    return SaveAnswerResponse.builder()
        .question(
            questionConverter.convert(
                messageWithAnswer, messageWithAnswer.actions(actionEvaluation, certificate)))
        .build();
  }
}
