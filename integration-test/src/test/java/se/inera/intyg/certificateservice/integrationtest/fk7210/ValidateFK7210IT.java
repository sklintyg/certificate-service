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
package se.inera.intyg.certificateservice.integrationtest.fk7210;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.elements.QuestionBeraknatFodelsedatum.QUESTION_BERAKNAT_FODELSEDATUM_ID;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.customValidateCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultTestablilityCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificate;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificateId;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.updateDateValue;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.validationErrors;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseIntegrationIT;

public abstract class ValidateFK7210IT extends BaseIntegrationIT {

  @Test
  @DisplayName(
      "Om utkastet innehåller korrekt 'beräknad fodelsedatum' skall utkastet vara klar för signering")
  void shallReturnEmptyListOfErrorsIfDateIsCorrect() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion()));

    final var date = LocalDate.now().plusDays(5);
    final var certificate = certificate(testCertificates);

    Objects.requireNonNull(
        certificate
            .getData()
            .put(
                QUESTION_BERAKNAT_FODELSEDATUM_ID.id(),
                updateDateValue(certificate, QUESTION_BERAKNAT_FODELSEDATUM_ID.id(), date)));

    final var response =
        api()
            .validateCertificate(
                customValidateCertificateRequest().certificate(certificate).build(),
                certificateId(testCertificates));

    assertEquals(Collections.emptyList(), validationErrors(response));
  }

  @Test
  @DisplayName(
      "Om utkastet innehåller 'beräknad fodelsedatum' före dagens datum skall valideringsfel returneras")
  void shallReturnListOfErrorsIfDateIsBeforeTodaysDate() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion()));

    final var date = LocalDate.now().minusDays(1);
    final var certificate = certificate(testCertificates);

    Objects.requireNonNull(
        certificate
            .getData()
            .put(
                QUESTION_BERAKNAT_FODELSEDATUM_ID.id(),
                updateDateValue(certificate, QUESTION_BERAKNAT_FODELSEDATUM_ID.id(), date)));

    final var response =
        api()
            .validateCertificate(
                customValidateCertificateRequest().certificate(certificate).build(),
                certificateId(testCertificates));

    assertAll(
        () ->
            assertEquals(
                1,
                validationErrors(response).size(),
                () -> "Wrong number of errors '%s'".formatted(validationErrors(response))),
        () ->
            assertTrue(
                validationErrors(response)
                    .get(0)
                    .getText()
                    .contains("Ange ett datum som är tidigast"),
                () ->
                    "Expect to contain 'Ange ett datum som är tidigast' but was '%s'"
                        .formatted(validationErrors(response).get(0))));
  }

  @Test
  @DisplayName(
      "Om utkastet innehåller 'beräknad födelsedatum' längre än ett år fram skall valideringsfel returneras")
  void shallReturnListOfErrorsIfDateIsAfterOneYearInFuture() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion()));

    final var date = LocalDate.now().plusYears(1).plusDays(1);
    final var certificate = certificate(testCertificates);

    Objects.requireNonNull(
        certificate
            .getData()
            .put(
                QUESTION_BERAKNAT_FODELSEDATUM_ID.id(),
                updateDateValue(certificate, QUESTION_BERAKNAT_FODELSEDATUM_ID.id(), date)));

    final var response =
        api()
            .validateCertificate(
                customValidateCertificateRequest().certificate(certificate).build(),
                certificateId(testCertificates));

    assertAll(
        () ->
            assertEquals(
                1,
                validationErrors(response).size(),
                () -> "Wrong number of errors '%s'".formatted(validationErrors(response))),
        () ->
            assertTrue(
                validationErrors(response)
                    .get(0)
                    .getText()
                    .contains("Ange ett datum som är senast"),
                () ->
                    "Expect to contain 'Ange ett datum som är senast' but was '%s'"
                        .formatted(validationErrors(response).get(0))));
  }

  @Test
  @DisplayName("Om utkastet saknar 'beräknad födelsedatum' skall valideringsfel returneras")
  void shallReturnListOfErrorsIfDateIsAMissing() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion()));

    final var certificate = certificate(testCertificates);

    Objects.requireNonNull(
        certificate
            .getData()
            .put(
                QUESTION_BERAKNAT_FODELSEDATUM_ID.id(),
                updateDateValue(certificate, QUESTION_BERAKNAT_FODELSEDATUM_ID.id(), null)));

    final var response =
        api()
            .validateCertificate(
                customValidateCertificateRequest().certificate(certificate).build(),
                certificateId(testCertificates));

    assertAll(
        () ->
            assertEquals(
                1,
                validationErrors(response).size(),
                () -> "Wrong number of errors '%s'".formatted(validationErrors(response))),
        () ->
            assertTrue(
                validationErrors(response).get(0).getText().contains("Ange ett datum."),
                () ->
                    "Expect to contain 'Ange ett datum.' but was '%s'"
                        .formatted(validationErrors(response).get(0))));
  }
}
