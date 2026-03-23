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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.v1;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.v1.QuestionNeuropsykiatriskV1.QUESTION_NEUROPSYKIATRISK_V1_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.v1.QuestionPsykiskUtvecklingsstorningV1.QUESTION_PSYKISK_UTVECKLINGSSTORNING_FIELD_V1_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.v1.QuestionPsykiskUtvecklingsstorningV1.QUESTION_PSYKISK_UTVECKLINGSSTORNING_V1_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionPsykiskUtvecklingsstorningAllvarligV1 {

  public static final ElementId QUESTION_PSYKISK_UTVECKLINGSSTORNING_ALLVARLIG_V1_ID =
      new ElementId("20.7");
  public static final FieldId QUESTION_PSYKISK_UTVECKLINGSSTORNING_ALLVARLIG_FIELD_V1_ID =
      new FieldId("20.7");

  private QuestionPsykiskUtvecklingsstorningAllvarligV1() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionPsykiskUtvecklingsstorningAllvarligV1() {
    return ElementSpecification.builder()
        .id(QUESTION_PSYKISK_UTVECKLINGSSTORNING_ALLVARLIG_V1_ID)
        .configuration(
            ElementConfigurationRadioBoolean.builder()
                .id(QUESTION_PSYKISK_UTVECKLINGSSTORNING_ALLVARLIG_FIELD_V1_ID)
                .name("Är det en allvarlig psykisk utvecklingsstörning?")
                .description(
                    "Med allvarlig psykisk utvecklingsstörning avses mental retardation enligt DSM-IV. Det avser även grav, svår eller medelsvår psykisk utvecklingsstörning enligt ICD-10. Intellektuell funktionsnedsättning enligt DSM-5 av djupgående, svår eller måttlig grad är att jämställa med ovan.")
                .selectedText("Ja")
                .unselectedText("Nej")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.show(
                    QUESTION_PSYKISK_UTVECKLINGSSTORNING_V1_ID,
                    QUESTION_PSYKISK_UTVECKLINGSSTORNING_FIELD_V1_ID),
                CertificateElementRuleFactory.mandatoryExist(
                    QUESTION_PSYKISK_UTVECKLINGSSTORNING_ALLVARLIG_V1_ID,
                    QUESTION_PSYKISK_UTVECKLINGSSTORNING_ALLVARLIG_FIELD_V1_ID)))
        .shouldValidate(
            ElementDataPredicateFactory.valueBoolean(QUESTION_PSYKISK_UTVECKLINGSSTORNING_V1_ID))
        .mapping(new ElementMapping(QUESTION_NEUROPSYKIATRISK_V1_ID, null))
        .validations(List.of(ElementValidationBoolean.builder().mandatory(true).build()))
        .build();
  }
}
