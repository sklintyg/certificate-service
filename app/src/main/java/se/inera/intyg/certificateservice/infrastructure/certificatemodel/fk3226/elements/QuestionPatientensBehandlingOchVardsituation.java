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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.elements;

import java.util.List;
import java.util.Map;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementLayout;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCode;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0009;

public class QuestionPatientensBehandlingOchVardsituation {

  public static final ElementId QUESTION_PATIENTENS_BEHANDLING_OCH_VARDSITUATION_ID =
      new ElementId("52");
  public static final FieldId QUESTION_PATIENTENS_BEHANDLING_OCH_VARDSITUATION_FIELD_ID =
      new FieldId("52.1");
  public static final FieldId ENDAST_PALLIATIV_FIELD_ID = new FieldId("ENDAST_PALLIATIV");
  public static final FieldId AKUT_LIVSHOTANDE_FIELD_ID = new FieldId("AKUT_LIVSHOTANDE");
  public static final FieldId ANNAT_FIELD_ID = new FieldId("ANNAT");

  private static final PdfFieldId PDF_ONLY_PALLIATIVE_CARE_FIELD_ID =
      new PdfFieldId("form1[0].#subform[1].ksr_PalliativVard[0]");
  private static final PdfFieldId PDF_ACUTE_LIFE_THREATENING_FIELD_ID =
      new PdfFieldId("form1[0].#subform[1].ksr_AkutLivshotande[0]");
  private static final PdfFieldId PDF_OTHER_THREAT_FIELD_ID =
      new PdfFieldId("form1[0].#subform[1].ksr_Annat2[0]");

  private QuestionPatientensBehandlingOchVardsituation() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionPatientBehandlingOchVardsituation(
      ElementSpecification... children) {
    final var radioMultipleCodes =
        List.of(
            new ElementConfigurationCode(
                ENDAST_PALLIATIV_FIELD_ID,
                CodeSystemKvFkmu0009.ENDAST_PALLIATIV.displayName(),
                CodeSystemKvFkmu0009.ENDAST_PALLIATIV),
            new ElementConfigurationCode(
                AKUT_LIVSHOTANDE_FIELD_ID,
                CodeSystemKvFkmu0009.AKUT_LIVSHOTANDE.displayName(),
                CodeSystemKvFkmu0009.AKUT_LIVSHOTANDE),
            new ElementConfigurationCode(
                ANNAT_FIELD_ID,
                CodeSystemKvFkmu0009.ANNAT.displayName(),
                CodeSystemKvFkmu0009.ANNAT));

    return ElementSpecification.builder()
        .id(QUESTION_PATIENTENS_BEHANDLING_OCH_VARDSITUATION_ID)
        .configuration(
            ElementConfigurationRadioMultipleCode.builder()
                .id(QUESTION_PATIENTENS_BEHANDLING_OCH_VARDSITUATION_FIELD_ID)
                .name("Patientens behandling och vårdsituation")
                .elementLayout(ElementLayout.ROWS)
                .list(radioMultipleCodes)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryOrExist(
                    QUESTION_PATIENTENS_BEHANDLING_OCH_VARDSITUATION_ID,
                    radioMultipleCodes.stream().map(ElementConfigurationCode::id).toList())))
        .validations(List.of(ElementValidationCode.builder().mandatory(true).build()))
        .children(List.of(children))
        .pdfConfiguration(
            PdfConfigurationCode.builder()
                .codes(
                    Map.of(
                        ENDAST_PALLIATIV_FIELD_ID, PDF_ONLY_PALLIATIVE_CARE_FIELD_ID,
                        AKUT_LIVSHOTANDE_FIELD_ID, PDF_ACUTE_LIFE_THREATENING_FIELD_ID,
                        ANNAT_FIELD_ID, PDF_OTHER_THREAT_FIELD_ID))
                .build())
        .build();
  }
}
