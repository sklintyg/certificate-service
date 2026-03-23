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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionSynfunktioner.QUESTION_SYNFUNKTIONER_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionSynfunktioner.QUESTION_SYNFUNKTIONER_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class CategorySynskarpa {

  private static final ElementId INTYGET_SYNSKARPA_CATEGORY_ID = new ElementId("KAT_1.1");

  private CategorySynskarpa() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification categorySynskarpa(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(INTYGET_SYNSKARPA_CATEGORY_ID)
        .configuration(
            ElementConfigurationCategory.builder()
                .name("Synskärpa")
                .description(
                    "Uppgifter om synskärpa med korrektion, om det vid undersökningen behövs korrektion för att uppfylla kraven i 2 kap. 1 eller 2 §§ medicinföreskrifterna.")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.showIfNot(
                    QUESTION_SYNFUNKTIONER_ID, QUESTION_SYNFUNKTIONER_FIELD_ID)))
        .children(List.of(children))
        .build();
  }
}
