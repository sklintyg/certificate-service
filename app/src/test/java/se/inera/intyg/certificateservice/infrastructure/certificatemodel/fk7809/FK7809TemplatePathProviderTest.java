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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7809;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Sent;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;

@ExtendWith(MockitoExtension.class)
class FK7809TemplatePathProviderTest {

  private static final String EXPECTED_PATH_WITH_ADDRESS = "fk7809/pdf/fk7809_v1.pdf";
  private static final String EXPECTED_PATH_NO_ADDRESS = "fk7809/pdf/fk7809_v1_no_address.pdf";

  @Mock private Certificate certificate;

  private final FK7809TemplatePathProvider provider = new FK7809TemplatePathProvider();

  private PdfGeneratorOptions options(boolean citizenFormat) {
    return PdfGeneratorOptions.builder()
        .additionalInfoText("Webcert")
        .citizenFormat(citizenFormat)
        .hiddenElements(List.of())
        .build();
  }

  @Test
  void shallReturnWithAddressTemplateWhenNotCitizenAndNotSent() {
    when(certificate.sent()).thenReturn(null);

    final var path = provider.pathOf(certificate, options(false));

    assertEquals(EXPECTED_PATH_WITH_ADDRESS, path);
  }

  @Test
  void shallReturnNoAddressTemplateWhenCitizenFormat() {
    final var path = provider.pathOf(certificate, options(true));

    assertEquals(EXPECTED_PATH_NO_ADDRESS, path);
  }

  @Test
  void shallReturnNoAddressTemplateWhenSent() {
    when(certificate.sent()).thenReturn(Sent.builder().sentAt(LocalDateTime.now()).build());

    final var path = provider.pathOf(certificate, options(false));

    assertEquals(EXPECTED_PATH_NO_ADDRESS, path);
  }
}
