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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.v1;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;

public class CategoryOvrigKommentarV1 {

  private static final ElementId INTYGET_OVRIG_KOMMENTAR_CATEGORY_V1_ID = new ElementId("KAT_16");

  private CategoryOvrigKommentarV1() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification categoryOvrigKommentarV1(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(INTYGET_OVRIG_KOMMENTAR_CATEGORY_V1_ID)
        .configuration(
            ElementConfigurationCategory.builder()
                .name("Övrig kommentar")
                .description(
                    "Övriga upplysningar som är relevanta ur trafiksäkerhetssynpunkt. Även exempelvis synpunkter/förslag om att personen bör genomgå ytterligare medicinsk utredning och orsak till detta.")
                .build())
        .children(List.of(children))
        .build();
  }
}
