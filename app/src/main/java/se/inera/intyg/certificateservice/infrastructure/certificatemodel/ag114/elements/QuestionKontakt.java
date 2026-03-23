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

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;

public class QuestionKontakt {

  public static final ElementId QUESTION_KONTAKT_ID = new ElementId("9");
  public static final FieldId QUESTION_KONTAKT_FIELD_ID = new FieldId("9.1");

  private QuestionKontakt() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionKontakt(ElementSpecification... children) {
    return ElementSpecification.builder()
        .id(QUESTION_KONTAKT_ID)
        .configuration(
            ElementConfigurationCheckboxBoolean.builder()
                .id(QUESTION_KONTAKT_FIELD_ID)
                .name("Kontakt med arbetsgivaren")
                .label(
                    "Jag önskar att arbetsgivaren kontaktar vårdenheten. Patienten har lämnat samtycke för kontakt mellan arbetsgivare och vårdgivare.")
                .selectedText("Ja")
                .unselectedText("Ej angivet")
                .build())
        .validations(List.of(ElementValidationBoolean.builder().mandatory(false).build()))
        .children(List.of(children))
        .build();
  }
}
