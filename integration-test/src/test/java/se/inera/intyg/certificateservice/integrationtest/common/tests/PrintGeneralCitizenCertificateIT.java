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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO.SIGNED;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ALVE_REACT_ALFREDSSON_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultTestablilityCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificateId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import se.inera.intyg.certificateservice.application.citizen.dto.PrintCitizenCertificateRequest;
import se.inera.intyg.certificateservice.application.common.dto.PersonIdDTO;
import se.inera.intyg.certificateservice.application.common.dto.PersonIdTypeDTO;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseIntegrationIT;
import se.inera.intyg.certificateservice.integrationtest.common.util.CertificatePrintServiceMock;
import se.inera.intyg.certificateservice.integrationtest.common.util.Containers;

public abstract class PrintGeneralCitizenCertificateIT extends BaseIntegrationIT {

  private CertificatePrintServiceMock certificatePrintServiceMock;

  @BeforeEach
  void setupMocks() {
    final var mockServerClient =
        new MockServerClient(
            Containers.MOCK_SERVER_CONTAINER.getHost(),
            Containers.MOCK_SERVER_CONTAINER.getServerPort());
    this.certificatePrintServiceMock = new CertificatePrintServiceMock(mockServerClient);
  }

  @Test
  @DisplayName("Om intyget är utfärdat på invånaren ska intyget skrivas ut")
  void shallReturnCertificateIfIssuedOnCitizen() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion(), SIGNED));

    certificatePrintServiceMock.mockPdf();

    final var response =
        api()
            .printCitizenCertificate(
                PrintCitizenCertificateRequest.builder()
                    .personId(
                        PersonIdDTO.builder()
                            .type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER)
                            .id(ATHENA_REACT_ANDERSSON_ID)
                            .build())
                    .additionalInfo("Intyget är utskrivet från 1177 Intyg.")
                    .build(),
                certificateId(testCertificates));

    assertNotNull(response.getBody().getPdfData(), "Should return pdf of certificate of patient.");
  }

  @Test
  @DisplayName("Om intyget är utfärdat på en annan invånare ska felkod 403 (FORBIDDEN) returneras")
  void shallReturn403IfNotIssuedOnCitizen() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion(), SIGNED));

    certificatePrintServiceMock.mockPdf();

    final var response =
        api()
            .printCitizenCertificate(
                PrintCitizenCertificateRequest.builder()
                    .personId(
                        PersonIdDTO.builder()
                            .type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER)
                            .id(ALVE_REACT_ALFREDSSON_ID)
                            .build())
                    .additionalInfo("Intyget är utskrivet från 1177 Intyg.")
                    .build(),
                certificateId(testCertificates));

    assertEquals(403, response.getStatusCode().value());
  }
}
