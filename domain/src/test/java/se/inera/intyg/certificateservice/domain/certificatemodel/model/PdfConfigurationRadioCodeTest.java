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
package se.inera.intyg.certificateservice.domain.certificatemodel.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;

class PdfConfigurationRadioCodeTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final FieldId CODE_FIELD_ID = new FieldId("PROGNOS");
  private static final PdfFieldId RADIO_GROUP = new PdfFieldId("form.radio[0]");
  private static final PdfFieldId OPTION_VALUE = new PdfFieldId("3");

  private final Certificate certificate = mock(Certificate.class);

  @Test
  void shallReturnRadioGroupValue() {
    final var elementValue =
        ElementValueCode.builder().codeId(CODE_FIELD_ID).code(CODE_FIELD_ID.value()).build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationRadioCode.builder()
            .radioGroupFieldId(RADIO_GROUP)
            .codes(Map.of(CODE_FIELD_ID, OPTION_VALUE))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var expected =
        List.of(PdfField.builder().fieldId(RADIO_GROUP).value(OPTION_VALUE.id()).build());

    assertEquals(expected, config.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallReturnEmptyWhenCodeIsInvalid() {
    final var elementValue = ElementValueCode.builder().codeId(null).code("x").build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationRadioCode.builder()
            .radioGroupFieldId(RADIO_GROUP)
            .codes(Map.of(CODE_FIELD_ID, OPTION_VALUE))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    assertEquals(Collections.emptyList(), config.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallThrowWhenCodeNotConfigured() {
    final var unknown = new FieldId("OTHER");
    final var elementValue =
        ElementValueCode.builder().codeId(unknown).code(unknown.value()).build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationRadioCode.builder()
            .radioGroupFieldId(RADIO_GROUP)
            .codes(Map.of(CODE_FIELD_ID, OPTION_VALUE))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    assertThrows(
        IllegalArgumentException.class,
        () -> config.toPdfFields(elementSpec, certificate).toList());
  }
}
