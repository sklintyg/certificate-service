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
package se.inera.intyg.certificateservice.certificate.custom.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPdfFieldDTO;
import se.inera.intyg.certificateservice.certificate.custom.provider.PdfFieldsProvider;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;

@ExtendWith(MockitoExtension.class)
class CustomPdfFieldsConverterTest {

  @Mock private PdfFieldsProvider firstProvider;
  @Mock private PdfFieldsProvider secondProvider;
  @Mock private Certificate certificate;
  @Mock private CustomPdfSpecification spec;

  private CustomPdfFieldsConverter converter;

  @org.junit.jupiter.api.BeforeEach
  void setUp() {
    converter = new CustomPdfFieldsConverter(List.of(firstProvider, secondProvider));
  }

  @Test
  void shallMergeFieldsFromAllProviders() {
    when(firstProvider.fields(certificate, spec))
        .thenReturn(Map.of("field-a", new CustomPdfFieldDTO("value-a")));
    when(secondProvider.fields(certificate, spec))
        .thenReturn(Map.of("field-b", new CustomPdfFieldDTO("value-b")));

    final var fields = converter.convert(certificate, spec);

    assertEquals(
        Map.of(
            "field-a", new CustomPdfFieldDTO("value-a"),
            "field-b", new CustomPdfFieldDTO("value-b")),
        fields);
  }

  @Test
  void shallReturnEmptyMapWhenNoProviders() {
    final var converterWithNoProviders = new CustomPdfFieldsConverter(List.of());

    final var fields = converterWithNoProviders.convert(certificate, spec);

    assertEquals(Map.of(), fields);
  }
}
