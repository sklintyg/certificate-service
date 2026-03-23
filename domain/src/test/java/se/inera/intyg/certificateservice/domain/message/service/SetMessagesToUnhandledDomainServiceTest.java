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
package se.inera.intyg.certificateservice.domain.message.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessage.complementMessageBuilder;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.model.MessageStatus;
import se.inera.intyg.certificateservice.domain.message.repository.MessageRepository;

@ExtendWith(MockitoExtension.class)
class SetMessagesToUnhandledDomainServiceTest {

  @Mock private MessageRepository messageRepository;

  @InjectMocks private SetMessagesToUnhandledDomainService setMessagesToHandleDomainService;

  @Test
  void shallUnhandleMessage() {
    final var messageToHandle = complementMessageBuilder().status(MessageStatus.HANDLED).build();

    setMessagesToHandleDomainService.unhandled(List.of(messageToHandle));

    assertEquals(MessageStatus.SENT, messageToHandle.status());
  }

  @Test
  void shallSaveUnhandledMessage() {
    final var messageToHandle = complementMessageBuilder().status(MessageStatus.HANDLED).build();

    setMessagesToHandleDomainService.unhandled(List.of(messageToHandle));

    verify(messageRepository).save(messageToHandle);
  }

  @Test
  void shallUnhandleMessages() {
    final var messagesToHandle =
        List.of(
            complementMessageBuilder()
                .id(new MessageId("firstMessage"))
                .status(MessageStatus.HANDLED)
                .build(),
            complementMessageBuilder()
                .id(new MessageId("secondMessage"))
                .status(MessageStatus.HANDLED)
                .build());

    setMessagesToHandleDomainService.unhandled(messagesToHandle);

    assertAll(
        () -> assertEquals(MessageStatus.SENT, messagesToHandle.getFirst().status()),
        () -> assertEquals(MessageStatus.SENT, messagesToHandle.getLast().status()));
  }

  @Test
  void shallSaveMessages() {
    final var messagesToHandle =
        List.of(
            complementMessageBuilder()
                .id(new MessageId("firstMessage"))
                .status(MessageStatus.HANDLED)
                .build(),
            complementMessageBuilder()
                .id(new MessageId("secondMessage"))
                .status(MessageStatus.HANDLED)
                .build());

    setMessagesToHandleDomainService.unhandled(messagesToHandle);

    verify(messageRepository).save(messagesToHandle.get(0));
    verify(messageRepository).save(messagesToHandle.get(1));
  }
}
