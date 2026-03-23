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
import se.inera.intyg.certificateservice.application.certificate.dto.config.CertificateDataConfigCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;

class CertificateDataCategoryConfigConverterTest {

  CertificateDataCategoryConfigConverter certificateDataCategoryConfigConverter;

  @BeforeEach
  void setUp() {
    certificateDataCategoryConfigConverter = new CertificateDataCategoryConfigConverter();
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
            certificateDataCategoryConfigConverter.convert(
                elementSpecification, FK7210_CERTIFICATE));
  }

  @Test
  void shouldReturnCategoryType() {
    assertEquals(ElementType.CATEGORY, certificateDataCategoryConfigConverter.getType());
  }

  @Test
  void shouldReturnConvertedCategory() {
    final var expected =
        CertificateDataConfigCategory.builder().text("NAME").description("DESCRIPTION").build();

    final var response =
        certificateDataCategoryConfigConverter.convert(
            ElementSpecification.builder()
                .configuration(
                    ElementConfigurationCategory.builder()
                        .name("NAME")
                        .description("DESCRIPTION")
                        .build())
                .build(),
            FK7210_CERTIFICATE);

    assertEquals(expected, response);
  }
}
