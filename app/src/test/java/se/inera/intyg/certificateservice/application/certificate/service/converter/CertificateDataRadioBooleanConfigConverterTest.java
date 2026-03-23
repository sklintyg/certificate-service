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
import se.inera.intyg.certificateservice.application.certificate.dto.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class CertificateDataRadioBooleanConfigConverterTest {

  private CertificateDataRadioBooleanConfigConverter certificateDataRadioBooleanConfigConverter;

  @BeforeEach
  void setUp() {
    certificateDataRadioBooleanConfigConverter = new CertificateDataRadioBooleanConfigConverter();
  }

  @Test
  void shouldThrowExceptionIfWrongClass() {
    final var elementSpecification =
        ElementSpecification.builder()
            .configuration(ElementConfigurationTextArea.builder().build())
            .build();

    assertThrows(
        IllegalStateException.class,
        () ->
            certificateDataRadioBooleanConfigConverter.convert(
                elementSpecification, FK7210_CERTIFICATE));
  }

  @Test
  void shallSetCorrectId() {
    final var elementSpecification =
        ElementSpecification.builder()
            .id(new ElementId("ID"))
            .configuration(ElementConfigurationRadioBoolean.builder().id(new FieldId("ID")).build())
            .build();

    final var result =
        certificateDataRadioBooleanConfigConverter.convert(
            elementSpecification, FK7210_CERTIFICATE);

    assertEquals("ID", ((CertificateDataConfigRadioBoolean) result).getId());
  }

  @Test
  void shallSetCorrectText() {
    final var elementSpecification =
        ElementSpecification.builder()
            .configuration(
                ElementConfigurationRadioBoolean.builder()
                    .id(new FieldId("ID"))
                    .name("NAME")
                    .build())
            .build();

    final var result =
        certificateDataRadioBooleanConfigConverter.convert(
            elementSpecification, FK7210_CERTIFICATE);

    assertEquals("NAME", result.getText());
  }

  @Test
  void shallSetCorrectDescription() {
    final var elementSpecification =
        ElementSpecification.builder()
            .configuration(
                ElementConfigurationRadioBoolean.builder()
                    .id(new FieldId("ID"))
                    .name("NAME")
                    .description("DESCRIPTION")
                    .build())
            .build();

    final var result =
        certificateDataRadioBooleanConfigConverter.convert(
            elementSpecification, FK7210_CERTIFICATE);

    assertEquals("DESCRIPTION", result.getDescription());
  }

  @Test
  void shallSetCorrectSelectedText() {
    final var elementSpecification =
        ElementSpecification.builder()
            .configuration(
                ElementConfigurationRadioBoolean.builder()
                    .id(new FieldId("ID"))
                    .selectedText("SELECTED_TEXT")
                    .build())
            .build();

    final var result =
        certificateDataRadioBooleanConfigConverter.convert(
            elementSpecification, FK7210_CERTIFICATE);

    assertEquals("SELECTED_TEXT", ((CertificateDataConfigRadioBoolean) result).getSelectedText());
  }

  @Test
  void shallSetCorrectUnselectedText() {
    final var elementSpecification =
        ElementSpecification.builder()
            .configuration(
                ElementConfigurationRadioBoolean.builder()
                    .id(new FieldId("ID"))
                    .unselectedText("UNSELECTED_TEXT")
                    .build())
            .build();

    final var result =
        certificateDataRadioBooleanConfigConverter.convert(
            elementSpecification, FK7210_CERTIFICATE);

    assertEquals(
        "UNSELECTED_TEXT", ((CertificateDataConfigRadioBoolean) result).getUnselectedText());
  }
}
