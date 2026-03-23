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

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;

public class CategoryFunktionsnedsattning {

  private static final ElementId FUNKTIONSNEDSATTNING_CATEGORY_ID = new ElementId("KAT_5");

  private CategoryFunktionsnedsattning() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification categoryFunktionsnedsattning(
      ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(FUNKTIONSNEDSATTNING_CATEGORY_ID)
        .configuration(
            ElementConfigurationCategory.builder()
                .name("Funktionsnedsättning")
                .description(
                    """
                        Beskriv de funktionsnedsättningar som patienten har. Ange om din bedömning är baserad på observationer, undersökningsfynd eller testresultat. Det kan till exempel vara:
                        <ul>
                        <li>avvikelser i somatiskt och psykiskt status</li><li>röntgen- och laboratoriefynd</li><li>resultat av kliniskt fysiologiska undersökningar</li><li>andra testresultat, exempelvis neuropsykologiska.</li></ul>
                        Ange även vilka uppgifter som är baserade på anamnes. Ange om möjligt grad av funktionsnedsättning (till exempel lätt, måttlig, stor eller total).

                        Funktionsområdenas hjälptexter följer väsentligen ICF men då kategorierna i läkarutlåtandena är färre har vissa förenklingar gjorts.""")
                .build())
        .children(List.of(children))
        .build();
  }
}
