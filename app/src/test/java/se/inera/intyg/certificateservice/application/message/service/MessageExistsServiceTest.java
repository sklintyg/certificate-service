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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.message.dto.MessageExistsResponse;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.repository.MessageRepository;

@ExtendWith(MockitoExtension.class)
class MessageExistsServiceTest {

  private static final String MESSAGE_ID = "messageId";
  @Mock private MessageRepository messageRepository;
  @InjectMocks private MessageExistsService messageExistsService;

  @Test
  void shallThrowIfMessageIdIsNull() {
    assertThrows(IllegalArgumentException.class, () -> messageExistsService.exist(null));
  }

  @Test
  void shallThrowIfMessageIdIsBlank() {
    assertThrows(IllegalArgumentException.class, () -> messageExistsService.exist(" "));
  }

  @Test
  void shallReturnCertificateExistsResponse() {
    final var messageId = new MessageId(MESSAGE_ID);
    final var expectedResponse = MessageExistsResponse.builder().exists(true).build();

    doReturn(true).when(messageRepository).exists(messageId);

    final var actualResponse = messageExistsService.exist(MESSAGE_ID);
    assertEquals(expectedResponse, actualResponse);
  }
}
