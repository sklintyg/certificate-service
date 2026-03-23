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
package se.inera.intyg.certificateservice.domain.action.message.model;

import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageActionSpecification;

public class MessageActionFactory {

  private MessageActionFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static MessageAction create(MessageActionSpecification actionSpecification) {
    return switch (actionSpecification.messageActionType()) {
      case COMPLEMENT ->
          MessageActionComplement.builder().specification(actionSpecification).build();
      case CANNOT_COMPLEMENT ->
          MessageActionCannotComplement.builder().specification(actionSpecification).build();
      case HANDLE_COMPLEMENT ->
          MessageActionHandleComplement.builder().specification(actionSpecification).build();
      case FORWARD -> MessageActionForward.builder().specification(actionSpecification).build();
      case ANSWER -> MessageActionAnswer.builder().specification(actionSpecification).build();
      case HANDLE_MESSAGE ->
          MessageActionHandleMessage.builder().specification(actionSpecification).build();
    };
  }
}
