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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageLevel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;

class MessageDiabetesV1Test {

  private static final ElementId ELEMENT_ID = new ElementId("diabetes");

  @Test
  void shallIncludeId() {
    final var element = MessageDiabetesV1.messageDiabetesV1();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeRules() {
    final var element = MessageDiabetesV1.messageDiabetesV1();

    assertEquals(
        List.of(
            ElementRuleExpression.builder()
                .id(new ElementId("12"))
                .expression(new RuleExpression("$12.1"))
                .type(ElementRuleType.SHOW)
                .build()),
        element.rules());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationMessage.builder()
            .message(
                ElementMessage.builder()
                    .content(
                        "Har personen läkemedelsbehandlad diabetes krävs normalt ett särskilt läkarintyg om detta. Om personen redan har ett villkor om att skicka in ett nytt diabetesintyg till Transportstyrelsen i framtiden är det dock inte alltid som ett sådant intyg krävs vid prövningen av ansökan. Diabetesintyg går att skicka in digitalt via Webcert. Intygsblanketten finns också på <LINK:transportstyrelsenLink>.")
                    .level(MessageLevel.OBSERVE)
                    .build())
            .build();

    final var element = MessageDiabetesV1.messageDiabetesV1();

    assertEquals(expectedConfiguration, element.configuration());
  }
}
