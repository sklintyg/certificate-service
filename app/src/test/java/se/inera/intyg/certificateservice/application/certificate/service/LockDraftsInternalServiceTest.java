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

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.LockDraftsRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.LockDraftsResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificate.service.LockCertificateDomainService;

@ExtendWith(MockitoExtension.class)
class LockDraftsInternalServiceTest {

  @Mock LockCertificateDomainService lockCertificateDomainService;
  @Mock CertificateConverter converter;
  @InjectMocks LockDraftsInternalService lockDraftsInternalService;

  @Test
  void shallThrowIfCutoffDateIsNull() {
    final var request = LockDraftsRequest.builder().build();
    assertThrows(IllegalArgumentException.class, () -> lockDraftsInternalService.lock(request));
  }

  @Test
  void shallReturnLockOldDraftsResponse() {
    final var expectedCertificate = CertificateDTO.builder().build();
    final var expectedResponse =
        LockDraftsResponse.builder().certificates(List.of(expectedCertificate)).build();
    final var request = LockDraftsRequest.builder().cutoffDate(LocalDateTime.now()).build();

    final var certificate = mock(MedicalCertificate.class);
    final var certificates = List.of(certificate);
    doReturn(certificates).when(lockCertificateDomainService).lock(request.getCutoffDate());
    doReturn(expectedCertificate)
        .when(converter)
        .convert(certificate, Collections.emptyList(), null);

    final var actualResponse = lockDraftsInternalService.lock(request);
    assertEquals(expectedResponse, actualResponse);
  }
}
