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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.certificate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueInteger;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar;
import se.riv.clinicalprocess.healthcond.certificate.v3.Svar.Delsvar;

@ExtendWith(MockitoExtension.class)
class XmlGeneratorIntegerTest {

  private static final String QUESTION_ID = "QUESTION_ID";
  private static final String ANSWER_ID = "ANSWER_ID";
  private static final Integer VALUE = 42;
  private final ElementSpecification ELEMENT_SPECIFICATION = ElementSpecification.builder().build();

  @InjectMocks private XmlGeneratorInteger xmlGeneratorInteger;

  @Test
  void shouldMapInteger() {
    final var data =
        ElementData.builder()
            .id(new ElementId(QUESTION_ID))
            .value(
                ElementValueInteger.builder()
                    .value(VALUE)
                    .integerId(new FieldId(ANSWER_ID))
                    .build())
            .build();
    final var expectedData = new Svar();
    final var subAnswer = new Delsvar();
    subAnswer.getContent().add(String.valueOf(VALUE));
    expectedData.setId(QUESTION_ID);
    subAnswer.setId(ANSWER_ID);
    expectedData.getDelsvar().add(subAnswer);

    final var response = xmlGeneratorInteger.generate(data, ELEMENT_SPECIFICATION);

    assertAll(
        () -> assertEquals(expectedData.getId(), response.getFirst().getId()),
        () ->
            assertEquals(
                expectedData.getDelsvar().getFirst().getId(),
                response.getFirst().getDelsvar().getFirst().getId()),
        () ->
            assertEquals(
                expectedData.getDelsvar().getFirst().getContent().getFirst(),
                response.getFirst().getDelsvar().getFirst().getContent().getFirst()));
  }

  @Test
  void shouldMapEmptyIfNullValue() {
    final var data =
        ElementData.builder()
            .value(ElementValueInteger.builder().integerId(new FieldId(ANSWER_ID)).build())
            .id(new ElementId(QUESTION_ID))
            .build();

    final var response = xmlGeneratorInteger.generate(data, ELEMENT_SPECIFICATION);

    assertTrue(response.isEmpty());
  }

  @Test
  void shouldMapEmptyIfValueIsNotInteger() {
    final var data =
        ElementData.builder()
            .value(
                ElementValueText.builder()
                    .text("not an integer")
                    .textId(new FieldId(ANSWER_ID))
                    .build())
            .id(new ElementId(QUESTION_ID))
            .build();

    final var response = xmlGeneratorInteger.generate(data, ELEMENT_SPECIFICATION);

    assertTrue(response.isEmpty());
  }
}
