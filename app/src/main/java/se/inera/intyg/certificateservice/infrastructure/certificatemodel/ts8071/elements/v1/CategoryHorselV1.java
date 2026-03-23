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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvIntygetGallerFor.ANNAT;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvIntygetGallerFor.FORLANG_GR_II_III;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvIntygetGallerFor.GR_II_III;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvIntygetGallerFor.TAXI;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvIntygetGallerFor.UTLANDSKT;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionIntygetAvser.QUESTION_INTYGET_AVSER_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class CategoryHorselV1 {

  private static final ElementId INTYGET_HORSEL_CATEGORY_V1_ID = new ElementId("KAT_3");

  private CategoryHorselV1() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification categoryHorselV1(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(INTYGET_HORSEL_CATEGORY_V1_ID)
        .configuration(ElementConfigurationCategory.builder().name("Hörsel").build())
        .children(List.of(children))
        .rules(
            List.of(
                CertificateElementRuleFactory.showOrExist(
                    QUESTION_INTYGET_AVSER_ID,
                    List.of(
                        new FieldId(GR_II_III.code()),
                        new FieldId(TAXI.code()),
                        new FieldId(UTLANDSKT.code()),
                        new FieldId(FORLANG_GR_II_III.code()),
                        new FieldId(ANNAT.code())))))
        .build();
  }
}
