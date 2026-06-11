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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.GeneralPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.TemplatePdfSpecification;

@ExtendWith(MockitoExtension.class)
class PdfGeneratorProviderTest {

  @Mock private PdfGenerator certificatePdfService;
  @Mock private PdfGenerator generalPdfService;
  @Mock private PdfGenerator customPdfService;
  @Mock private Certificate certificate;
  @Mock private CertificateModel certificateModel;

  private PdfGeneratorProvider provider;

  @BeforeEach
  void setUp() {
    provider = new PdfGeneratorProvider(certificatePdfService, generalPdfService, customPdfService);
    when(certificate.certificateModel()).thenReturn(certificateModel);
  }

  @Test
  void shallReturnGeneralPdfServiceWhenPdfSpecificationIsNull() {
    when(certificateModel.pdfSpecification()).thenReturn(null);

    assertEquals(generalPdfService, provider.provider(certificate));
  }

  @Test
  void shallReturnGeneralPdfServiceWhenGeneralPdfSpecification() {
    when(certificateModel.pdfSpecification()).thenReturn(GeneralPdfSpecification.builder().build());

    assertEquals(generalPdfService, provider.provider(certificate));
  }

  @Test
  void shallReturnCustomPdfServiceWhenCustomPdfSpecification() {
    when(certificateModel.pdfSpecification())
        .thenReturn(
            CustomPdfSpecification.builder()
                .pdfTemplatePath("template.pdf")
                .pdfNoAddressTemplatePath("template_no_address.pdf")
                .build());

    assertEquals(customPdfService, provider.provider(certificate));
  }

  @Test
  void shallReturnCertificatePdfServiceWhenTemplatePdfSpecification() {
    when(certificateModel.pdfSpecification())
        .thenReturn(TemplatePdfSpecification.builder().build());

    assertEquals(certificatePdfService, provider.provider(certificate));
  }
}
