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
package se.inera.intyg.certificateservice.application.message.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.message.model.Complement;
import se.inera.intyg.certificateservice.domain.message.model.Content;

class ComplementConverterTest {

  private static final ElementId ELEMENT_SPECIFICATION_ID = new ElementId("elementSpecificationId");
  private static final ElementId MISSING_ELEMENT_SPECIFICATION_ID =
      new ElementId("missingElementSpecificationId");
  private static final String QUESTION_TEXT = "questionText";
  private static final Content CONTENT = new Content("content");
  private ComplementConverter complementConverter;
  private Complement complement;
  private Certificate certificate;

  @BeforeEach
  void setUp() {
    complementConverter = new ComplementConverter();
    certificate =
        MedicalCertificate.builder()
            .certificateModel(
                CertificateModel.builder()
                    .elementSpecifications(
                        List.of(
                            ElementSpecification.builder()
                                .id(ELEMENT_SPECIFICATION_ID)
                                .configuration(
                                    ElementConfigurationDate.builder().name(QUESTION_TEXT).build())
                                .build()))
                    .build())
            .build();

    complement = Complement.builder().elementId(ELEMENT_SPECIFICATION_ID).content(CONTENT).build();
  }

  @Test
  void shallIncludeQuestionId() {
    assertEquals(
        ELEMENT_SPECIFICATION_ID.id(),
        complementConverter.convert(complement, certificate).getQuestionId());
  }

  @Test
  void shallIncludeQuestionText() {
    assertEquals(
        QUESTION_TEXT, complementConverter.convert(complement, certificate).getQuestionText());
  }

  @Test
  void shallIncludeMessage() {
    assertEquals(
        CONTENT.content(), complementConverter.convert(complement, certificate).getMessage());
  }

  @Nested
  class ComplementingQuestionIdThatIsMissing {

    @BeforeEach
    void setUp() {
      complement =
          Complement.builder().elementId(MISSING_ELEMENT_SPECIFICATION_ID).content(CONTENT).build();
    }

    @Test
    void shallIncludeQuestionId() {
      assertEquals(
          MISSING_ELEMENT_SPECIFICATION_ID.id(),
          complementConverter.convert(complement, certificate).getQuestionId());
    }

    @Test
    void shallIncludeQuestionText() {
      assertEquals(
          MISSING_ELEMENT_SPECIFICATION_ID.id(),
          complementConverter.convert(complement, certificate).getQuestionText());
    }

    @Test
    void shallIncludeMessage() {
      assertEquals(
          CONTENT.content(), complementConverter.convert(complement, certificate).getMessage());
    }
  }
}
