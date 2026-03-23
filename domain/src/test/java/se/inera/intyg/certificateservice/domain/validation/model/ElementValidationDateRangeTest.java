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
package se.inera.intyg.certificateservice.domain.validation.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRange;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValidationDateRangeTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final FieldId FIELD_ID = new FieldId("date_range");
  private static final ElementId CATEGORY_ID = new ElementId("categoryId");
  private ElementValidationDateRange elementValidationDateRange;

  @Nested
  class IllegalStates {

    @BeforeEach
    void setUp() {
      elementValidationDateRange = ElementValidationDateRange.builder().build();
    }

    @Test
    void shallThrowIllegalArgumentExceptionIfDataIsNull() {
      final Optional<ElementId> categoryId = Optional.empty();
      assertThrows(
          IllegalArgumentException.class,
          () -> elementValidationDateRange.validate(null, categoryId, Collections.emptyList()));
    }

    @Test
    void shallThrowIllegalArgumentExceptionIfValueIsNull() {
      final Optional<ElementId> categoryId = Optional.empty();
      final var elementData = ElementData.builder().id(ELEMENT_ID).build();

      assertThrows(
          IllegalArgumentException.class,
          () ->
              elementValidationDateRange.validate(
                  elementData, categoryId, Collections.emptyList()));
    }
  }

  @Nested
  class Mandatory {

    @BeforeEach
    void setUp() {
      elementValidationDateRange = ElementValidationDateRange.builder().mandatory(true).build();
    }

    @Test
    void shouldReturnValidationErrorIfDateIsEmpty() {
      final var expectedValidationError =
          getExpectedValidationError("Ange period.", new FieldId(FIELD_ID.value()));
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueDateRange.builder().id(FIELD_ID).build())
              .build();

      final var actualResult =
          elementValidationDateRange.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(List.of(expectedValidationError), actualResult);
    }

    @Test
    void shouldReturnValidationErrorIfFromDateIsMissing() {
      final var expectedValidationError =
          getExpectedValidationError("Ange ett datum.", new FieldId(FIELD_ID.value() + ".from"));
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueDateRange.builder().id(FIELD_ID).toDate(LocalDate.now()).build())
              .build();

      final var actualResult =
          elementValidationDateRange.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(List.of(expectedValidationError), actualResult);
    }

    @Test
    void shouldReturnValidationErrorIfToDateIsMissing() {
      final var expectedValidationError =
          getExpectedValidationError("Ange ett datum.", new FieldId(FIELD_ID.value() + ".to"));
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueDateRange.builder().id(FIELD_ID).fromDate(LocalDate.now()).build())
              .build();

      final var actualResult =
          elementValidationDateRange.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(List.of(expectedValidationError), actualResult);
    }

    @Test
    void shouldReturnValidationErrorIfToDateIsBeforeFromDate() {
      final var expectedValidationError =
          getExpectedValidationError(
              "Ange ett slutdatum som infaller efter startdatumet.",
              new FieldId(FIELD_ID.value() + ".range"));
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(
                  ElementValueDateRange.builder()
                      .id(FIELD_ID)
                      .fromDate(LocalDate.now())
                      .toDate(LocalDate.now().minusDays(1))
                      .build())
              .build();

      final var actualResult =
          elementValidationDateRange.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(List.of(expectedValidationError), actualResult);
    }
  }

  @Nested
  class NotMandatory {

    @BeforeEach
    void setUp() {
      elementValidationDateRange = ElementValidationDateRange.builder().mandatory(false).build();
    }

    @Test
    void shouldNotReturnValidationErrorIfFromDateAndToDateAreMissing() {
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueDateRange.builder().id(FIELD_ID).build())
              .build();

      final var actualResult =
          elementValidationDateRange.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(Collections.emptyList(), actualResult);
    }

    @Test
    void shouldReturnValidationErrorIfFromDateIsMissing() {
      final var expectedValidationError =
          getExpectedValidationError("Ange ett datum.", new FieldId(FIELD_ID.value() + ".from"));
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueDateRange.builder().id(FIELD_ID).toDate(LocalDate.now()).build())
              .build();

      final var actualResult =
          elementValidationDateRange.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(List.of(expectedValidationError), actualResult);
    }

    @Test
    void shouldReturnValidationErrorIfToDateIsMissing() {
      final var expectedValidationError =
          getExpectedValidationError("Ange ett datum.", new FieldId(FIELD_ID.value() + ".to"));
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueDateRange.builder().id(FIELD_ID).fromDate(LocalDate.now()).build())
              .build();

      final var actualResult =
          elementValidationDateRange.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(List.of(expectedValidationError), actualResult);
    }

    @Test
    void shouldReturnValidationErrorIfToDateIsBeforeFromDate() {
      final var expectedValidationError =
          getExpectedValidationError(
              "Ange ett slutdatum som infaller efter startdatumet.",
              new FieldId(FIELD_ID.value() + ".range"));
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(
                  ElementValueDateRange.builder()
                      .id(FIELD_ID)
                      .fromDate(LocalDate.now())
                      .toDate(LocalDate.now().minusDays(1))
                      .build())
              .build();

      final var actualResult =
          elementValidationDateRange.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(List.of(expectedValidationError), actualResult);
    }
  }

  private static ValidationError getExpectedValidationError(String message, FieldId fieldId) {
    return ValidationError.builder()
        .elementId(ELEMENT_ID)
        .fieldId(fieldId)
        .categoryId(CATEGORY_ID)
        .message(new ErrorMessage(message))
        .build();
  }
}
