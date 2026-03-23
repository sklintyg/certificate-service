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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDataElement;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateMetadataDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.config.CertificateDataConfigDate;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueText;
import se.inera.intyg.certificateservice.application.certificate.service.converter.ElementCertificateConverter;
import se.inera.intyg.certificateservice.application.certificate.service.converter.ElementDataConverter;
import se.inera.intyg.certificateservice.application.certificate.service.converter.ElementMetaDataConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;

@ExtendWith(MockitoExtension.class)
class ElementCertificateConverterTest {

  private static final String ELEMENT_ID = "questionId";
  private static final CertificateDataElement CERTIFICATE_QUESTION_ELEMENT =
      CertificateDataElement.builder()
          .config(CertificateDataConfigDate.builder().build())
          .value(CertificateDataValueText.builder().build())
          .build();

  private static final CertificateDataElement CERTIFICATE_QUESTION_ELEMENT_WITHOUT_VALUE =
      CertificateDataElement.builder().config(CertificateDataConfigDate.builder().build()).build();

  @Mock private ElementDataConverter elementDataConverter;

  @Mock private ElementMetaDataConverter elementMetaDataConverter;

  @InjectMocks private ElementCertificateConverter elementCertificateConverter;

  @Test
  void shallConvertDataFromCertificate() {
    final var elementData = ElementData.builder().build();
    final var expectedResult = List.of(elementData);

    final var certificateDTO =
        CertificateDTO.builder().data(Map.of(ELEMENT_ID, CERTIFICATE_QUESTION_ELEMENT)).build();

    doReturn(elementData)
        .when(elementDataConverter)
        .convert(ELEMENT_ID, CERTIFICATE_QUESTION_ELEMENT);

    final var actualResult = elementCertificateConverter.convert(certificateDTO);
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void shallConvertMetadataFromCertificate() {
    final var elementData = ElementData.builder().build();
    final var expectedResult = List.of(elementData);

    final var metadataDTO = CertificateMetadataDTO.builder().build();

    final var certificateDTO =
        CertificateDTO.builder().data(Collections.emptyMap()).metadata(metadataDTO).build();

    doReturn(List.of(elementData)).when(elementMetaDataConverter).convert(metadataDTO);

    final var actualResult = elementCertificateConverter.convert(certificateDTO);
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void shallMergeDataAndMetadata() {
    final var elementDataOne = ElementData.builder().build();
    final var elementDataTwo = ElementData.builder().build();
    final var expectedResult = List.of(elementDataOne, elementDataTwo);
    final var metadataDTO = CertificateMetadataDTO.builder().build();

    final var certificateDTO =
        CertificateDTO.builder()
            .data(Map.of(ELEMENT_ID, CERTIFICATE_QUESTION_ELEMENT))
            .metadata(metadataDTO)
            .build();

    doReturn(elementDataOne)
        .when(elementDataConverter)
        .convert(ELEMENT_ID, CERTIFICATE_QUESTION_ELEMENT);
    doReturn(List.of(elementDataTwo)).when(elementMetaDataConverter).convert(metadataDTO);

    final var actualResult = elementCertificateConverter.convert(certificateDTO);
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void shallFilterCertificateDataElementWithoutValue() {
    final var certificateDTO =
        CertificateDTO.builder()
            .data(Map.of(ELEMENT_ID, CERTIFICATE_QUESTION_ELEMENT_WITHOUT_VALUE))
            .build();

    final var actualResult = elementCertificateConverter.convert(certificateDTO);
    assertTrue(actualResult.isEmpty(), "Should not include data of type category");
  }
}
