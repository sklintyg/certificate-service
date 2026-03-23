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
package se.inera.intyg.certificateservice.application.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDTO;
import se.inera.intyg.certificateservice.application.patient.dto.GetPatientCertificatesRequest;
import se.inera.intyg.certificateservice.application.patient.dto.GetPatientCertificatesResponse;
import se.inera.intyg.certificateservice.application.patient.service.GetPatientCertificateService;

@ExtendWith(MockitoExtension.class)
class PatientCertificateControllerTest {

  @Mock private GetPatientCertificateService getPatientCertificateService;
  @InjectMocks private PatientCertificateController patientCertificateController;

  @Test
  void shallReturnGetPatientCertificatesResponse() {
    final var certificates = List.of(CertificateDTO.builder().build());
    final var expectedResult =
        GetPatientCertificatesResponse.builder().certificates(certificates).build();

    final var request = GetPatientCertificatesRequest.builder().build();
    doReturn(expectedResult).when(getPatientCertificateService).get(request);

    final var actualResult = patientCertificateController.getPatientCertificates(request);
    assertEquals(expectedResult, actualResult);
  }
}
