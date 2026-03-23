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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.FK7210_CERTIFICATE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class CertificateDataTextAreaConfigConverterTest {

  CertificateDataTextAreaConfigConverter certificateDataTextAreaConfigConverter;

  @BeforeEach
  void setUp() {
    certificateDataTextAreaConfigConverter = new CertificateDataTextAreaConfigConverter();
  }

  @Test
  void shouldThrowExceptionIfWrongClass() {
    final var elementSpecification =
        ElementSpecification.builder()
            .configuration(ElementConfigurationDate.builder().build())
            .build();

    assertThrows(
        IllegalStateException.class,
        () ->
            certificateDataTextAreaConfigConverter.convert(
                elementSpecification, FK7210_CERTIFICATE));
  }

  @Test
  void shouldReturnCategoryType() {
    assertEquals(ElementType.TEXT_AREA, certificateDataTextAreaConfigConverter.getType());
  }

  @Test
  void shouldReturnConvertedConfig() {
    final var expected =
        CertificateDataConfigTextArea.builder()
            .id("ID")
            .text("NAME")
            .description("DESCRIPTION")
            .header("HEADER")
            .label("LABEL")
            .build();

    final var response =
        certificateDataTextAreaConfigConverter.convert(
            ElementSpecification.builder()
                .configuration(
                    ElementConfigurationTextArea.builder()
                        .id(new FieldId("ID"))
                        .name("NAME")
                        .description("DESCRIPTION")
                        .header("HEADER")
                        .label("LABEL")
                        .build())
                .build(),
            FK7210_CERTIFICATE);

    assertEquals(expected, response);
  }
}
