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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateCountIssuedByRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateCountIssuedByResponse;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;

@ExtendWith(MockitoExtension.class)
class GetCertificateCountIssuedByInternalServiceTest {

  private static final String HSA_ID = "HSA_ID";
  @Mock CertificateRepository certificateRepository;

  @InjectMocks
  GetCertificateCountIssuedByInternalService getCertificateCountIssuedByInternalService;

  @Test
  void shouldThrowIfHsaIdFromRequestIsNull() {
    final var request = GetCertificateCountIssuedByRequest.builder().build();
    assertThrows(
        IllegalArgumentException.class,
        () -> getCertificateCountIssuedByInternalService.get(request));
  }

  @Test
  void shouldThrowIfHsaIdFromRequestIsBlank() {
    final var request = GetCertificateCountIssuedByRequest.builder().issuedByHsaId("").build();
    assertThrows(
        IllegalArgumentException.class,
        () -> getCertificateCountIssuedByInternalService.get(request));
  }

  @Test
  void shouldReturnNumberOfSignedCertificatesIssuedBy() {
    final var numberOfCertificates = 1L;

    final var expectedResult =
        GetCertificateCountIssuedByResponse.builder()
            .numberOfCertificates(numberOfCertificates)
            .build();
    final var request = GetCertificateCountIssuedByRequest.builder().issuedByHsaId(HSA_ID).build();

    when(certificateRepository.getNumberOfSignedCertificatesIssuedBy(new HsaId(HSA_ID)))
        .thenReturn(numberOfCertificates);

    final var actualResult = getCertificateCountIssuedByInternalService.get(request);
    assertEquals(actualResult, expectedResult);
  }
}
