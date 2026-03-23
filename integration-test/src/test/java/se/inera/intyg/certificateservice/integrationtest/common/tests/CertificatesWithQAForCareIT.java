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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO.SIGNED;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.customGetCertificatesInternalWithQARequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultGetCertificatesInternalWithQARequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultTestablilityCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificateId;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificatesWithQAResponse;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.decodeXml;

import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseIntegrationIT;

public abstract class CertificatesWithQAForCareIT extends BaseIntegrationIT {

  @Test
  @DisplayName("Skall returnera en lista av intyg")
  void shallReturnListOfListItems() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion(), SIGNED));

    final var certificatesWithQA =
        internalApi()
            .getCertificatesInternalWithQA(
                customGetCertificatesInternalWithQARequest()
                    .certificateIds(
                        testCertificates.stream()
                            .map(certificate -> certificate.getCertificate().getMetadata().getId())
                            .toList())
                    .build());

    final var getCertificatesWithQAResponse = certificatesWithQAResponse(certificatesWithQA);

    final var decodedXml = decodeXml(getCertificatesWithQAResponse.getList());
    assertTrue(decodedXml.contains(Objects.requireNonNull(certificateId(testCertificates))));
  }

  @Test
  @DisplayName("Skall returnera en tom lista om inga intyg hittas")
  void shallReturnListWithoutListItems() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion(), SIGNED));

    final var certificatesWithQA =
        internalApi().getCertificatesInternalWithQA(defaultGetCertificatesInternalWithQARequest());

    final var getCertificatesWithQAResponse = certificatesWithQAResponse(certificatesWithQA);

    final var decodedXml = decodeXml(getCertificatesWithQAResponse.getList());
    assertFalse(decodedXml.contains(Objects.requireNonNull(certificateId(testCertificates))));
  }
}
