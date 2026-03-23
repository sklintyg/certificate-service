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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageLevel;

class MessageForutsattningarForAttLamnaSkriftligtSamtyckeTest {

  private static final ElementId ELEMENT_ID = new ElementId("forutsattningar");

  @Test
  void shallIncludeId() {
    final var element =
        MessageForutsattningarForAttLamnaSkriftligtSamtycke
            .messageForutsattningarForAttLamnaSkriftligtSamtycke();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationMessage.builder()
            .message(
                ElementMessage.builder()
                    .content(
                        "Om patienten har medicinska förutsättningar att samtycka till en närståendes stöd, så ska patienten göra det. Därför ska du fylla i om hen kan samtycka eller inte.")
                    .level(MessageLevel.INFO)
                    .build())
            .build();

    final var element =
        MessageForutsattningarForAttLamnaSkriftligtSamtycke
            .messageForutsattningarForAttLamnaSkriftligtSamtycke();

    assertEquals(expectedConfiguration, element.configuration());
  }
}
