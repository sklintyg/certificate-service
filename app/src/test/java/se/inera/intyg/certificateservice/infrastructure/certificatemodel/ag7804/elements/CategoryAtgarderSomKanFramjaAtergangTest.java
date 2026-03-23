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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.CategoryAtgarderSomKanFramjaAtergang.categoryAtgarderSomKanFramjaAttergang;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionSmittbararpenning.QUESTION_SMITTBARARPENNING_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionSmittbararpenning.QUESTION_SMITTBARARPENNING_ID;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;

class CategoryAtgarderSomKanFramjaAtergangTest {

  private static final ElementId ELEMENT_ID = new ElementId("KAT_10");

  @Test
  void shallIncludeId() {
    final var element = categoryAtgarderSomKanFramjaAttergang();
    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCategory.builder()
            .name("Åtgärder som kan främja återgången i arbete")
            .build();

    final var element = categoryAtgarderSomKanFramjaAttergang();

    assertEquals(expectedConfiguration, element.elementSpecification(ELEMENT_ID).configuration());
  }

  @Test
  void shallIncludeRules() {
    final var element = categoryAtgarderSomKanFramjaAttergang();
    final var expectedRules =
        List.of(
            ElementRuleExpression.builder()
                .id(QUESTION_SMITTBARARPENNING_ID)
                .type(ElementRuleType.HIDE)
                .expression(new RuleExpression("$" + QUESTION_SMITTBARARPENNING_FIELD_ID.value()))
                .build());
    assertEquals(expectedRules, element.rules());
  }
}
