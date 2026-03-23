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

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageLevel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;

class MessageNedsattningArbetsformagaStartDateInfoTest {

  private static final ElementId ELEMENT_ID = new ElementId("earlyStartDate");

  @Test
  void shallIncludeId() {
    final var element = MessageNedsattningArbetsformagaStartDateInfo.messageStartDateInfo();
    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationMessage.builder()
            .message(
                ElementMessage.builder()
                    .content(
                        "Du har angett att sjukskrivningsperioden startar mer än 7 dagar bakåt i tiden. Ange orsaken till detta i fältet \"Övriga upplysningar\".")
                    .level(MessageLevel.OBSERVE)
                    .build())
            .build();
    final var element = MessageNedsattningArbetsformagaStartDateInfo.messageStartDateInfo();
    assertEquals(expectedConfiguration, element.configuration());
  }

  @Test
  void shallIncludeShowRuleWithCorrectExpression() {
    final var element = MessageNedsattningArbetsformagaStartDateInfo.messageStartDateInfo();
    final var rules =
        List.of(
            ElementRuleExpression.builder()
                .type(ElementRuleType.SHOW)
                .id(new ElementId("32"))
                .expression(
                    new RuleExpression(
                        "($EN_FJARDEDEL.from < -7) || ($HALFTEN.from < -7) || ($TRE_FJARDEDEL.from < -7) || ($HELT_NEDSATT.from < -7)"))
                .build());

    assertEquals(rules, element.rules());
  }
}
