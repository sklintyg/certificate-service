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
package se.inera.intyg.certificateservice.domain.event.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.event.model.MessageEvent;

class MessageEventDomainServiceTest {

  private MessageEventDomainService messageEventDomainService;
  private MessageEventSubscriber subscriberOne;
  private MessageEventSubscriber subscriberTwo;

  @BeforeEach
  void setUp() {
    subscriberOne = mock(MessageEventSubscriber.class);
    subscriberTwo = mock(MessageEventSubscriber.class);
    final var subscribers = List.of(subscriberOne, subscriberTwo);

    messageEventDomainService = new MessageEventDomainService(subscribers);
  }

  @Test
  void shallPublishToSubscribers() {
    final var expectedEvent = MessageEvent.builder().build();

    messageEventDomainService.publish(expectedEvent);

    verify(subscriberOne).event(expectedEvent);
    verify(subscriberTwo).event(expectedEvent);
  }

  @Test
  void shallThrowExceptionIfEventIsNull() {
    assertThrows(IllegalArgumentException.class, () -> messageEventDomainService.publish(null));

    verifyNoInteractions(subscriberOne);
    verifyNoInteractions(subscriberTwo);
  }
}
