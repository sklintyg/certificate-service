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
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateConstants.CERTIFICATE_ID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.BinaryCertificateMetadataDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateInternalBinaryResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.BinaryCertificateMetadataConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Pdf;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.certificate.service.GetCertificateInternalPdfDomainService;

@ExtendWith(MockitoExtension.class)
class GetCertificateInternalBinaryServiceTest {

  @Mock CertificateRepository certificateRepository;
  @Mock GetCertificateInternalPdfDomainService getCertificateInternalPdfDomainService;
  @Mock BinaryCertificateMetadataConverter binaryCertificateMetadataConverter;
  @InjectMocks GetCertificateInternalBinaryService getCertificateInternalBinaryService;

  @Test
  void shallThrowIfCertificateIdIsNull() {
    assertThrows(
        IllegalArgumentException.class, () -> getCertificateInternalBinaryService.get(null));
  }

  @Test
  void shallThrowIfCertificateIdIsBlank() {
    assertThrows(IllegalArgumentException.class, () -> getCertificateInternalBinaryService.get(""));
  }

  @Test
  void shallReturnGetCertificateInternalBinaryResponse() {
    final var certificate = MedicalCertificate.builder().build();
    final var pdf = new Pdf("pdfData".getBytes(), "fileName");
    final var binaryCertificate =
        BinaryCertificateMetadataDTO.builder().certificateId(CERTIFICATE_ID).build();
    final var expectedResponse =
        GetCertificateInternalBinaryResponse.builder().metadata(binaryCertificate).build();

    doReturn(certificate).when(certificateRepository).getById(new CertificateId(CERTIFICATE_ID));
    doReturn(pdf)
        .when(getCertificateInternalPdfDomainService)
        .get(new CertificateId(CERTIFICATE_ID));
    doReturn(binaryCertificate).when(binaryCertificateMetadataConverter).convert(certificate, pdf);

    final var actualResponse = getCertificateInternalBinaryService.get(CERTIFICATE_ID);

    assertEquals(expectedResponse, actualResponse);
  }
}
