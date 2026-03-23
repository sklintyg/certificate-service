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
package se.inera.intyg.certificateservice.application.citizen.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.citizen.validation.CitizenCertificateRequestValidator;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;

@ExtendWith(MockitoExtension.class)
class CitizenCertificateExistsServiceTest {

  private static final String CERTIFICATE_ID = "CERTIFICATE_ID";

  @Mock CertificateRepository certificateRepository;

  @Mock CitizenCertificateRequestValidator citizenCertificateRequestValidator;

  @InjectMocks CitizenCertificateExistsService citizenCertificateExistsService;

  @Test
  void shouldValidateCertificateId() {
    citizenCertificateExistsService.exist(CERTIFICATE_ID);

    verify(citizenCertificateRequestValidator).validate(CERTIFICATE_ID);
  }

  @Test
  void shouldReturnTrueIfExistsInRepo() {
    when(certificateRepository.exists(new CertificateId(CERTIFICATE_ID))).thenReturn(true);

    final var result = citizenCertificateExistsService.exist(CERTIFICATE_ID);

    assertTrue(result.isExists());
  }

  @Test
  void shouldReturnFalseIfNotExistsInRepo() {
    final var result = citizenCertificateExistsService.exist(CERTIFICATE_ID);

    assertFalse(result.isExists());
  }
}
