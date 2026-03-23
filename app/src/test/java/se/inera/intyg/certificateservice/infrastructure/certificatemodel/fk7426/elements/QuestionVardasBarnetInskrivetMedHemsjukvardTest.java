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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.elements.QuestionVardasBarnetInskrivetMedHemsjukvard.questionVardasBarnetInskrivetMedHemsjukvard;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

class QuestionVardasBarnetInskrivetMedHemsjukvardTest {

  private static final ElementId ELEMENT_ID = new ElementId("62.3");
  private static final FieldId FIELD_ID = new FieldId("62.3");
  private static final ElementId MAPPING_ID = new ElementId("62");

  @Test
  void shallIncludeId() {
    final var elementSpecification = questionVardasBarnetInskrivetMedHemsjukvard();
    assertEquals(ELEMENT_ID, elementSpecification.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var configurationRadioBoolean =
        ElementConfigurationRadioBoolean.builder()
            .id(FIELD_ID)
            .selectedText("Ja")
            .unselectedText("Nej")
            .name("Är barnet inskrivet med hemsjukvård?")
            .build();

    final var elementSpecification = questionVardasBarnetInskrivetMedHemsjukvard();
    assertEquals(configurationRadioBoolean, elementSpecification.configuration());
  }

  @Test
  void shallIncludeValidation() {
    final var expectedValidation =
        List.of(ElementValidationBoolean.builder().mandatory(true).build());

    final var elementSpecification = questionVardasBarnetInskrivetMedHemsjukvard();
    assertEquals(expectedValidation, elementSpecification.validations());
  }

  @Test
  void shallIncludeRules() {
    final var expectedRules =
        List.of(CertificateElementRuleFactory.mandatoryExist(ELEMENT_ID, FIELD_ID));

    final var elementSpecification = questionVardasBarnetInskrivetMedHemsjukvard();
    assertEquals(expectedRules, elementSpecification.rules());
  }

  @Test
  void shallIncludeMapping() {
    final var expectedMapping = new ElementMapping(MAPPING_ID, null);
    final var elementSpecification = questionVardasBarnetInskrivetMedHemsjukvard();
    assertEquals(expectedMapping, elementSpecification.mapping());
  }

  @Test
  void shallIncludeChildren() {
    final var expectedChild = questionVardasBarnetInskrivetMedHemsjukvard();
    final var elementSpecification = questionVardasBarnetInskrivetMedHemsjukvard(expectedChild);
    assertEquals(List.of(expectedChild), elementSpecification.children());
  }

  @Test
  void shallIncludePdfConfiguration() {
    final var expectedPdfConfiguration =
        PdfConfigurationBoolean.builder()
            .checkboxFalse(new PdfFieldId("form1[0].#subform[3].ksr_Nej_2[0]"))
            .checkboxTrue(new PdfFieldId("form1[0].#subform[3].ksr_Ja_2[0]"))
            .build();

    final var elementSpecification = questionVardasBarnetInskrivetMedHemsjukvard();
    assertEquals(expectedPdfConfiguration, elementSpecification.pdfConfiguration());
  }

  @Test
  void shouldSetIncludeWhenRenewingToFalse() {
    final var elementSpecification = questionVardasBarnetInskrivetMedHemsjukvard();
    assertFalse(elementSpecification.includeWhenRenewing());
  }
}
