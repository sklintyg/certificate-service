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
package se.inera.intyg.certificateservice.integrationtest.common.setup;

import static org.awaitility.Awaitility.await;

import jakarta.jms.Message;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.testcontainers.shaded.org.awaitility.core.ConditionTimeoutException;

@Slf4j
public class MessageListener {

  private static final ConcurrentHashMap<String, Message> CERTIFICATE_INDEX =
      new ConcurrentHashMap<>();

  @JmsListener(destination = "${certificate.event.queue.name}")
  public void onMessage(Message message) {
    try {
      final var certificateId = message.getStringProperty("certificateId");
      if (certificateId != null) {
        CERTIFICATE_INDEX.put(certificateId, message);
      }
    } catch (Exception e) {
      log.info(
          "onMessage (instance {}) received but couldn't read properties: {}",
          System.identityHashCode(this),
          message);
    }
  }

  public Message awaitByCertificateId(Duration timeout, String certificateId) {
    try {
      await()
          .atMost(timeout)
          .pollInterval(Duration.ofMillis(200))
          .until(() -> CERTIFICATE_INDEX.containsKey(certificateId));
      return CERTIFICATE_INDEX.get(certificateId);
    } catch (ConditionTimeoutException e) {
      log.debug("Timeout waiting for message with certificateId: {}", certificateId);
      return null;
    }
  }
}
