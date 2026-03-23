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
package se.inera.intyg.certificateservice.application.certificatetypeinfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.CertificateModelIdDTO;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.CertificateTypeInfoDTO;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.GetCertificateTypeInfoRequest;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.GetCertificateTypeInfoResponse;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.GetLatestCertificateExternalTypeVersionResponse;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.GetLatestCertificateTypeVersionResponse;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.service.GetCertificateTypeInfoService;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.service.GetLatestCertificateExternalTypeVersionService;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.service.GetLatestCertificateTypeVersionService;

@ExtendWith(MockitoExtension.class)
class CertificateTypeInfoControllerTest {

  private static final String FK_7210 = "fk7210";
  private static final String VERSION = "1.0";
  private static final String CODE_SYSTEM = "codeSystem";
  private static final String CODE = "code";
  @Mock private GetCertificateTypeInfoService getCertificateTypeInfoService;

  @Mock
  private GetLatestCertificateExternalTypeVersionService
      getLatestCertificateExternalTypeVersionService;

  @Mock private GetLatestCertificateTypeVersionService getLatestCertificateTypeVersionService;
  @InjectMocks private CertificateTypeInfoController certificateTypeInfoController;

  @Test
  void shallReturnListOfCertificateTypeInfo() {
    final var certificateTypeInfoRequest = GetCertificateTypeInfoRequest.builder().build();
    final var expectedResult =
        GetCertificateTypeInfoResponse.builder()
            .list(
                List.of(
                    CertificateTypeInfoDTO.builder().build(),
                    CertificateTypeInfoDTO.builder().build()))
            .build();

    when(getCertificateTypeInfoService.getActiveCertificateTypeInfos(certificateTypeInfoRequest))
        .thenReturn(expectedResult);

    final var result =
        certificateTypeInfoController.findActiveCertificateTypeInfos(certificateTypeInfoRequest);

    assertEquals(expectedResult, result);
  }

  @Test
  void shallReturnCertificateTypeAndVersion() {
    final var expectedResult =
        GetLatestCertificateTypeVersionResponse.builder()
            .certificateModelId(
                CertificateModelIdDTO.builder().type(FK_7210).version(VERSION).build())
            .build();

    when(getLatestCertificateTypeVersionService.get(FK_7210)).thenReturn(expectedResult);

    final var result = certificateTypeInfoController.findLatestCertificateTypeVersion(FK_7210);

    assertEquals(expectedResult, result);
  }

  @Test
  void shallReturnCertificateExternalTypeAndVersion() {
    final var expectedResult =
        GetLatestCertificateExternalTypeVersionResponse.builder()
            .certificateModelId(
                CertificateModelIdDTO.builder().type(FK_7210).version(VERSION).build())
            .build();

    when(getLatestCertificateExternalTypeVersionService.get(CODE_SYSTEM, CODE))
        .thenReturn(expectedResult);

    final var result =
        certificateTypeInfoController.findLatestCertificateExternalTypeVersion(CODE_SYSTEM, CODE);

    assertEquals(expectedResult, result);
  }
}
