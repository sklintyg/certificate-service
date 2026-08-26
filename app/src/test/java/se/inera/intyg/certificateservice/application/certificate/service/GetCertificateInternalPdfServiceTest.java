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
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateInternalPdfResponse;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Pdf;
import se.inera.intyg.certificateservice.domain.certificate.service.GetCertificateInternalPdfDomainService;

@ExtendWith(MockitoExtension.class)
class GetCertificateInternalPdfServiceTest {

  private static final String CERTIFICATE_ID = "certificateId";
  private static final byte[] PDF_DATA = "pdfData".getBytes();
  private static final Pdf PDF = new Pdf(PDF_DATA, "fileName");

  @Mock private GetCertificateInternalPdfDomainService getCertificateInternalPdfDomainService;
  @InjectMocks private GetCertificateInternalPdfService getCertificateInternalPdfService;

  @Test
  void shallThrowIfCertificateIdIsNull() {
    assertThrows(IllegalArgumentException.class, () -> getCertificateInternalPdfService.get(null));
  }

  @Test
  void shallThrowIfCertificateIdIsBlank() {
    assertThrows(IllegalArgumentException.class, () -> getCertificateInternalPdfService.get(""));
  }

  @Test
  void shallReturnGetCertificateInternalPdfResponse() {
    final var expectedResponse =
        GetCertificateInternalPdfResponse.builder().pdfData(PDF_DATA).build();

    doReturn(PDF)
        .when(getCertificateInternalPdfDomainService)
        .get(new CertificateId(CERTIFICATE_ID));

    final var actualResponse = getCertificateInternalPdfService.get(CERTIFICATE_ID);

    assertEquals(expectedResponse, actualResponse);
  }
}
