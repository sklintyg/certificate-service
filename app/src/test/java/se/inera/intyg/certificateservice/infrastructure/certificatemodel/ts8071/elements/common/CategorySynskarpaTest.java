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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;

class CategorySynskarpaTest {

  private static final ElementId ELEMENT_ID = new ElementId("KAT_1.1");

  @Test
  void shallIncludeId() {
    final var element = CategorySynskarpa.categorySynskarpa();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCategory.builder()
            .name("Synskärpa")
            .description(
                "Uppgifter om synskärpa med korrektion, om det vid undersökningen behövs korrektion för att uppfylla kraven i 2 kap. 1 eller 2 §§ medicinföreskrifterna.")
            .build();

    final var element = CategorySynskarpa.categorySynskarpa();

    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shallIncludeRules() {
    final var expectedRules =
        List.of(
            ElementRuleExpression.builder()
                .id(new ElementId("4"))
                .type(ElementRuleType.SHOW)
                .expression(new RuleExpression("!$4.1 && !empty($4.1)"))
                .build());

    final var element = CategorySynskarpa.categorySynskarpa();

    assertEquals(expectedRules, element.rules());
  }
}
