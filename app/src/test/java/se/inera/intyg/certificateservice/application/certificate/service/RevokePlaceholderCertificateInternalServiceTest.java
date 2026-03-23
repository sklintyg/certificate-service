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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.PlaceholderCertificate;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;

@ExtendWith(SpringExtension.class)
class RevokePlaceholderCertificateInternalServiceTest {

  private static final String CERTIFICATE_ID = "certificateId";
  @Mock CertificateRepository certificateRepository;

  @InjectMocks
  RevokePlaceholderCertificateInternalService revokePlaceholderCertificateInternalService;

  @Test
  void shouldThrowIfCertificateIdIsNull() {
    assertThrows(
        IllegalArgumentException.class,
        () -> revokePlaceholderCertificateInternalService.revoke(null));
  }

  @Test
  void shouldThrowIfCertificateIdIsBlank() {
    assertThrows(
        IllegalArgumentException.class,
        () -> revokePlaceholderCertificateInternalService.revoke(""));
  }

  @Test
  void shouldRevokeCertificate() {
    final var placeholderCertificate = mock(PlaceholderCertificate.class);

    when(certificateRepository.getPlaceholderById(new CertificateId(CERTIFICATE_ID)))
        .thenReturn(placeholderCertificate);

    revokePlaceholderCertificateInternalService.revoke(CERTIFICATE_ID);

    verify(placeholderCertificate).revoke(null, null);
  }

  @Test
  void shouldPersistRevokedCertificate() {
    final var placeholderCertificate = mock(PlaceholderCertificate.class);

    when(certificateRepository.getPlaceholderById(new CertificateId(CERTIFICATE_ID)))
        .thenReturn(placeholderCertificate);

    revokePlaceholderCertificateInternalService.revoke(CERTIFICATE_ID);

    verify(certificateRepository).save(placeholderCertificate);
  }

  @Test
  void shouldReturnCertificate() {
    final var expectedCertificate = mock(PlaceholderCertificate.class);

    when(certificateRepository.getPlaceholderById(new CertificateId(CERTIFICATE_ID)))
        .thenReturn(expectedCertificate);
    when(certificateRepository.save(expectedCertificate)).thenReturn(expectedCertificate);

    final var actualCertificate =
        revokePlaceholderCertificateInternalService.revoke(CERTIFICATE_ID);
    assertEquals(expectedCertificate, actualCertificate);
  }
}
