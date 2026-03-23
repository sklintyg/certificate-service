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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.CategoryBedomning.categoryBedomning;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

class CategoryBedomningTest {

  private static final ElementId ELEMENT_ID = new ElementId("KAT_5");

  @Test
  void shouldIncludeId() {
    final var element = categoryBedomning();
    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shouldIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCategory.builder()
            .name("Bedömning")
            .description(
                """
            Utgångspunkten är att patientens arbetsförmåga ska bedömas i förhållande till patientens normala arbetstid.

            Under sjuklöneperioden kan läkaren bedöma arbetsförmågan steglöst. Det betyder att läkaren inte behöver begränsa sig till de fyra nivåer som gäller för Försäkringskassans bedömning av rätten till sjukpenning. En steglös bedömning kan innebära att patientens faktiska arbetsförmåga kan tas tillvara på ett mer optimalt sätt.
            """)
            .build();

    final var element = categoryBedomning();

    assertEquals(expectedConfiguration, element.elementSpecification(ELEMENT_ID).configuration());
  }
}
