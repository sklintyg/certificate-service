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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PrefillAnswerTest {

  @Test
  void shouldReturnAnswerNotFoundError() {
    final var answer = PrefillAnswer.answerNotFound("MI1");
    assertAll(
        () -> assertEquals(1, answer.getErrors().size()),
        () -> assertEquals(PrefillErrorType.ANSWER_NOT_FOUND, answer.getErrors().getFirst().type()),
        () ->
            assertEquals(
                "Answer with id MI1 not found in certificate model",
                answer.getErrors().getFirst().details()));
  }

  @Test
  void shouldReturnInvalidFormatError() {
    final var answer = PrefillAnswer.invalidFormat("id", "message");
    assertAll(
        () -> assertEquals(1, answer.getErrors().size()),
        () -> assertEquals(PrefillErrorType.INVALID_FORMAT, answer.getErrors().getFirst().type()),
        () ->
            assertEquals(
                "Invalid format for answer id 'id' - reason: message",
                answer.getErrors().getFirst().details()));
  }
}
