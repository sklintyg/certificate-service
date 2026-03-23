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
package se.inera.intyg.certificateservice.application.citizen.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.citizen.dto.CertificateLinkDTO;
import se.inera.intyg.certificateservice.application.citizen.dto.CertificateTextTypeDTO;
import se.inera.intyg.certificateservice.domain.common.model.CertificateLink;
import se.inera.intyg.certificateservice.domain.common.model.CertificateText;
import se.inera.intyg.certificateservice.domain.common.model.CertificateTextType;

@ExtendWith(MockitoExtension.class)
class CertificateTextConverterTest {

  @InjectMocks CertificateTextConverter certificateTextConverter;

  private static final String TEXT = "text";
  private static final String NAME = "name";
  private static final String ID = "id";
  private static final String URL = "url";
  private static final CertificateText CERTIFICATE_TEXT =
      CertificateText.builder()
          .text(TEXT)
          .type(CertificateTextType.PREAMBLE_TEXT)
          .links(List.of(CertificateLink.builder().name(NAME).id(ID).url(URL).build()))
          .build();

  @Test
  void shouldThrowIllegalArgumentExceptionIfTextTypeIsNull() {
    final var text =
        CertificateText.builder()
            .text(TEXT)
            .links(List.of(CertificateLink.builder().name(NAME).id(ID).url(URL).build()))
            .build();

    assertThrows(IllegalArgumentException.class, () -> certificateTextConverter.convert(text));
  }

  @Test
  void shouldConvertText() {
    final var result = certificateTextConverter.convert(CERTIFICATE_TEXT);

    assertEquals(TEXT, result.getText());
  }

  @Test
  void shouldConvertType() {
    final var result = certificateTextConverter.convert(CERTIFICATE_TEXT);

    assertEquals(CertificateTextTypeDTO.PREAMBLE_TEXT, result.getType());
  }

  @Test
  void shouldConvertLinks() {
    final var expected = List.of(CertificateLinkDTO.builder().name(NAME).id(ID).url(URL).build());

    final var result = certificateTextConverter.convert(CERTIFICATE_TEXT);

    assertEquals(expected, result.getLinks());
  }
}
