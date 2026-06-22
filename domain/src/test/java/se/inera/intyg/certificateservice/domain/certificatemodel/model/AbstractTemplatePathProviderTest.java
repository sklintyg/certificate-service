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
package se.inera.intyg.certificateservice.domain.certificatemodel.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Sent;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;

@ExtendWith(MockitoExtension.class)
class AbstractTemplatePathProviderTest {

  private static final String PATH_WITH_ADDRESS = "path/with-address.pdf";
  private static final String PATH_WITHOUT_ADDRESS = "path/without-address.pdf";

  private final TestTemplatePathProvider provider = new TestTemplatePathProvider();

  @Mock private Certificate certificate;

  @Mock private PdfGeneratorOptions options;

  @Test
  void shallReturnPathWithoutAddressWhenCitizenFormat() {
    when(options.citizenFormat()).thenReturn(true);

    assertEquals(PATH_WITHOUT_ADDRESS, provider.pathOf(certificate, options));
  }

  @Test
  void shallReturnPathWithoutAddressWhenCertificateIsSent() {
    when(options.citizenFormat()).thenReturn(false);
    when(certificate.sent()).thenReturn(Sent.builder().sentAt(LocalDateTime.now()).build());

    assertEquals(PATH_WITHOUT_ADDRESS, provider.pathOf(certificate, options));
  }

  @Test
  void shallReturnPathWithAddressWhenCertificateSentIsNull() {
    when(options.citizenFormat()).thenReturn(false);
    when(certificate.sent()).thenReturn(null);

    assertEquals(PATH_WITH_ADDRESS, provider.pathOf(certificate, options));
  }

  @Test
  void shallReturnPathWithAddressWhenCertificateSentAtIsNull() {
    when(options.citizenFormat()).thenReturn(false);
    when(certificate.sent()).thenReturn(Sent.builder().sentAt(null).build());

    assertEquals(PATH_WITH_ADDRESS, provider.pathOf(certificate, options));
  }

  private static class TestTemplatePathProvider extends AbstractTemplatePathProvider {

    @Override
    protected String pathWithoutAddress() {
      return PATH_WITHOUT_ADDRESS;
    }

    @Override
    protected String pathWithAddress() {
      return PATH_WITH_ADDRESS;
    }
  }
}
