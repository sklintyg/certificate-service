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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueIcf;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationIcf;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.PrefillAnswer;
import se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill.PrefillErrorType;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;
import se.riv.clinicalprocess.healthcond.certificate.v33.Forifyllnad;

class PrefillIcfConverterTest {

  private static final ElementId ELEMENT_ID = new ElementId("1");
  private static final FieldId TEXT_AREA_ID = new FieldId("2");
  private static final String TEXT = "Text";
  private static final ElementSpecification SPECIFICATION =
      ElementSpecification.builder()
          .id(ELEMENT_ID)
          .configuration(ElementConfigurationIcf.builder().id(TEXT_AREA_ID).build())
          .build();
  private static final ElementData EXPECTED_ELEMENT_DATA =
      ElementData.builder()
          .id(ELEMENT_ID)
          .value(ElementValueIcf.builder().id(TEXT_AREA_ID).text(TEXT).build())
          .build();

  private final PrefillIcfConverter prefillIcfConverter = new PrefillIcfConverter();

  @Test
  void shouldReturnSupportsTextArea() {
    assertEquals(ElementConfigurationIcf.class, prefillIcfConverter.supports());
  }

  @Nested
  class PrefillAnswerWithForifyllnad {

    @Test
    void shouldReturnNullIfNoAnswersOrSubAnswers() {
      Forifyllnad prefill = new Forifyllnad();

      PrefillAnswer result = prefillIcfConverter.prefillAnswer(SPECIFICATION, prefill);

      assertNull(result);
    }

    @Test
    void shouldReturnPrefillAnswerWithInvalidSubAnswerId() {
      final var prefill = new Forifyllnad();
      final var svar = new Svar();
      svar.setId(ELEMENT_ID.id());
      final var delsvar = new Delsvar();
      delsvar.setId("wrongId");
      delsvar.getContent().add(TEXT);
      svar.getDelsvar().add(delsvar);
      prefill.getSvar().add(svar);

      final var result = prefillIcfConverter.prefillAnswer(SPECIFICATION, prefill);

      assertEquals(PrefillErrorType.INVALID_SUB_ANSWER_ID, result.getErrors().getFirst().type());
    }

    @Test
    void shouldReturnPrefillAnswerIfSubAnswerExists() {
      final var prefill = new Forifyllnad();
      final var svar = new Svar();
      svar.setId("other");
      final var delsvar = new Delsvar();
      delsvar.setId(TEXT_AREA_ID.value());
      delsvar.getContent().add(TEXT);
      svar.getDelsvar().add(delsvar);
      prefill.getSvar().add(svar);

      final var specification =
          ElementSpecification.builder()
              .id(new ElementId(TEXT_AREA_ID.value()))
              .configuration(ElementConfigurationIcf.builder().id(TEXT_AREA_ID).build())
              .build();

      final var result = prefillIcfConverter.prefillAnswer(specification, prefill);

      final var expected =
          PrefillAnswer.builder()
              .elementData(
                  ElementData.builder()
                      .id(new ElementId(TEXT_AREA_ID.value()))
                      .value(ElementValueIcf.builder().id(TEXT_AREA_ID).text(TEXT).build())
                      .build())
              .build();

      assertEquals(expected, result);
    }

    @Test
    void shouldReturnPrefillAnswerIfAnswerExists() {
      final var prefill = new Forifyllnad();
      final var svar = new Svar();
      svar.setId(SPECIFICATION.id().id());
      final var delsvar = new Delsvar();
      delsvar.setId(TEXT_AREA_ID.value());
      delsvar.getContent().add(TEXT);
      svar.getDelsvar().add(delsvar);
      prefill.getSvar().add(svar);

      final var result = prefillIcfConverter.prefillAnswer(SPECIFICATION, prefill);

      final var expected = PrefillAnswer.builder().elementData(EXPECTED_ELEMENT_DATA).build();

      assertEquals(expected, result);
    }

    @Test
    void shouldReturnErrorIfWrongConfigurationType() {
      final var prefill = new Forifyllnad();
      final var wrongSpec =
          ElementSpecification.builder()
              .id(ELEMENT_ID)
              .configuration(ElementConfigurationCategory.builder().build())
              .build();

      final var result = prefillIcfConverter.prefillAnswer(wrongSpec, prefill);

      assertEquals(PrefillErrorType.TECHNICAL_ERROR, result.getErrors().getFirst().type());
    }

    @Test
    void shouldReturnErrorIfMultipleAnswers() {
      final var prefill = new Forifyllnad();
      final var svar1 = new Svar();
      svar1.setId(SPECIFICATION.id().id());

      final var svar2 = new Svar();
      svar2.setId(SPECIFICATION.id().id());

      prefill.getSvar().add(svar1);
      prefill.getSvar().add(svar2);

      final var result = prefillIcfConverter.prefillAnswer(SPECIFICATION, prefill);

      assertEquals(PrefillErrorType.WRONG_NUMBER_OF_ANSWERS, result.getErrors().getFirst().type());
    }

    @Test
    void shouldReturnErrorIfMultipleSubAnswers() {
      final var prefill = new Forifyllnad();
      final var svar1 = new Svar();
      svar1.setId("other");
      final var delsvar1 = new Delsvar();
      delsvar1.setId(SPECIFICATION.id().id());
      delsvar1.getContent().add(TEXT);
      svar1.getDelsvar().add(delsvar1);

      final var delsvar2 = new Delsvar();
      delsvar2.setId(SPECIFICATION.id().id());
      delsvar2.getContent().add(TEXT);
      svar1.getDelsvar().add(delsvar2);

      prefill.getSvar().add(svar1);

      final var result = prefillIcfConverter.prefillAnswer(SPECIFICATION, prefill);

      assertEquals(PrefillErrorType.WRONG_NUMBER_OF_ANSWERS, result.getErrors().getFirst().type());
    }

    @Test
    void shouldReturnErrorIfBothSubAnswerAndAnswerIsPresent() {
      final var prefill = new Forifyllnad();
      final var svar1 = new Svar();
      svar1.setId(SPECIFICATION.id().id());
      final var delsvar1 = new Delsvar();
      delsvar1.setId(SPECIFICATION.id().id());
      delsvar1.getContent().add(TEXT);
      svar1.getDelsvar().add(delsvar1);

      prefill.getSvar().add(svar1);

      final var result = prefillIcfConverter.prefillAnswer(SPECIFICATION, prefill);

      assertEquals(PrefillErrorType.WRONG_NUMBER_OF_ANSWERS, result.getErrors().getFirst().type());
    }
  }
}
