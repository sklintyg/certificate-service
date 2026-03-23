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
package se.inera.intyg.certificateservice.infrastructure.messaging;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEvent;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEventType;
import se.inera.intyg.certificateservice.domain.event.model.MessageEvent;
import se.inera.intyg.certificateservice.domain.event.model.MessageEventType;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;

@ExtendWith(MockitoExtension.class)
class EventMessageServiceTest {

  private static final MessageId MESSAGE_ID = new MessageId("ID");
  @InjectMocks EventMessageService eventMessageService;

  @Mock JmsTemplate jmsTemplate;

  @Nested
  class CertificateEventTests {

    @Test
    void shouldLogForSignedEvent() {
      final var event = createEvent(CertificateEventType.SIGNED);

      eventMessageService.event(event);

      verify(jmsTemplate, times(1)).send(any());
    }

    @Test
    void shouldLogForSentEvent() {
      final var event = createEvent(CertificateEventType.SENT);

      eventMessageService.event(event);

      verify(jmsTemplate, times(1)).send(any());
    }

    @Test
    void shouldNotLogForReadEvent() {
      final var event = createEvent(CertificateEventType.READ);

      eventMessageService.event(event);

      verify(jmsTemplate, times(0)).send(any());
    }

    private CertificateEvent createEvent(CertificateEventType eventType) {
      return CertificateEvent.builder()
          .certificate(MedicalCertificate.builder().id(new CertificateId("ID")).build())
          .type(eventType)
          .build();
    }
  }

  @Nested
  class MessageEventTests {

    @Test
    void shouldSendEvent() {
      final var event = createEvent();

      eventMessageService.event(event);

      verify(jmsTemplate, times(1)).send(any());
    }

    private MessageEvent createEvent() {
      return MessageEvent.builder()
          .messageId(MESSAGE_ID)
          .type(MessageEventType.ANSWER_COMPLEMENT)
          .build();
    }
  }
}
