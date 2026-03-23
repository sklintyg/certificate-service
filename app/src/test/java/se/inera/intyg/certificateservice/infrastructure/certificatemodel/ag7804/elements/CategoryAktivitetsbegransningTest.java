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
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.CategoryAktivitetsbegransning.categoryAktivitetsbegransning;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

class CategoryAktivitetsbegransningTest {

  private static final ElementId ELEMENT_ID = new ElementId("KAT_6");

  @Test
  void shallIncludeId() {
    final var element = categoryAktivitetsbegransning();
    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCategory.builder()
            .name("Aktivitetsbegränsning")
            .description(
                """
            Beskriv aktivitetsbegränsningens svårighetsgrad, till exempel lätt, medelsvår, svår, total eller motsvarande.

            Om patienten är arbetssökande eller har varit sjukskriven en längre tid kan det vara svårt att beskriva aktivitetsbegränsningar enbart i relation till patientens nuvarande arbetsuppgifter. Då kan det vara bra att även beskriva hur aktivitetsbegränsningarna yttrar sig i andra situationer, t. ex vardagliga situationer.
            """)
            .build();

    final var element = categoryAktivitetsbegransning();

    assertEquals(expectedConfiguration, element.elementSpecification(ELEMENT_ID).configuration());
  }
}
