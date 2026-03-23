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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.v2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

class CategoryAlkoholOchLakemedelV2Test {

  private static final ElementId ELEMENT_ID = new ElementId("KAT_12");

  @Test
  void shouldIncludeId() {
    final var element = CategoryAlkoholOchLakemedelV2.categoryAlkoholOchLakemedelV2();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shouldIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCategory.builder()
            .name("Alkohol, andra psykoaktiva substanser och läkemedel")
            .build();

    final var element = CategoryAlkoholOchLakemedelV2.categoryAlkoholOchLakemedelV2();

    assertEquals(expectedConfiguration, element.configuration());
  }
}
