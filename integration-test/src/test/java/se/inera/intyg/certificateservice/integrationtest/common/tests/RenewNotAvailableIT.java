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
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO.SIGNED;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultRenewCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultSendCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultTestablilityCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificateId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseIntegrationIT;

public abstract class RenewNotAvailableIT extends BaseIntegrationIT {

  @Test
  @DisplayName("Intyg skall inte kunna förnyas - 403 (FORBIDDEN)")
  void shallReturn403IfRenewReceived() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion(), SIGNED));

    api().sendCertificate(defaultSendCertificateRequest(), certificateId(testCertificates));

    final var response =
        api().renewCertificate(defaultRenewCertificateRequest(), certificateId(testCertificates));

    assertEquals(403, response.getStatusCode().value());
  }
}
