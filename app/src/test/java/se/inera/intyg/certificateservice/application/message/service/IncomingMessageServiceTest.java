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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.certificateservice.application.testdata.TestDataIncomingMessage.INCOMING_ANSWER_MESSAGE;
import static se.inera.intyg.certificateservice.application.testdata.TestDataIncomingMessage.INCOMING_COMPLEMENT_MESSAGE;
import static se.inera.intyg.certificateservice.application.testdata.TestDataIncomingMessage.INCOMING_QUESTION_MESSAGE;
import static se.inera.intyg.certificateservice.application.testdata.TestDataIncomingMessage.INCOMING_REMINDER_MESSAGE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessage.ANSWER;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessage.COMPLEMENT_MESSAGE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessage.CONTACT_MESSAGE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessage.REMINDER;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessageConstants.MESSAGE_ID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.message.dto.IncomingMessageRequest;
import se.inera.intyg.certificateservice.application.message.service.converter.MessageConverter;
import se.inera.intyg.certificateservice.application.message.service.validator.IncomingMessageValidator;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.service.ReceiveAnswerMessageDomainService;
import se.inera.intyg.certificateservice.domain.message.service.ReceiveComplementMessageDomainService;
import se.inera.intyg.certificateservice.domain.message.service.ReceiveQuestionMessageDomainService;
import se.inera.intyg.certificateservice.domain.message.service.ReceiveReminderMessageDomainService;

@ExtendWith(MockitoExtension.class)
class IncomingMessageServiceTest {

  @Mock private ReceiveQuestionMessageDomainService receiveQuestionMessageDomainService;
  @Mock private ReceiveAnswerMessageDomainService receiveAnswerMessageDomainService;
  @Mock private ReceiveReminderMessageDomainService receiveReminderMessageDomainService;
  @Mock private IncomingMessageValidator incomingMessageValidator;

  @Mock private MessageConverter messageConverter;

  @Mock private ReceiveComplementMessageDomainService receiveComplementMessageDomainService;

  @InjectMocks private IncomingMessageService incomingMessageService;

  @Test
  void shallThrowExceptionIfRequestIsNotValid() {
    final var invalidMessage = IncomingMessageRequest.builder().build();

    doThrow(new IllegalArgumentException()).when(incomingMessageValidator).validate(invalidMessage);

    assertThrows(
        IllegalArgumentException.class, () -> incomingMessageService.receive(invalidMessage));
  }

  @Test
  void shallReceiveComplementMessages() {
    doReturn(COMPLEMENT_MESSAGE).when(messageConverter).convert(INCOMING_COMPLEMENT_MESSAGE);
    incomingMessageService.receive(INCOMING_COMPLEMENT_MESSAGE);
    verify(receiveComplementMessageDomainService).receive(COMPLEMENT_MESSAGE);
  }

  @Test
  void shallReceiveReminderMessages() {
    doReturn(REMINDER).when(messageConverter).convertReminder(INCOMING_REMINDER_MESSAGE);
    incomingMessageService.receive(INCOMING_REMINDER_MESSAGE);
    verify(receiveReminderMessageDomainService).receive(new MessageId(MESSAGE_ID), REMINDER);
  }

  @Test
  void shallReceiveAnswerMessages() {
    doReturn(ANSWER).when(messageConverter).convertAnswer(INCOMING_ANSWER_MESSAGE);
    incomingMessageService.receive(INCOMING_ANSWER_MESSAGE);
    verify(receiveAnswerMessageDomainService).receive(new MessageId(MESSAGE_ID), ANSWER);
  }

  @Test
  void shallReceiveQuestionMessages() {
    doReturn(CONTACT_MESSAGE).when(messageConverter).convert(INCOMING_QUESTION_MESSAGE);
    incomingMessageService.receive(INCOMING_QUESTION_MESSAGE);
    verify(receiveQuestionMessageDomainService).receive(CONTACT_MESSAGE);
  }
}
