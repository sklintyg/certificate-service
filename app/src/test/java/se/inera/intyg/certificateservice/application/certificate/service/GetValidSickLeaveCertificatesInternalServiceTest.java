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
package se.inera.intyg.certificateservice.application.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.FK7804_CERTIFICATE;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.GetValidSickLeaveCertificateIdsInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.GetValidSickLeaveCertificateIdsInternalResponse;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;

@ExtendWith(MockitoExtension.class)
class GetValidSickLeaveCertificatesInternalServiceTest {

  private static final String CERTIFICATE_ID = "CERTIFICATE_ID";

  @Mock CertificateRepository certificateRepository;

  @InjectMocks
  GetValidSickLeaveCertificatesInternalService getValidSickLeaveCertificatesInternalService;

  @Test
  void shouldThrowIfRequestIsNull() {
    assertThrows(
        IllegalArgumentException.class,
        () -> getValidSickLeaveCertificatesInternalService.get(null));
  }

  @Test
  void shouldThrowIfCertificateIdsIsNullInRequest() {
    final var request = GetValidSickLeaveCertificateIdsInternalRequest.builder().build();
    assertThrows(
        IllegalArgumentException.class,
        () -> getValidSickLeaveCertificatesInternalService.get(request));
  }

  @Test
  void shouldThrowIfCertificateIdsIsEmptyInRequest() {
    final var request =
        GetValidSickLeaveCertificateIdsInternalRequest.builder()
            .certificateIds(Collections.emptyList())
            .build();

    assertThrows(
        IllegalArgumentException.class,
        () -> getValidSickLeaveCertificatesInternalService.get(request));
  }

  @Test
  void shouldReturnGetValidSickLeaveCertificateIdsInternalResponse() {
    final var expectedResult =
        GetValidSickLeaveCertificateIdsInternalResponse.builder()
            .certificateIds(Collections.singletonList(FK7804_CERTIFICATE.id().id()))
            .build();
    final var request =
        GetValidSickLeaveCertificateIdsInternalRequest.builder()
            .certificateIds(List.of(CERTIFICATE_ID))
            .build();

    when(certificateRepository.findValidSickLeavesCertificateIdsByIds(
            List.of(new CertificateId(CERTIFICATE_ID))))
        .thenReturn(List.of(FK7804_CERTIFICATE.id()));

    final var actualResult = getValidSickLeaveCertificatesInternalService.get(request);
    assertEquals(expectedResult, actualResult);
  }
}
