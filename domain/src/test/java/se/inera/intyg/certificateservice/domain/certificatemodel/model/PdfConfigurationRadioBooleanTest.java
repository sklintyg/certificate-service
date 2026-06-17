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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;

class PdfConfigurationRadioBooleanTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final FieldId BOOLEAN_FIELD_ID = new FieldId("52.1");
  private static final PdfFieldId RADIO_FIELD = new PdfFieldId("form.radio[0]");

  private final Certificate certificate = mock(Certificate.class);

  @Test
  void shallReturnTrueOptionValue() {
    final var elementValue =
        ElementValueBoolean.builder().booleanId(BOOLEAN_FIELD_ID).value(true).build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationRadioBoolean.builder()
            .pdfFieldId(RADIO_FIELD)
            .optionTrue(new PdfRadioOption("2"))
            .optionFalse(new PdfRadioOption("1"))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var expected = List.of(PdfField.builder().fieldId(RADIO_FIELD).value("2").build());

    assertEquals(expected, config.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallReturnFalseOptionValue() {
    final var elementValue =
        ElementValueBoolean.builder().booleanId(BOOLEAN_FIELD_ID).value(false).build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationRadioBoolean.builder()
            .pdfFieldId(RADIO_FIELD)
            .optionTrue(new PdfRadioOption("2"))
            .optionFalse(new PdfRadioOption("1"))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    final var expected = List.of(PdfField.builder().fieldId(RADIO_FIELD).value("1").build());

    assertEquals(expected, config.toPdfFields(elementSpec, certificate).toList());
  }

  @Test
  void shallReturnEmptyWhenValueIsNull() {
    final var elementValue =
        ElementValueBoolean.builder().booleanId(BOOLEAN_FIELD_ID).value(null).build();
    final var elementSpec = ElementSpecification.builder().id(ELEMENT_ID).build();
    final var config =
        PdfConfigurationRadioBoolean.builder()
            .pdfFieldId(RADIO_FIELD)
            .optionTrue(new PdfRadioOption("2"))
            .optionFalse(new PdfRadioOption("1"))
            .build();

    doReturn(Optional.of(ElementData.builder().id(ELEMENT_ID).value(elementValue).build()))
        .when(certificate)
        .getElementDataById(ELEMENT_ID);

    assertEquals(Collections.emptyList(), config.toPdfFields(elementSpec, certificate).toList());
  }
}
