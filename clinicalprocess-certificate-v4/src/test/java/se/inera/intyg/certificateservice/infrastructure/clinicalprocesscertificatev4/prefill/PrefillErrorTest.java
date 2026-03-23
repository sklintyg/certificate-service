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

import java.util.List;
import org.junit.jupiter.api.Test;

class PrefillErrorTest {

  @Test
  void shouldReturnAnswerNotFoundError() {
    var error = PrefillError.answerNotFound("MI1");
    assertAll(
        () -> assertEquals(PrefillErrorType.ANSWER_NOT_FOUND, error.type()),
        () -> assertEquals("Answer with id MI1 not found in certificate model", error.details()));
  }

  @Test
  void shouldReturnTechnicalError() {
    var error = PrefillError.technicalError("investigation failed");
    assertAll(
        () -> assertEquals(PrefillErrorType.TECHNICAL_ERROR, error.type()),
        () -> assertEquals("A technical error occurred investigation failed", error.details()));
  }

  @Test
  void shouldReturnWrongConfigurationTypeError() {
    var error = PrefillError.wrongConfigurationType();
    assertAll(
        () -> assertEquals(PrefillErrorType.TECHNICAL_ERROR, error.type()),
        () -> assertEquals("A technical error occurred Wrong configuration type", error.details()));
  }

  @Test
  void shouldReturnInvalidFormatError() {
    var error = PrefillError.invalidFormat("id", "message");
    assertAll(
        () -> assertEquals(PrefillErrorType.INVALID_FORMAT, error.type()),
        () -> assertEquals("Invalid format for answer id 'id' - reason: message", error.details()));
  }

  @Test
  void shouldReturnMissingConverterError() {
    var error = PrefillError.missingConverter("MedicalInvestigationConverter");
    assertAll(
        () -> assertEquals(PrefillErrorType.MISSING_CONVERTER, error.type()),
        () ->
            assertEquals("No converter found for MedicalInvestigationConverter", error.details()));
  }

  @Test
  void shouldReturnInvalidSubAnswerId() {
    final var expectedId = "expectedId";
    final var actualIds = List.of("actualId1", "actualId2");
    final var answerId = "answerId";
    var error = PrefillError.invalidSubAnswerId(expectedId, actualIds, answerId);

    assertAll(
        () -> assertEquals(PrefillErrorType.INVALID_SUB_ANSWER_ID, error.type()),
        () ->
            assertEquals(
                "Invalid sub answer id. Expected '%s' but got '%s' for answer id %s"
                    .formatted(expectedId, actualIds, answerId),
                error.details()));
  }
}
