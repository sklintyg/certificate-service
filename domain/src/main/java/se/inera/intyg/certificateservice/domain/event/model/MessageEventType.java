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
package se.inera.intyg.certificateservice.domain.event.model;

public enum MessageEventType {
  ANSWER_COMPLEMENT(
      "answer-complement-message", Constants.ACTION_TYPE_CREATION, Constants.MESSAGE_TYPE_SENT),
  SEND_QUESTION(
      "send-question-message", Constants.ACTION_TYPE_CREATION, Constants.MESSAGE_TYPE_SENT),
  SEND_ANSWER("send-answer-message", Constants.ACTION_TYPE_CREATION, Constants.MESSAGE_TYPE_SENT);

  private final String action;
  private final String actionType;
  private final String messageType;

  MessageEventType(String action, String actionType, String messageType) {
    this.action = action;
    this.actionType = actionType;
    this.messageType = messageType;
  }

  public String action() {
    return action;
  }

  public String actionType() {
    return actionType;
  }

  public String messageType() {
    return messageType;
  }

  public boolean hasMessageType() {
    return messageType != null;
  }

  private static class Constants {

    private static final String ACTION_TYPE_CREATION = "creation";
    private static final String MESSAGE_TYPE_SENT = "message-sent";
  }
}
