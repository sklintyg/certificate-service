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
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

class CategoryHotTest {

  private static final ElementId ELEMENT_ID = new ElementId("KAT_2");

  @Test
  void shallIncludeId() {
    final var element = CategoryHot.categoryHot();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCategory.builder()
            .name("Påtagligt hot mot patientens liv")
            .description(
                """
            Ange på vilket sätt hälsotillståndet utgör ett påtagligt hot mot patientens liv i nuläget eller på viss tids sikt.

            Hälsotillståndet kan utgöra ett påtagligt hot även om det finns hopp om att det förbättras.
            <ul>
            <li>Ange alla diagnoser som sammantaget medför ett påtagligt hot mot patientens liv.</li><li>Ange ett av alternativen som gäller patientens behandling och vårdsituation.</li></ul>""")
            .build();

    final var element = CategoryHot.categoryHot();

    assertEquals(expectedConfiguration, element.elementSpecification(ELEMENT_ID).configuration());
  }
}
