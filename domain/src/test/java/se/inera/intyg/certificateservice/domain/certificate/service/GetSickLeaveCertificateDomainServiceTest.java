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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificate.model.SickLeaveCertificate;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.SickLeaveProvider;

@ExtendWith(MockitoExtension.class)
class GetSickLeaveCertificateDomainServiceTest {

  private static final CertificateId CERTIFICATE_ID = new CertificateId("certificateId");
  @Mock CertificateRepository certificateRepository;
  @InjectMocks GetSickLeaveCertificateDomainService getSickLeaveCertificateDomainService;

  @Test
  void shouldReturnOptionalEmptyIfNoSickLeaveProviderIsPresent() {
    final var certificate =
        MedicalCertificate.builder().certificateModel(CertificateModel.builder().build()).build();

    when(certificateRepository.getById(CERTIFICATE_ID)).thenReturn(certificate);

    final var result = getSickLeaveCertificateDomainService.get(CERTIFICATE_ID, false);
    assertTrue(result.isEmpty());
  }

  @Test
  void shouldReturnOptionalOfSickLeaveCertificateBuiltFromSickLeaveProvider() {
    final var expectedSickLeaveCertificate =
        SickLeaveCertificate.builder().partOfSickLeaveChain(true).build();
    final var sickLeaveProvider = mock(SickLeaveProvider.class);
    final var certificate =
        MedicalCertificate.builder()
            .certificateModel(
                CertificateModel.builder().sickLeaveProvider(sickLeaveProvider).build())
            .build();

    when(certificateRepository.getById(CERTIFICATE_ID)).thenReturn(certificate);
    when(sickLeaveProvider.build(certificate, false))
        .thenReturn(Optional.of(expectedSickLeaveCertificate));

    final var result = getSickLeaveCertificateDomainService.get(CERTIFICATE_ID, false);
    assertEquals(Optional.of(expectedSickLeaveCertificate), result);
  }

  @Test
  void shouldReturnOptionalEmptyIfSickLeaveIsNotPartOfChain() {
    final var sickLeaveProvider = mock(SickLeaveProvider.class);
    final var certificate =
        MedicalCertificate.builder()
            .certificateModel(
                CertificateModel.builder().sickLeaveProvider(sickLeaveProvider).build())
            .build();
    final var sickLeave = SickLeaveCertificate.builder().partOfSickLeaveChain(false).build();

    when(certificateRepository.getById(CERTIFICATE_ID)).thenReturn(certificate);
    when(sickLeaveProvider.build(certificate, false)).thenReturn(Optional.of(sickLeave));
    final var result = getSickLeaveCertificateDomainService.get(CERTIFICATE_ID, false);
    assertTrue(result.isEmpty());
  }
}
