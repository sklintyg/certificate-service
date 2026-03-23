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
import se.inera.intyg.certificateservice.application.message.dto.CreateMessageRequest;
import se.inera.intyg.certificateservice.application.message.dto.CreateMessageResponse;
import se.inera.intyg.certificateservice.application.message.service.converter.QuestionConverter;
import se.inera.intyg.certificateservice.application.message.service.validator.CreateMessageRequestValidator;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.message.model.Content;
import se.inera.intyg.certificateservice.domain.message.model.MessageType;
import se.inera.intyg.certificateservice.domain.message.service.CreateMessageDomainService;

@Service
@RequiredArgsConstructor
public class CreateMessageService {

  private final CreateMessageRequestValidator createMessageRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final CreateMessageDomainService createMessageDomainService;
  private final QuestionConverter questionConverter;
  private final CertificateRepository certificateRepository;

  @Transactional
  public CreateMessageResponse create(CreateMessageRequest request, String certificateId) {
    createMessageRequestValidator.validate(request, certificateId);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            request.getPatient(),
            request.getUser(),
            request.getUnit(),
            request.getCareUnit(),
            request.getCareProvider());

    final var certificate = certificateRepository.getById(new CertificateId(certificateId));

    final var createdQuestion =
        createMessageDomainService.create(
            certificate,
            actionEvaluation,
            MessageType.valueOf(request.getQuestionType().name()),
            new Content(request.getMessage()));

    return CreateMessageResponse.builder()
        .question(
            questionConverter.convert(
                createdQuestion, createdQuestion.actions(actionEvaluation, certificate)))
        .build();
  }
}
