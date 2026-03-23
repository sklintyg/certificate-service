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
package se.inera.intyg.certificateservice.testability.certificate.testcertificate.elements;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextField;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;

public class QuestionTextField {

  public static final ElementId QUESTION_TEXT_FIELD_ID = new ElementId("13");
  public static final FieldId QUESTION_TEXT_FIELD_FIELD_ID = new FieldId("13.1");

  private QuestionTextField() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionTextField() {
    return ElementSpecification.builder()
        .id(QUESTION_TEXT_FIELD_ID)
        .configuration(
            ElementConfigurationTextField.builder()
                .id(QUESTION_TEXT_FIELD_FIELD_ID)
                .name("TEXT_FIELD")
                .build())
        .validations(List.of(ElementValidationText.builder().mandatory(false).build()))
        .build();
  }
}
