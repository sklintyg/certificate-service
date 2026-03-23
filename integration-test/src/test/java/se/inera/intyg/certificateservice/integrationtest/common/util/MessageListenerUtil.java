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
package se.inera.intyg.certificateservice.integrationtest.common.util;

import jakarta.jms.Message;
import java.time.Duration;
import java.util.Objects;
import se.inera.intyg.certificateservice.integrationtest.common.setup.MessageListener;

public final class MessageListenerUtil {

  private static volatile MessageListener messageListener;

  private MessageListenerUtil() {}

  public static void setMessageListener(MessageListener messageListener) {
    MessageListenerUtil.messageListener = messageListener;
  }

  private static MessageListener getMessageListener() {
    return Objects.requireNonNull(messageListener, "MessageListener not initialized!");
  }

  public static Message awaitByCertificateId(Duration timeout, String certificateId) {
    return getMessageListener().awaitByCertificateId(timeout, certificateId);
  }
}
