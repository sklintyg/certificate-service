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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

class CategoryIdentitetTest {

  private static final ElementId ELEMENT_ID = new ElementId("KAT_0.2");

  @Test
  void shallIncludeId() {
    final var element = CategoryIdentitet.categoryIdentitet();

    assertEquals(ELEMENT_ID, element.id());
  }

  @Test
  void shallIncludeConfiguration() {
    final var expectedConfiguration =
        ElementConfigurationCategory.builder()
            .name("Identitet")
            .description(
                """
            <b iu-className="iu-fw-heading">ID-kort</b>
            Avser SIS-märkt ID-kort, svenskt nationellt ID-kort eller ID-kort utfärdat av Skatteverket.

            <b iu-className="iu-fw-heading">Företagskort eller tjänstekort</b>
            Avser SIS-märkt företagskort eller tjänstekort.

            <b iu-className="iu-fw-heading">Försäkran enligt 18 kap 4 §</b>
            Försäkran enligt 18 kap 4 § i Transportstyrelsens föreskrifter och allmänna råd om medicinska krav för innehav av körkort m.m. (TSFS2010:125). Identiteten får fastställas genom att en förälder, annan vårdnadshavare, make, maka eller sambo, registrerad partner eller myndigt barn skriftligen försäkrar att lämnade uppgifter om sökandens identitet är riktiga. Den som lämnar en sådan försäkran ska vara närvarande vid identitetskontrollen och kunna styrka sin egen identitet.

            <b iu-className="iu-fw-heading">Pass</b>
            EU-pass och pass utfärdat av Färöarna, Förenade kungariket, Island, Liechtenstein, Norge eller Schweiz
            """)
            .build();

    final var element = CategoryIdentitet.categoryIdentitet();

    assertEquals(expectedConfiguration, element.configuration());
  }
}
