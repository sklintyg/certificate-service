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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionBalanssinne {

  public static final ElementId QUESTION_BALANSSINNE_ID = new ElementId("8");
  public static final FieldId QUESTION_BALANSSINNE_FIELD_ID = new FieldId("8.1");

  private QuestionBalanssinne() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionBalanssinne(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(QUESTION_BALANSSINNE_ID)
        .configuration(
            ElementConfigurationRadioBoolean.builder()
                .id(QUESTION_BALANSSINNE_FIELD_ID)
                .description(
                    "Här avses överraskande anfall av balansrubbningar eller yrsel som inträffat nyligen och krävt läkarkontakt, exempelvis vid sjukdomen Morbus Menière. Balansrubbningar eller yrsel som beror på till exempel godartad lägesyrsel (kristallsjuka), lågt blodtryck eller migrän "
                        + "behöver inte anges.")
                .selectedText("Ja")
                .unselectedText("Nej")
                .name(
                    "Har personen överraskande anfall av balansrubbningar eller yrsel som kan innebära en trafiksäkerhetsrisk?")
                .build())
        .validations(List.of(ElementValidationBoolean.builder().mandatory(true).build()))
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryExist(
                    QUESTION_BALANSSINNE_ID, QUESTION_BALANSSINNE_FIELD_ID)))
        .children(List.of(children))
        .build();
  }
}
