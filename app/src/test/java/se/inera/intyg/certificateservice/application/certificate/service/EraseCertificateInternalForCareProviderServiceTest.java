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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;

@ExtendWith(MockitoExtension.class)
class EraseCertificateInternalForCareProviderServiceTest {

  private static final String CARE_PROVIDER_ID = "careProviderId";
  @Mock CertificateRepository certificateRepository;

  @InjectMocks
  EraseCertificateInternalForCareProviderService eraseCertificateInternalForCareProviderService;

  @Test
  void shallDeleteByCareProviderId() {
    eraseCertificateInternalForCareProviderService.erase(CARE_PROVIDER_ID);

    verify(certificateRepository).deleteByCareProviderId(new HsaId(CARE_PROVIDER_ID));
  }

  @Test
  @ExtendWith(OutputCaptureExtension.class)
  void shallLog(CapturedOutput output) {
    doReturn(5L).when(certificateRepository).deleteByCareProviderId(new HsaId(CARE_PROVIDER_ID));

    eraseCertificateInternalForCareProviderService.erase(CARE_PROVIDER_ID);
    assertThat(output)
        .contains(
            "Successfully completed erasure of certificates for care provider 'careProviderId' Total number of erased certificates: 5");
  }
}
