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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.message.model.MessageActionType;

class FK7810MessageActionSpecificationTest {

  @Test
  void shallIncludeMessageActionAnswer() {
    final var expectedType = MessageActionType.ANSWER;

    final var actionSpecifications = FK7810MessageActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.messageActionType())),
        "Expected type: %s".formatted(expectedType));
  }

  @Test
  void shallIncludeMessageActionHandleComplement() {
    final var expectedType = MessageActionType.HANDLE_COMPLEMENT;

    final var actionSpecifications = FK7810MessageActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.messageActionType())),
        "Expected type: %s".formatted(expectedType));
  }

  @Test
  void shallIncludeMessageActionCannotComplement() {
    final var expectedType = MessageActionType.CANNOT_COMPLEMENT;

    final var actionSpecifications = FK7810MessageActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.messageActionType())),
        "Expected type: %s".formatted(expectedType));
  }

  @Test
  void shallIncludeMessageActionComplement() {
    final var expectedType = MessageActionType.COMPLEMENT;

    final var actionSpecifications = FK7810MessageActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.messageActionType())),
        "Expected type: %s".formatted(expectedType));
  }

  @Test
  void shallIncludeMessageActionForward() {
    final var expectedType = MessageActionType.FORWARD;

    final var actionSpecifications = FK7810MessageActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.messageActionType())),
        "Expected type: %s".formatted(expectedType));
  }

  @Test
  void shallIncludeMessageActionHandleMessage() {
    final var expectedType = MessageActionType.HANDLE_MESSAGE;

    final var actionSpecifications = FK7810MessageActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.messageActionType())),
        "Expected type: %s".formatted(expectedType));
  }
}
