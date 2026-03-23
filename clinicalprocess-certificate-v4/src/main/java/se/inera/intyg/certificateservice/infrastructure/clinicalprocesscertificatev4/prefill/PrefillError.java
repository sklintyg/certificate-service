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

import static se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.PrefillErrorType.ANSWER_NOT_FOUND;
import static se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.PrefillErrorType.INVALID_DIAGNOSIS_CODE;
import static se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.PrefillErrorType.INVALID_SUB_ANSWER_ID;
import static se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.PrefillErrorType.WRONG_NUMBER_OF_ANSWERS;

import java.util.List;

public record PrefillError(PrefillErrorType type, String details) {

  public static PrefillError answerNotFound(String id) {
    return new PrefillError(
        ANSWER_NOT_FOUND, "Answer with id %s not found in certificate model".formatted(id));
  }

  public static PrefillError technicalError(String s) {
    return new PrefillError(
        PrefillErrorType.TECHNICAL_ERROR, "A technical error occurred %s".formatted(s));
  }

  public static PrefillError wrongConfigurationType() {
    return technicalError("Wrong configuration type");
  }

  public static PrefillError invalidSubAnswerId(
      String expectedId, List<String> actualIds, String answerId) {
    return new PrefillError(
        INVALID_SUB_ANSWER_ID,
        "Invalid sub answer id. Expected '%s' but got '%s' for answer id %s"
            .formatted(expectedId, actualIds, answerId));
  }

  public static PrefillError invalidBooleanValue(String answerId, String value) {
    return new PrefillError(
        PrefillErrorType.INVALID_BOOLEAN_VALUE,
        "Invalid boolean value '%s' for answer id %s".formatted(value, answerId));
  }

  public static PrefillError invalidFormat(String answerId, String exceptionMessage) {
    return new PrefillError(
        PrefillErrorType.INVALID_FORMAT,
        "Invalid format for answer id '%s' - reason: %s".formatted(answerId, exceptionMessage));
  }

  public static PrefillError missingConverter(String converter) {
    return new PrefillError(
        PrefillErrorType.MISSING_CONVERTER, "No converter found for %s".formatted(converter));
  }

  public static PrefillError wrongNumberOfAnswers(String answerId, int expected, int actual) {
    return new PrefillError(
        WRONG_NUMBER_OF_ANSWERS,
        "Expected %d answers but got %d for answer %s".formatted(expected, actual, answerId));
  }

  public static PrefillError wrongNumberOfSubAnswers(String answerId, int expected, int actual) {
    return new PrefillError(
        WRONG_NUMBER_OF_ANSWERS,
        "Expected %d sub-answers but got %d for answer %s".formatted(expected, actual, answerId));
  }

  public static PrefillError duplicateAnswer(String answerId) {
    return new PrefillError(
        WRONG_NUMBER_OF_ANSWERS, "Multiple occurrences for answer %s".formatted(answerId));
  }

  public static PrefillError invalidDiagnosisCode(String code) {
    return new PrefillError(
        INVALID_DIAGNOSIS_CODE, "Invalid diagnosis code provided '%s'".formatted(code));
  }
}
