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
import static org.mockito.Mockito.when;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Pdf;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;

@ExtendWith(MockitoExtension.class)
class GetCertificateInternalPdfDomainServiceTest {

  private static final CertificateId CERTIFICATE_ID = new CertificateId("certificateId");
  private static final Certificate CERTIFICATE = MedicalCertificate.builder().build();
  private static final Pdf PDF = new Pdf("pdfData".getBytes(), "fileName");

  @Mock private CertificateRepository certificateRepository;
  @Mock private PdfGenerator pdfGenerator;
  @Mock private PdfGeneratorProvider pdfGeneratorProvider;

  @InjectMocks
  private GetCertificateInternalPdfDomainService getCertificateInternalPdfDomainService;

  @BeforeEach
  void setup() {
    when(certificateRepository.getById(CERTIFICATE_ID)).thenReturn(CERTIFICATE);
  }

  @Test
  void shallReturnGeneratedPdf() {
    final var options =
        PdfGeneratorOptions.builder()
            .citizenFormat(false)
            .hiddenElements(Collections.emptyList())
            .build();

    when(pdfGeneratorProvider.provider(CERTIFICATE)).thenReturn(pdfGenerator);
    when(pdfGenerator.generate(CERTIFICATE, options)).thenReturn(PDF);

    final var actualResult = getCertificateInternalPdfDomainService.get(CERTIFICATE_ID);

    assertEquals(PDF, actualResult);
  }
}
