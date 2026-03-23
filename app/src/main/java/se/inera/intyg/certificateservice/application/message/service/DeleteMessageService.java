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
import se.inera.intyg.certificateservice.application.message.dto.DeleteMessageRequest;
import se.inera.intyg.certificateservice.application.message.service.validator.DeleteMessageRequestValidator;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.service.DeleteMessageDomainService;

@Service
@RequiredArgsConstructor
public class DeleteMessageService {

  private final DeleteMessageRequestValidator deleteMessageRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final DeleteMessageDomainService deleteMessageDomainService;

  @Transactional
  public void delete(DeleteMessageRequest request, String messageId) {
    deleteMessageRequestValidator.validate(request, messageId);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            request.getUser(), request.getUnit(), request.getCareUnit(), request.getCareProvider());

    deleteMessageDomainService.delete(new MessageId(messageId), actionEvaluation);
  }
}
