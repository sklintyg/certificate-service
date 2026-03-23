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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.converter.PrefillCheckboxBooleanConverter;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;
import se.riv.clinicalprocess.healthcond.certificate.v33.Forifyllnad;

class PrefillCheckboxBooleanConverterTest {

  private static final ElementId ELEMENT_ID = new ElementId("1");
  private static final FieldId CHECKBOX_ID = new FieldId("2");
  private static final ElementSpecification SPECIFICATION =
      ElementSpecification.builder()
          .id(ELEMENT_ID)
          .configuration(ElementConfigurationCheckboxBoolean.builder().id(CHECKBOX_ID).build())
          .build();

  private final PrefillCheckboxBooleanConverter converter = new PrefillCheckboxBooleanConverter();

  @Test
  void shouldReturnSupportsCheckboxBoolean() {
    assertEquals(ElementConfigurationCheckboxBoolean.class, converter.supports());
  }

  @Nested
  class PrefillAnswerWithForifyllnad {

    @Test
    void shouldReturnNullIfNoAnswersOrSubAnswers() {
      final var prefill = new Forifyllnad();

      final var result = converter.prefillAnswer(SPECIFICATION, prefill);

      assertNull(result);
    }

    @Test
    void shouldReturnErrorIfWrongConfigurationType() {
      final var prefill = new Forifyllnad();
      final var wrongSpec =
          ElementSpecification.builder()
              .id(ELEMENT_ID)
              .configuration(ElementConfigurationCategory.builder().build())
              .build();

      final var result = converter.prefillAnswer(wrongSpec, prefill);

      assertEquals(PrefillErrorType.TECHNICAL_ERROR, result.getErrors().getFirst().type());
    }

    @Test
    void shouldReturnErrorIfInvalidBooleanValue() {
      final var prefill = new Forifyllnad();
      final var svar = new Svar();
      svar.setId(ELEMENT_ID.id());
      final var delsvar = new Delsvar();
      delsvar.setId(CHECKBOX_ID.value());
      delsvar.getContent().add("notABoolean");
      svar.getDelsvar().add(delsvar);
      prefill.getSvar().add(svar);

      final var result = converter.prefillAnswer(SPECIFICATION, prefill);

      assertEquals(PrefillErrorType.INVALID_BOOLEAN_VALUE, result.getErrors().getFirst().type());
    }

    @Test
    void shouldReturnPrefillAnswerTrueIfSubAnswerExists() {
      final var prefill = getPrefill(true, ELEMENT_ID.id());
      final var expected =
          PrefillAnswer.builder()
              .elementData(
                  ElementData.builder()
                      .id(new ElementId(ELEMENT_ID.id()))
                      .value(
                          ElementValueBoolean.builder().booleanId(CHECKBOX_ID).value(true).build())
                      .build())
              .build();

      final var result = converter.prefillAnswer(SPECIFICATION, prefill);

      assertEquals(expected, result);
    }

    @Test
    void shouldReturnPrefillAnswerFalseIfSubAnswerExists() {
      final var prefill = getPrefill(false, ELEMENT_ID.id());
      final var expected =
          PrefillAnswer.builder()
              .elementData(
                  ElementData.builder()
                      .id(new ElementId(ELEMENT_ID.id()))
                      .value(
                          ElementValueBoolean.builder().booleanId(CHECKBOX_ID).value(false).build())
                      .build())
              .build();
      final var result = converter.prefillAnswer(SPECIFICATION, prefill);
      assertEquals(expected, result);
    }
  }

  private static Forifyllnad getPrefill(boolean answer, String id) {
    final var prefill = new Forifyllnad();
    final var svar = new Svar();
    svar.setId(id);
    final var delsvar = new Delsvar();
    delsvar.setId(CHECKBOX_ID.value());
    delsvar.getContent().add(String.valueOf(answer));
    svar.getDelsvar().add(delsvar);
    prefill.getSvar().add(svar);
    return prefill;
  }
}
