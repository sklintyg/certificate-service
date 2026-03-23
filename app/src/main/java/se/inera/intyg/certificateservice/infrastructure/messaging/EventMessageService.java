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

import static se.inera.intyg.certificateservice.logging.MdcLogConstants.SESSION_ID_KEY;
import static se.inera.intyg.certificateservice.logging.MdcLogConstants.TRACE_ID_KEY;

import jakarta.jms.JMSException;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.event.model.CertificateEvent;
import se.inera.intyg.certificateservice.domain.event.model.MessageEvent;
import se.inera.intyg.certificateservice.domain.event.service.CertificateEventSubscriber;
import se.inera.intyg.certificateservice.domain.event.service.MessageEventSubscriber;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventMessageService implements CertificateEventSubscriber, MessageEventSubscriber {

  private final JmsTemplate jmsTemplate;

  @Override
  public void event(CertificateEvent event) {
    if (event.type().hasMessageType()) {
      jmsTemplate.send(
          session ->
              getCertificateEventMessage(
                  event.certificate().id().id(), event.type().messageType(), session));
    }
  }

  @Override
  public void event(MessageEvent event) {
    jmsTemplate.send(
        session ->
            getMessageEventMessage(
                event.messageId().id(),
                event.type().messageType(),
                event.certificateId().id(),
                session));
  }

  private static TextMessage getMessageEventMessage(
      String messageId, String eventType, String certificateId, Session session)
      throws JMSException {
    final var message = session.createTextMessage();
    message.setStringProperty("messageId", messageId);
    message.setStringProperty("certificateId", certificateId);
    message.setStringProperty("eventType", eventType);
    message.setStringProperty(SESSION_ID_KEY, MDC.get(SESSION_ID_KEY));
    message.setStringProperty(TRACE_ID_KEY, MDC.get(TRACE_ID_KEY));
    return message;
  }

  private static TextMessage getCertificateEventMessage(
      String certificateId, String eventType, Session session) throws JMSException {
    final var message = session.createTextMessage();
    message.setStringProperty("certificateId", certificateId);
    message.setStringProperty("eventType", eventType);
    message.setStringProperty(SESSION_ID_KEY, MDC.get(SESSION_ID_KEY));
    message.setStringProperty(TRACE_ID_KEY, MDC.get(TRACE_ID_KEY));
    return message;
  }
}
