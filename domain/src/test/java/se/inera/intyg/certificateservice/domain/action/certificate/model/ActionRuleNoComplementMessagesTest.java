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
package se.inera.intyg.certificateservice.domain.action.certificate.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.message.model.Answer;
import se.inera.intyg.certificateservice.domain.message.model.Message;
import se.inera.intyg.certificateservice.domain.message.model.MessageStatus;
import se.inera.intyg.certificateservice.domain.message.model.MessageType;

class ActionRuleNoComplementMessagesTest {

  private static final ActionRuleNoComplementMessages actionRule =
      new ActionRuleNoComplementMessages();

  @Test
  void shouldReturnFalseIfMessageIsComplementAndNotAnsweredOrHandled() {
    final var result =
        actionRule.evaluate(
            Optional.of(
                MedicalCertificate.builder()
                    .messages(
                        List.of(
                            Message.builder()
                                .type(MessageType.COMPLEMENT)
                                .status(MessageStatus.SENT)
                                .build()))
                    .build()),
            Optional.empty());

    assertFalse(result);
  }

  @Test
  void shouldReturnTrueIfNoComplement() {
    final var result =
        actionRule.evaluate(
            Optional.of(
                MedicalCertificate.builder()
                    .messages(
                        List.of(
                            Message.builder()
                                .type(MessageType.REMINDER)
                                .status(MessageStatus.SENT)
                                .build()))
                    .build()),
            Optional.empty());

    assertTrue(result);
  }

  @Test
  void shouldReturnTrueIfNoMessages() {
    final var result =
        actionRule.evaluate(
            Optional.of(MedicalCertificate.builder().messages(Collections.emptyList()).build()),
            Optional.empty());

    assertTrue(result);
  }

  @Test
  void shouldReturnTrueIfNullMessages() {
    final var result =
        actionRule.evaluate(Optional.of(MedicalCertificate.builder().build()), Optional.empty());

    assertTrue(result);
  }

  @Test
  void shouldReturnTrueIfHandledComplement() {
    final var result =
        actionRule.evaluate(
            Optional.of(
                MedicalCertificate.builder()
                    .messages(
                        List.of(
                            Message.builder()
                                .type(MessageType.COMPLEMENT)
                                .status(MessageStatus.HANDLED)
                                .build()))
                    .build()),
            Optional.empty());

    assertTrue(result);
  }

  @Test
  void shouldReturnTrueIfAnsweredComplement() {
    final var result =
        actionRule.evaluate(
            Optional.of(
                MedicalCertificate.builder()
                    .messages(
                        List.of(
                            Message.builder()
                                .type(MessageType.COMPLEMENT)
                                .status(MessageStatus.SENT)
                                .answer(Answer.builder().build())
                                .build()))
                    .build()),
            Optional.empty());

    assertTrue(result);
  }
}
