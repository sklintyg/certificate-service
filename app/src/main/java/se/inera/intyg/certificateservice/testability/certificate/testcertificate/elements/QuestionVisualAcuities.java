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
package se.inera.intyg.certificateservice.testability.certificate.testcertificate.elements;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationVisualAcuities;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementVisualAcuity;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationVisualAcuities;

public class QuestionVisualAcuities {

  private QuestionVisualAcuities() {
    throw new IllegalStateException("Utility class");
  }

  public static final ElementId QUESTION_VISUAL_ACUITIES_ID = new ElementId("14");
  public static final FieldId QUESTION_VISUAL_ACUITIES_FIELD_ID = new FieldId("14.1");
  public static final String TEST1_ID = "14.1";
  public static final String TEST2_ID = "14.2";
  public static final String TEST3_ID = "14.3";
  public static final String TEST4_ID = "14.4";
  public static final String TEST5_ID = "14.5";
  public static final String TEST6_ID = "14.6";

  public static ElementSpecification questionVisualAcuities() {
    return ElementSpecification.builder()
        .id(QUESTION_VISUAL_ACUITIES_ID)
        .configuration(
            ElementConfigurationVisualAcuities.builder()
                .id(QUESTION_VISUAL_ACUITIES_FIELD_ID)
                .name("VISUAL_ACUITIES")
                .withCorrectionLabel("Med korrektion")
                .withoutCorrectionLabel("Utan korrektion")
                .min(0.0)
                .max(2.0)
                .rightEye(
                    ElementVisualAcuity.builder()
                        .label("Höger öga")
                        .withoutCorrectionId(TEST1_ID)
                        .withCorrectionId(TEST2_ID)
                        .build())
                .leftEye(
                    ElementVisualAcuity.builder()
                        .label("Vänster öga")
                        .withoutCorrectionId(TEST3_ID)
                        .withCorrectionId(TEST4_ID)
                        .build())
                .binocular(
                    ElementVisualAcuity.builder()
                        .label("Binokulärt")
                        .withoutCorrectionId(TEST5_ID)
                        .withCorrectionId(TEST6_ID)
                        .build())
                .build())
        .validations(List.of(ElementValidationVisualAcuities.builder().mandatory(false).build()))
        .build();
  }
}
