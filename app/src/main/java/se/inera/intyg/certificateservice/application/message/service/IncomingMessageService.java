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
import se.inera.intyg.certificateservice.application.message.dto.IncomingMessageRequest;
import se.inera.intyg.certificateservice.application.message.service.converter.MessageConverter;
import se.inera.intyg.certificateservice.application.message.service.validator.IncomingMessageValidator;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.service.ReceiveAnswerMessageDomainService;
import se.inera.intyg.certificateservice.domain.message.service.ReceiveComplementMessageDomainService;
import se.inera.intyg.certificateservice.domain.message.service.ReceiveQuestionMessageDomainService;
import se.inera.intyg.certificateservice.domain.message.service.ReceiveReminderMessageDomainService;

@Service
@RequiredArgsConstructor
public class IncomingMessageService {

  private final IncomingMessageValidator incomingMessageValidator;
  private final MessageConverter messageConverter;
  private final ReceiveComplementMessageDomainService receiveComplementMessageDomainService;
  private final ReceiveReminderMessageDomainService receiveReminderMessageDomainService;
  private final ReceiveQuestionMessageDomainService receiveQuestionMessageDomainService;
  private final ReceiveAnswerMessageDomainService receiveAnswerMessageDomainService;

  @Transactional
  public void receive(IncomingMessageRequest incomingMessageRequest) {
    incomingMessageValidator.validate(incomingMessageRequest);

    switch (incomingMessageRequest.getType()) {
      case KOMPLT ->
          receiveComplementMessageDomainService.receive(
              messageConverter.convert(incomingMessageRequest));
      case PAMINN ->
          receiveReminderMessageDomainService.receive(
              new MessageId(incomingMessageRequest.getReminderMessageId()),
              messageConverter.convertReminder(incomingMessageRequest));
      default -> {
        if (isAnswer(incomingMessageRequest)) {
          receiveAnswerMessageDomainService.receive(
              new MessageId(incomingMessageRequest.getAnswerMessageId()),
              messageConverter.convertAnswer(incomingMessageRequest));
        } else {
          receiveQuestionMessageDomainService.receive(
              messageConverter.convert(incomingMessageRequest));
        }
      }
    }
  }

  private static boolean isAnswer(IncomingMessageRequest incomingMessageRequest) {
    return incomingMessageRequest.getAnswerMessageId() != null;
  }
}
