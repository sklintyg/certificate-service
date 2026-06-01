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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleExpression;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementRuleType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageLevel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;

class MessageBeraknatFodelsedatumTest {

  private static final ElementId ELEMENT_ID = new ElementId("fodelsedatum");

  @Test
  void shallIncludeId() {
    final var element = MessageBeraknatFodelsedatum.messageBeraknatFodelsedatum();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeRules() {
    final var element = MessageBeraknatFodelsedatum.messageBeraknatFodelsedatum();

    assertEquals(
        List.of(
            ElementRuleExpression.builder()
                .id(new ElementId("54"))
                .expression(
                    new RuleExpression(
                        "epochDay('54.1') == %s".formatted(LocalDate.now().toEpochDay())))
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
                        """
                        Du har angivit dagens datum som beräknat födelsedatum.<br>
                        Säkerställ att datumet stämmer.
                        """)
                    .level(MessageLevel.INFO)
                    .build())
            .build();

    final var element = MessageBeraknatFodelsedatum.messageBeraknatFodelsedatum();

    assertEquals(expectedConfiguration, element.configuration());
  }
}
