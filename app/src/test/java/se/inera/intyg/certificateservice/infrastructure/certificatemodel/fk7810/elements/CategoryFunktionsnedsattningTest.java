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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7810.elements.CategoryFunktionsnedsattning.categoryFunktionsnedsattning;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

class CategoryFunktionsnedsattningTest {

  private static final ElementId ELEMENT_ID = new ElementId("KAT_4");

  @Test
  void shallIncludeId() {
    final var element = categoryFunktionsnedsattning();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCategory.builder()
            .name("Funktionsnedsättning")
            .description(
                """
                Utifrån diagnoserna ovan, beskriv eventuell funktionsnedsättning för respektive funktionsområde samt gradering till exempel enligt internationell klassifikation av funktionstillstånd, funktionshinder och hälsa (ICF) (lätt, måttlig, stor eller total).

                Basera beskrivningen på vad som framkommit vid senaste undersökningstillfället och tidigare utredningar. Ange vilka status- och undersökningsfynd du baserar bedömningen på.""")
            .build();

    final var element = categoryFunktionsnedsattning();

    assertEquals(expectedConfiguration, element.configuration());
  }
}
