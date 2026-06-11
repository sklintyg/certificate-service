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
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPdfFieldDTO;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPdfMetadataDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfSignature;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfTagIndex;

@ExtendWith(MockitoExtension.class)
class CustomPrintRequestConverterTest {

  private static final byte[] TEMPLATE_BYTES = new byte[] {1, 2, 3};
  private static final String ENCODED_TEMPLATE = Base64.getEncoder().encodeToString(TEMPLATE_BYTES);
  private static final String FILE_NAME = "intyg_om_graviditet_26-01-01_1200";
  private static final String ADDITIONAL_INFO = "Webcert 2.0";

  @InjectMocks private CustomPrintRequestConverter converter;

  @Mock private CustomPdfMetadataConverter customPdfMetadataConverter;
  @Mock private CustomPdfFieldsConverter customPdfFieldsConverter;
  @Mock private Certificate certificate;

  private CustomPdfSpecification spec;
  private PdfGeneratorOptions options;
  private CustomPdfMetadataDTO stubMetadata;
  private Map<String, CustomPdfFieldDTO> stubFields;

  @BeforeEach
  void setUp() {
    spec =
        CustomPdfSpecification.builder()
            .pdfTemplatePath("fk7210/pdf/fk7210_v1.pdf")
            .pdfNoAddressTemplatePath("fk7210/pdf/fk7210_v1_no_address.pdf")
            .patientIdFieldIds(List.of())
            .signature(
                PdfSignature.builder()
                    .signedDateFieldId(new PdfFieldId("date"))
                    .signedByNameFieldId(new PdfFieldId("name"))
                    .paTitleFieldId(new PdfFieldId("pa"))
                    .specialtyFieldId(new PdfFieldId("spec"))
                    .hsaIdFieldId(new PdfFieldId("hsa"))
                    .workplaceCodeFieldId(new PdfFieldId("wp"))
                    .contactInformation(new PdfFieldId("contact"))
                    .signatureWithAddressTagIndex(new PdfTagIndex(15))
                    .signatureWithoutAddressTagIndex(new PdfTagIndex(7))
                    .signaturePageIndex(0)
                    .build())
            .signatureTextX(100)
            .signatureTextY(50)
            .signatureTextFontSize(8)
            .build();

    options =
        PdfGeneratorOptions.builder()
            .additionalInfoText(ADDITIONAL_INFO)
            .citizenFormat(false)
            .hiddenElements(List.of())
            .build();

    stubMetadata = new CustomPdfMetadataDTO(List.of(), null, null, false);
    stubFields = Map.of();

    when(certificate.sent()).thenReturn(null);
    when(customPdfMetadataConverter.convert(any(), any(), any(), anyBoolean(), anyString()))
        .thenReturn(stubMetadata);
    when(customPdfFieldsConverter.convert(any(), any())).thenReturn(stubFields);
  }

  @Test
  void shallBase64EncodeTemplate() {
    final var request = converter.convert(certificate, options, spec, TEMPLATE_BYTES, FILE_NAME);

    assertEquals(ENCODED_TEMPLATE, request.template());
  }

  @Test
  void shallDelegateMetadataConversionToMetadataConverter() {
    converter.convert(certificate, options, spec, TEMPLATE_BYTES, FILE_NAME);

    verify(customPdfMetadataConverter)
        .convert(eq(certificate), eq(options), eq(spec), anyBoolean(), eq(FILE_NAME));
  }

  @Test
  void shallDelegateFieldsConversionToFieldsConverter() {
    converter.convert(certificate, options, spec, TEMPLATE_BYTES, FILE_NAME);

    verify(customPdfFieldsConverter).convert(eq(certificate), eq(spec));
  }

  @Test
  void shallIncludeMetadataFromMetadataConverter() {
    final var request = converter.convert(certificate, options, spec, TEMPLATE_BYTES, FILE_NAME);

    assertEquals(stubMetadata, request.metadata());
  }

  @Test
  void shallIncludeFieldsFromFieldsConverter() {
    final var request = converter.convert(certificate, options, spec, TEMPLATE_BYTES, FILE_NAME);

    assertEquals(stubFields, request.fields());
  }
}
