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
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDropdownCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.common.model.Code;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationCode;

public class QuestionDropdown {

  public static final ElementId QUESTION_DROPDOWN_ID = new ElementId("8");
  private static final FieldId QUESTION_DROPDOWN_FIELD_ID = new FieldId("8.1");

  private QuestionDropdown() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionDropdown(ElementSpecification... children) {
    final var dropdownItems =
        List.of(
            new ElementConfigurationCode(new FieldId(""), "Välj i listan", null),
            new ElementConfigurationCode(
                new FieldId("Test1"), "TEST 1", new Code("TEST_1", "TEST", "TEST 1")),
            new ElementConfigurationCode(
                new FieldId("Test2"), "TEST 2", new Code("TEST_2", "TEST", "TEST 2")),
            new ElementConfigurationCode(
                new FieldId("Test3"), "TEST 3", new Code("TEST_3", "TEST", "TEST 3")),
            new ElementConfigurationCode(
                new FieldId("Test4"), "TEST 4", new Code("TEST_4", "TEST", "TEST 4")));

    return ElementSpecification.builder()
        .id(QUESTION_DROPDOWN_ID)
        .configuration(
            ElementConfigurationDropdownCode.builder()
                .id(QUESTION_DROPDOWN_FIELD_ID)
                .name("DROPDOWN")
                .list(dropdownItems)
                .build())
        .validations(List.of(ElementValidationCode.builder().mandatory(false).build()))
        .children(List.of(children))
        .build();
  }
}
