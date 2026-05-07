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
package se.inera.intyg.certificateservice.fhir.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Questionnaire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;

class FhirR5HttpMessageConverterTest {

  private static final String QUESTIONNAIRE_JSON = """
      {
        "resourceType": "Questionnaire",
        "id": "test-questionnaire",
        "title": "Test Questionnaire",
        "status": "active"
      }
      """;

  private FhirR5HttpMessageConverter converter;

  @BeforeEach
  void setUp() {
    converter = new FhirR5HttpMessageConverter();
  }

  @Test
  void shouldSupportIBaseResourceSubclasses() {
    assertTrue(converter.canRead(Questionnaire.class, MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldNotSupportNonFhirClasses() {
    assertFalse(converter.canRead(String.class, MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldReadQuestionnaireFromJson() throws IOException {
    final var inputMessage = new MockHttpInputMessage(
        new ByteArrayInputStream(QUESTIONNAIRE_JSON.getBytes(StandardCharsets.UTF_8))
    );

    final var result = converter.read(Questionnaire.class, inputMessage);

    assertInstanceOf(Questionnaire.class, result);
    assertEquals("test-questionnaire", ((Questionnaire) result).getIdPart());
    assertEquals("Test Questionnaire", ((Questionnaire) result).getTitle());
  }

  @Test
  void shouldWriteQuestionnaireToJson() throws IOException {
    final var questionnaire = new Questionnaire();
    questionnaire.setId("written-questionnaire");
    questionnaire.setTitle("Written Questionnaire");

    final var outputMessage = new MockHttpOutputMessage();
    converter.write(questionnaire, MediaType.APPLICATION_JSON, outputMessage);

    final var written = outputMessage.getBodyAsString(StandardCharsets.UTF_8);
    assertTrue(written.contains("written-questionnaire"));
    assertTrue(written.contains("Written Questionnaire"));
  }

  @Test
  void shouldSupportApplicationFhirJsonMediaType() {
    assertTrue(converter.canRead(Questionnaire.class, new MediaType("application", "fhir+json")));
  }
}
