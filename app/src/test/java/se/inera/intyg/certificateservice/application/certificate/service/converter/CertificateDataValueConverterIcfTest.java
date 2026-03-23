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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataIcfValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueIcf;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationIcf;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class CertificateDataValueConverterIcfTest {

  private static final String ELEMENT_ID = "elementId";
  private static final String TEST_TEXT = "testText";
  private static final FieldId FIELD_ID = new FieldId("code");
  private static final String NAME = "NAME";
  private final CertificateDataValueConverterIcf converter = new CertificateDataValueConverterIcf();
  private final ElementConfigurationIcf elementConfigurationIcf =
      ElementConfigurationIcf.builder().id(FIELD_ID).name(NAME).build();

  @Test
  void shouldThrowExceptionIfWrongClassOfValueIfElementValueNotNull() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(ElementConfigurationIcf.builder().id(FIELD_ID).build())
            .build();

    final var elementValueDate = ElementValueDate.builder().build();

    assertThrows(
        IllegalStateException.class, () -> converter.convert(configuration, elementValueDate));
  }

  @Test
  void shouldNotThrowExceptionIfWrongClassOfValueIfElementValueIsNull() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(ElementConfigurationIcf.builder().id(FIELD_ID).build())
            .build();

    final var result = converter.convert(configuration, null);
    assertNull(((CertificateDataIcfValue) result).getText());
  }

  @Test
  void shouldThrowExceptionIfWrongClassOfConfig() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(ElementConfigurationTextArea.builder().id(FIELD_ID).build())
            .build();

    final var elementValueIcf = ElementValueIcf.builder().build();

    assertThrows(
        IllegalStateException.class, () -> converter.convert(configuration, elementValueIcf));
  }

  @Test
  void shallReturnType() {
    assertEquals(ElementType.ICF, converter.getType());
  }

  @Test
  void shallCreateCertificateDataIcfValue() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(elementConfigurationIcf)
            .build();

    final var elementValueIcf = ElementValueIcf.builder().build();

    final var result = converter.convert(configuration, elementValueIcf);

    assertInstanceOf(CertificateDataIcfValue.class, result);
  }

  @Test
  void shallSetIdFromConfigurationIcfValue() {
    final var configuration =
        ElementSpecification.builder().configuration(elementConfigurationIcf).build();

    final var elementValueIcf = ElementValueIcf.builder().build();

    final var result = converter.convert(configuration, elementValueIcf);

    assertEquals(FIELD_ID.value(), ((CertificateDataIcfValue) result).getId());
  }

  @Test
  void shallSetCorrectTextForIcfValue() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(elementConfigurationIcf)
            .build();

    final var elementValueIcf = ElementValueIcf.builder().text(TEST_TEXT).build();

    final var result = converter.convert(configuration, elementValueIcf);

    assertEquals(TEST_TEXT, ((CertificateDataIcfValue) result).getText());
  }

  @Test
  void shallSetValueToNull() {
    final var configuration =
        ElementSpecification.builder()
            .id(new ElementId(ELEMENT_ID))
            .configuration(elementConfigurationIcf)
            .build();

    final var elementValueIcf = ElementValueIcf.builder().build();

    final var result = converter.convert(configuration, elementValueIcf);

    assertNull(
        ((CertificateDataIcfValue) result).getText(),
        "If no value is provided value should be null");
  }
}
