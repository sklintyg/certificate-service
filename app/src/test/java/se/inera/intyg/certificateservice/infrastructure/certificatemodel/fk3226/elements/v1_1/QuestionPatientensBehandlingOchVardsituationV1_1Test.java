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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.elements.v1_1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementLayout;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCode;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0009;

class QuestionPatientensBehandlingOchVardsituationV1_1Test {

  private static final ElementId ELEMENT_ID = new ElementId("52");

  @Test
  void shallIncludeId() {
    final var element =
        QuestionPatientensBehandlingOchVardsituationV1_1
            .questionPatientBehandlingOchVardsituation();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationRadioMultipleCode.builder()
            .id(new FieldId("52.1"))
            .name("Patientens vårdsituation")
            .elementLayout(ElementLayout.ROWS)
            .list(
                List.of(
                    new ElementConfigurationCode(
                        new FieldId("ENDAST_PALLIATIV"),
                        "Vård i patientens sista tid i livet (dagar, veckor eller månader)",
                        CodeSystemKvFkmu0009.ENDAST_PALLIATIV_V2),
                    new ElementConfigurationCode(
                        new FieldId("AKUT_LIVSHOTANDE"),
                        "Akut livshotande tillstånd (till exempel vård på intensivvårdsavdelning)",
                        CodeSystemKvFkmu0009.AKUT_LIVSHOTANDE),
                    new ElementConfigurationCode(
                        new FieldId("ANNAT"), "Annat", CodeSystemKvFkmu0009.ANNAT)))
            .build();

    final var element =
        QuestionPatientensBehandlingOchVardsituationV1_1
            .questionPatientBehandlingOchVardsituation();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shallIncludeRules() {
    final var expectedRules =
        List.of(
            ElementRuleExpression.builder()
                .id(ELEMENT_ID)
                .type(ElementRuleType.MANDATORY)
                .expression(
                    new RuleExpression(
                        "exists($ENDAST_PALLIATIV) || exists($AKUT_LIVSHOTANDE) || exists($ANNAT)"))
                .build());

    final var element =
        QuestionPatientensBehandlingOchVardsituationV1_1
            .questionPatientBehandlingOchVardsituation();

    assertEquals(expectedRules, element.rules());
  }

  @Test
  void shallIncludeValidations() {
    final var expectedValidations =
        List.of(ElementValidationCode.builder().mandatory(true).build());

    final var element =
        QuestionPatientensBehandlingOchVardsituationV1_1
            .questionPatientBehandlingOchVardsituation();

    assertEquals(expectedValidations, element.validations());
  }

  @Test
  void shallIncludePdfConfiguration() {
    final var expected =
        PdfConfigurationCode.builder()
            .codes(
                Map.of(
                    new FieldId("ENDAST_PALLIATIV"),
                    new PdfFieldId("form1[0].#subform[1].ksr_PalliativVard[0]"),
                    new FieldId("AKUT_LIVSHOTANDE"),
                    new PdfFieldId("form1[0].#subform[1].ksr_AkutLivshotande[0]"),
                    new FieldId("ANNAT"),
                    new PdfFieldId("form1[0].#subform[1].ksr_Annat2[0]")))
            .build();

    final var element =
        QuestionPatientensBehandlingOchVardsituationV1_1
            .questionPatientBehandlingOchVardsituation();

    assertEquals(expected, element.pdfConfiguration());
  }
}
