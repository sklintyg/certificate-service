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
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementLayout;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.common.model.Code;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCodeList;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeFactory;

public class QuestionCheckboxMultipleCodeColumns {

  public static final ElementId QUESTION_CHECKBOX_MULTIPLE_CODE_ID = new ElementId("3");
  public static final FieldId QUESTION_CHECKBOX_MULTIPLE_CODE_FIELD_ID = new FieldId("3.1");

  private QuestionCheckboxMultipleCodeColumns() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionCheckboxMultipleCodeColumns(
      ElementSpecification... children) {
    final var checkboxes =
        List.of(
            CodeFactory.elementConfigurationCode(new Code("1", "test", "Test 1")),
            CodeFactory.elementConfigurationCode(new Code("2", "test", "Test 2")),
            CodeFactory.elementConfigurationCode(new Code("3", "test", "Test 3")));

    return ElementSpecification.builder()
        .id(QUESTION_CHECKBOX_MULTIPLE_CODE_ID)
        .configuration(
            ElementConfigurationCheckboxMultipleCode.builder()
                .id(QUESTION_CHECKBOX_MULTIPLE_CODE_FIELD_ID)
                .name("CHECKBOX_MULTIPLE_CODE")
                .elementLayout(ElementLayout.ROWS)
                .list(checkboxes)
                .build())
        .validations(List.of(ElementValidationCodeList.builder().mandatory(false).build()))
        .children(List.of(children))
        .build();
  }
}
