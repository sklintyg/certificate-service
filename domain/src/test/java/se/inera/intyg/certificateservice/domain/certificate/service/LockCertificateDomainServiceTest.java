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
package se.inera.intyg.certificateservice.domain.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.model.CertificatesRequest;

@ExtendWith(MockitoExtension.class)
class LockCertificateDomainServiceTest {

  @Mock private CertificateRepository certificateRepository;
  @InjectMocks private LockCertificateDomainService service;

  private LocalDateTime cutoffDate = LocalDateTime.now().minusDays(1);
  private CertificatesRequest request =
      CertificatesRequest.builder().createdTo(cutoffDate).statuses(List.of(Status.DRAFT)).build();

  @Test
  void shallLockDraft() {
    final var certificate = mock(MedicalCertificate.class);
    doReturn(List.of(certificate)).when(certificateRepository).findByCertificatesRequest(request);

    service.lock(cutoffDate);

    verify(certificate).lock();
  }

  @Test
  void shallPersistLockedDraft() {
    final var certificate = mock(MedicalCertificate.class);
    doReturn(List.of(certificate)).when(certificateRepository).findByCertificatesRequest(request);

    service.lock(cutoffDate);

    verify(certificateRepository).save(certificate);
  }

  @Test
  void shallReturnLockedDrafts() {
    final var certificate = mock(MedicalCertificate.class);
    final var lockedCertificate = mock(MedicalCertificate.class);

    doReturn(List.of(certificate)).when(certificateRepository).findByCertificatesRequest(request);
    doReturn(lockedCertificate).when(certificateRepository).save(certificate);

    final var lockedCertificates = service.lock(cutoffDate);

    assertEquals(List.of(lockedCertificate), lockedCertificates);
  }
}
