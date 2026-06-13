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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPdfMetadataDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;

@ExtendWith(MockitoExtension.class)
class CustomPrintRequestConverterTest {

  @Mock private CustomPdfMetadataConverter customPdfMetadataConverter;
  @Mock private CustomPdfFieldsConverter customPdfFieldsConverter;
  @Mock private Certificate certificate;
  @InjectMocks private CustomPrintRequestConverter converter;

  private static final byte[] TEMPLATE_BYTES = new byte[] {1, 2, 3};
  private static final String ENCODED_TEMPLATE = Base64.getEncoder().encodeToString(TEMPLATE_BYTES);
  private static final String FILE_NAME = "intyg_om_graviditet_26-01-01_1200";
  private static final String ADDITIONAL_INFO = "Webcert 2.0";

  private final CustomPdfSpecification spec = CustomPdfSpecification.builder().build();
  private final PdfGeneratorOptions options =
      PdfGeneratorOptions.builder()
          .additionalInfoText(ADDITIONAL_INFO)
          .citizenFormat(false)
          .hiddenElements(List.of())
          .build();

  @Test
  void shallBase64EncodeTemplate() {
    final var request = converter.convert(certificate, options, spec, TEMPLATE_BYTES, FILE_NAME);

    assertEquals(ENCODED_TEMPLATE, request.template());
  }

  @Test
  void shallConvertMetadata() {
    final var expected = new CustomPdfMetadataDTO(List.of(), null, null, false);
    when(customPdfMetadataConverter.convert(any(), any(), any(), anyString())).thenReturn(expected);

    final var request = converter.convert(certificate, options, spec, TEMPLATE_BYTES, FILE_NAME);

    assertEquals(expected, request.metadata());
  }

  @Test
  void shallIncludeFieldsFromFieldsConverter() {
    when(customPdfFieldsConverter.convert(any(), any())).thenReturn(Collections.emptyMap());

    final var request = converter.convert(certificate, options, spec, TEMPLATE_BYTES, FILE_NAME);

    assertEquals(Collections.emptyMap(), request.fields());
  }
}
