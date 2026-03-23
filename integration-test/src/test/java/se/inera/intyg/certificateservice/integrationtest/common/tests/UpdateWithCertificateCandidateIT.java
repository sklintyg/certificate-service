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
package se.inera.intyg.certificateservice.integrationtest.common.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO.SIGNED;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.customTestabilityCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultGetCertificateCandidateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultUpdateWithCertificateCandidateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificateId;
import static se.inera.intyg.certificateservice.testability.certificate.dto.TestabilityFillTypeDTO.EMPTY;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseIntegrationIT;

public abstract class UpdateWithCertificateCandidateIT extends BaseIntegrationIT {

  private static final String FK7804 = "fk7804";
  private static final String VERSION = "2.0";

  @Test
  @DisplayName("Skall returnera ett fk7804 kandidatintyg om det finns ett för patienten")
  void shallReturnCandidateCertificateWhenExists() {
    final var candidateCertificate =
        testabilityApi()
            .addCertificate(customTestabilityCertificateRequest(FK7804, VERSION, SIGNED).build());

    final var certificate =
        testabilityApi()
            .addCertificate(
                customTestabilityCertificateRequest(type(), typeVersion()).fillType(EMPTY).build());

    final var response =
        api()
            .getCertificateCandidate(
                defaultGetCertificateCandidateRequest(), certificateId(certificate.getBody()));

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(
        candidateCertificate.getBody().getCertificate().getMetadata().getId(),
        response.getBody().getCertificate().getMetadata().getId());
  }

  @Test
  @DisplayName("Skall returnera ett tomt kandidatintyg om det saknas för patienten")
  void shallReturnEmptyCandidateCertificateWhenNotExists() {
    testabilityApi().addCertificate(customTestabilityCertificateRequest(FK7804, VERSION).build());

    final var certificate =
        testabilityApi()
            .addCertificate(
                customTestabilityCertificateRequest(type(), typeVersion()).fillType(EMPTY).build());

    final var response =
        api()
            .getCertificateCandidate(
                defaultGetCertificateCandidateRequest(), certificateId(certificate.getBody()));

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNull(response.getBody().getCertificate());
  }

  @Test
  @DisplayName("Skall uppdatera utkastet med kandidatintyget om det finns ett för patienten")
  void shallReturnUpdateDraftWithCandidate() {
    final var candidateCertificate =
        testabilityApi()
            .addCertificate(customTestabilityCertificateRequest(FK7804, VERSION, SIGNED).build());

    final var certificate =
        testabilityApi()
            .addCertificate(
                customTestabilityCertificateRequest(type(), typeVersion()).fillType(EMPTY).build());

    final var response =
        api()
            .updateWithCertificateCandidate(
                defaultUpdateWithCertificateCandidateRequest(),
                certificateId(certificate.getBody()),
                certificateId(candidateCertificate.getBody()));

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().getCertificate().getMetadata().getVersion());
  }
}
