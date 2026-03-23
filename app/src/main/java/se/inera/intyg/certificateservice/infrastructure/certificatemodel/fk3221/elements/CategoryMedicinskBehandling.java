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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3221.elements;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;

public class CategoryMedicinskBehandling {

  private static final ElementId MEDICINSK_BEHANDLING_CATEGORY_ID = new ElementId("KAT_6");

  private CategoryMedicinskBehandling() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification categoryMedicinskBehandling(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(MEDICINSK_BEHANDLING_CATEGORY_ID)
        .configuration(
            ElementConfigurationCategory.builder()
                .name("Medicinska behandlingar")
                .description(
                    "Ange pågående och planerade medicinska behandlingar eller åtgärder som är relevanta utifrån funktionsnedsättningen. Det kan vara ordinerade läkemedel, hjälpmedel, träningsinsatser eller särskild kost. Ange den medicinska indikationen och syftet med behandlingen eller åtgärden. Du kan även beskriva andra behandlingar och åtgärder som prövats utifrån funktionsnedsättningen. Om barnet använder läkemedel som inte är subventionerade: beskriv anledningen till det.")
                .build())
        .children(List.of(children))
        .build();
  }
}
