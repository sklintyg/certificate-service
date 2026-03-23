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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7809.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

class CategoryAktivitetsbegransningarTest {

  private static final ElementId ELEMENT_ID = new ElementId("KAT_6");

  @Test
  void shallIncludeId() {
    final var element = CategoryAktivitetsbegransningar.categoryAktivitetsbegransningar();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCategory.builder()
            .name("Aktivitetsbegränsningar")
            .description(
                """
                Beskriv de aktivitetsbegränsningar som du bedömer att patienten har. Beskriv även om din bedömning är baserad på observationer, anamnes eller utredning gjord av någon annan. Någon annan kan till exempel vara psykolog, arbetsterapeut, audionom, syn- eller hörselpedagog.

                I beskrivningen kan du utgå från aktiviteter inom områden som till exempel kommunikation, förflyttning, personlig vård och hemliv. Ange om möjligt svårighetsgraden på aktivitetsbegränsningarna.
                """)
            .build();

    final var element = CategoryAktivitetsbegransningar.categoryAktivitetsbegransningar();

    assertEquals(expectedConfiguration, element.configuration());
  }
}
