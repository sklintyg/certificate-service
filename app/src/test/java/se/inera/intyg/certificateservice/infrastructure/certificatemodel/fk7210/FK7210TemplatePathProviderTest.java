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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210TemplatePathProvider.PDF_FK_7210_PDF;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210TemplatePathProvider.PDF_NO_ADDRESS_FK_7210_PDF;

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
class FK7210TemplatePathProviderTest {

  @Mock private Certificate certificate;

  private final FK7210TemplatePathProvider provider = new FK7210TemplatePathProvider();

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

    assertEquals(PDF_FK_7210_PDF, path);
  }

  @Test
  void shallReturnNoAddressTemplateWhenCitizenFormat() {
    final var path = provider.pathOf(certificate, options(true));

    assertEquals(PDF_NO_ADDRESS_FK_7210_PDF, path);
  }

  @Test
  void shallReturnNoAddressTemplateWhenSent() {
    when(certificate.sent()).thenReturn(Sent.builder().sentAt(LocalDateTime.now()).build());

    final var path = provider.pathOf(certificate, options(false));

    assertEquals(PDF_NO_ADDRESS_FK_7210_PDF, path);
  }
}
