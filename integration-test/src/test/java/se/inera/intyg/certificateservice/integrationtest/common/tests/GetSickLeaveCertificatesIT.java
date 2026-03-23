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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_MEDICINCENTRUM_DTO;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.customTestabilityCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultGetSickLeaveCertificatesInternalRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificateId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseIntegrationIT;
import se.inera.intyg.certificateservice.testability.certificate.dto.TestabilityFillTypeDTO;

public abstract class GetSickLeaveCertificatesIT extends BaseIntegrationIT {

  @Test
  @DisplayName("Om intyget är utfärdat på rätt patient ska det returneras")
  void shallReturnSickLeaveCertificatesForPatient() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(
                customTestabilityCertificateRequest(type(), typeVersion())
                    .unit(ALFA_MEDICINCENTRUM_DTO)
                    .status(CertificateStatusTypeDTO.SIGNED)
                    .fillType(TestabilityFillTypeDTO.MAXIMAL)
                    .build());

    final var response =
        internalApi()
            .getSickLeaveCertificatesInternal(defaultGetSickLeaveCertificatesInternalRequest());

    assertAll(
        () -> assertEquals(1, response.getBody().getCertificates().size()),
        () ->
            assertEquals(
                certificateId(testCertificates),
                response.getBody().getCertificates().getFirst().getCertificateId()));
  }
}
