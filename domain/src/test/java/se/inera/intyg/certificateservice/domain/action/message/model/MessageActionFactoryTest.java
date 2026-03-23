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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageActionSpecification;
import se.inera.intyg.certificateservice.domain.message.model.MessageActionType;

class MessageActionFactoryTest {

  @Test
  void shallIncludeActionComplement() {
    final var messageActionSpecification =
        MessageActionSpecification.builder()
            .messageActionType(MessageActionType.COMPLEMENT)
            .build();

    final var messageAction = MessageActionFactory.create(messageActionSpecification);
    assertEquals(messageAction.getClass(), MessageActionComplement.class);
  }

  @Test
  void shallIncludeActionCannotComplement() {
    final var messageActionSpecification =
        MessageActionSpecification.builder()
            .messageActionType(MessageActionType.CANNOT_COMPLEMENT)
            .build();

    final var messageAction = MessageActionFactory.create(messageActionSpecification);
    assertEquals(messageAction.getClass(), MessageActionCannotComplement.class);
  }

  @Test
  void shallIncludeActionHandleComplement() {
    final var messageActionSpecification =
        MessageActionSpecification.builder()
            .messageActionType(MessageActionType.HANDLE_COMPLEMENT)
            .build();

    final var messageAction = MessageActionFactory.create(messageActionSpecification);
    assertEquals(messageAction.getClass(), MessageActionHandleComplement.class);
  }

  @Test
  void shallIncludeActionForward() {
    final var messageActionSpecification =
        MessageActionSpecification.builder().messageActionType(MessageActionType.FORWARD).build();

    final var messageAction = MessageActionFactory.create(messageActionSpecification);
    assertEquals(messageAction.getClass(), MessageActionForward.class);
  }

  @Test
  void shallIncludeActionAnswer() {
    final var messageActionSpecification =
        MessageActionSpecification.builder().messageActionType(MessageActionType.ANSWER).build();

    final var messageAction = MessageActionFactory.create(messageActionSpecification);
    assertEquals(messageAction.getClass(), MessageActionAnswer.class);
  }

  @Test
  void shallIncludeActionHandleMessage() {
    final var messageActionSpecification =
        MessageActionSpecification.builder()
            .messageActionType(MessageActionType.HANDLE_MESSAGE)
            .build();

    final var messageAction = MessageActionFactory.create(messageActionSpecification);
    assertEquals(messageAction.getClass(), MessageActionHandleMessage.class);
  }
}
