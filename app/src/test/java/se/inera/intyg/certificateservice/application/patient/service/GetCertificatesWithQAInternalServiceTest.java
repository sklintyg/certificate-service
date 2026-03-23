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
package se.inera.intyg.certificateservice.application.patient.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificatesWithQAInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificatesWithQAInternalResponse;
import se.inera.intyg.certificateservice.application.patient.service.validator.CertificatesWithQARequestValidator;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Xml;
import se.inera.intyg.certificateservice.domain.patient.service.GetCertificatesWithQAInternalDomainService;

@ExtendWith(MockitoExtension.class)
class GetCertificatesWithQAInternalServiceTest {

  private static final String CERTIFICATE_ID_1 = "certificateId1";
  private static final String CERTIFICATE_ID_2 = "certificateId2";
  @Mock CertificatesWithQARequestValidator requestValidator;
  @Mock GetCertificatesWithQAInternalDomainService getCertificatesWithQAInternalDomainService;
  @InjectMocks GetCertificatesWithQAInternalService getCertificatesWithQAInternalService;

  @Test
  void shallThrowIfRequestIsInvalid() {
    final var request = CertificatesWithQAInternalRequest.builder().build();
    doThrow(IllegalArgumentException.class).when(requestValidator).validate(request);

    assertThrows(
        IllegalArgumentException.class, () -> getCertificatesWithQAInternalService.get(request));
  }

  @Test
  void shallReturnCertificatesWithQAResponseWithBase64EncodedXml() {
    final var xml = new Xml("xml");
    final var expectedResponse =
        CertificatesWithQAInternalResponse.builder().list(xml.base64()).build();

    final var request =
        CertificatesWithQAInternalRequest.builder()
            .certificateIds(List.of(CERTIFICATE_ID_1, CERTIFICATE_ID_2))
            .build();

    doReturn(xml)
        .when(getCertificatesWithQAInternalDomainService)
        .get(List.of(new CertificateId(CERTIFICATE_ID_1), new CertificateId(CERTIFICATE_ID_2)));

    final var actualResponse = getCertificatesWithQAInternalService.get(request);
    assertEquals(expectedResponse, actualResponse);
  }
}
