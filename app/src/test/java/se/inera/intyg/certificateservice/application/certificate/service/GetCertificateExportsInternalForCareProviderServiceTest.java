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
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.FK3226_CERTIFICATE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.FK7210_CERTIFICATE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.XML;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.ExportCertificateInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.ExportCertificateInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.ExportInternalResponse;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateExportPage;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.certificate.service.XmlGenerator;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;

@ExtendWith(MockitoExtension.class)
class GetCertificateExportsInternalForCareProviderServiceTest {

  private static final int PAGE = 1;
  private static final int SIZE = 10;
  private static final String CARE_PROVIDER_ID = "careProviderId";
  @Mock CertificateRepository certificateRepository;
  @Mock XmlGenerator xmlGenerator;

  @InjectMocks
  GetCertificateExportsInternalForCareProviderService
      getCertificateExportsInternalForCareProviderService;

  @Test
  void shallReturnExportInternalResponseExportInternalResponse() {
    final var exportCertificateInternalResponses =
        List.of(
            ExportCertificateInternalResponse.builder()
                .certificateId(FK7210_CERTIFICATE.id().id())
                .xml(XML.base64())
                .revoked(true)
                .build(),
            ExportCertificateInternalResponse.builder()
                .certificateId(FK3226_CERTIFICATE.id().id())
                .xml(XML.base64())
                .revoked(true)
                .build());

    final var expectedResult =
        ExportInternalResponse.builder().exports(exportCertificateInternalResponses).build();

    final var certificateExportPage =
        CertificateExportPage.builder()
            .certificates(List.of(FK7210_CERTIFICATE, FK3226_CERTIFICATE))
            .build();

    final var internalRequest =
        ExportCertificateInternalRequest.builder().page(PAGE).size(SIZE).build();

    doReturn(certificateExportPage)
        .when(certificateRepository)
        .getExportByCareProviderId(new HsaId(CARE_PROVIDER_ID), PAGE, SIZE);

    final var actualResult =
        getCertificateExportsInternalForCareProviderService.get(internalRequest, CARE_PROVIDER_ID);
    assertEquals(expectedResult, actualResult);
  }
}
