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
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValidationDateListTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final FieldId FIELD_ID = new FieldId("code");
  private static final FieldId FIELD_ID_DATE_ONE = new FieldId("dateIdOne");
  private static final FieldId FIELD_ID_DATE_TWO = new FieldId("dateIdTwo");
  private static final ElementId CATEGORY_ID = new ElementId("categoryId");

  private ElementValidationDateList elementValidationDateList;

  @Nested
  class IllegalStates {

    @BeforeEach
    void setUp() {
      elementValidationDateList = ElementValidationDateList.builder().build();
    }

    @Test
    void shallThrowIllegalArgumentExceptionIfDataIsNull() {
      final Optional<ElementId> categoryId = Optional.empty();
      assertThrows(
          IllegalArgumentException.class,
          () -> elementValidationDateList.validate(null, categoryId, Collections.emptyList()));
    }

    @Test
    void shallThrowIllegalArgumentExceptionIfValueIsNull() {
      final Optional<ElementId> categoryId = Optional.empty();
      final var elementData = ElementData.builder().id(ELEMENT_ID).build();

      assertThrows(
          IllegalArgumentException.class,
          () ->
              elementValidationDateList.validate(elementData, categoryId, Collections.emptyList()));
    }

    @Test
    void shallThrowIllegalArgumentExceptionIfValueIsWrongType() {
      final Optional<ElementId> categoryId = Optional.empty();
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueUnitContactInformation.builder().build())
              .build();

      assertThrows(
          IllegalArgumentException.class,
          () ->
              elementValidationDateList.validate(elementData, categoryId, Collections.emptyList()));
    }
  }

  @Nested
  class DateAfterMaxErrors {

    @Test
    void shallNotReturnErrorMessageIfDateIsAfterMaxAndMaxIsNotSet() {
      elementValidationDateList = ElementValidationDateList.builder().build();
      final var categoryId = Optional.of(CATEGORY_ID);
      final var elementData =
          ElementData.builder()
              .value(
                  ElementValueDateList.builder()
                      .dateListId(FIELD_ID)
                      .dateList(
                          List.of(
                              ElementValueDate.builder()
                                  .dateId(FIELD_ID_DATE_ONE)
                                  .date(LocalDate.now().plusDays(1))
                                  .build()))
                      .build())
              .build();

      final var validationErrors =
          elementValidationDateList.validate(elementData, categoryId, Collections.emptyList());
      assertEquals(Collections.emptyList(), validationErrors);
    }

    @Test
    void shallReturnErrorMessageIfDateIsAfterMaxAndMaxIsSet() {
      elementValidationDateList = ElementValidationDateList.builder().max(Period.ofDays(0)).build();
      final var expectedValidationError =
          getExpectedValidationError(
              "Ange ett datum som är senast " + LocalDate.now() + ".", FIELD_ID_DATE_ONE);
      final var categoryId = Optional.of(CATEGORY_ID);
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(
                  ElementValueDateList.builder()
                      .dateListId(FIELD_ID)
                      .dateList(
                          List.of(
                              ElementValueDate.builder()
                                  .dateId(FIELD_ID_DATE_ONE)
                                  .date(LocalDate.now().plusDays(1))
                                  .build()))
                      .build())
              .build();

      final var validationErrors =
          elementValidationDateList.validate(elementData, categoryId, Collections.emptyList());
      assertEquals(expectedValidationError, validationErrors);
    }

    @Test
    void shallReturnMultipleErrorMessageIfDateIsAfterMaxAndMaxIsSet() {
      elementValidationDateList = ElementValidationDateList.builder().max(Period.ofDays(0)).build();
      final var expectedValidationError =
          Stream.concat(
                  getExpectedValidationError(
                      "Ange ett datum som är senast " + LocalDate.now() + ".", FIELD_ID_DATE_ONE)
                      .stream(),
                  getExpectedValidationError(
                      "Ange ett datum som är senast " + LocalDate.now() + ".", FIELD_ID_DATE_TWO)
                      .stream())
              .toList();

      final var categoryId = Optional.of(CATEGORY_ID);
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(
                  ElementValueDateList.builder()
                      .dateListId(FIELD_ID)
                      .dateList(
                          List.of(
                              ElementValueDate.builder()
                                  .dateId(FIELD_ID_DATE_ONE)
                                  .date(LocalDate.now().plusDays(1))
                                  .build(),
                              ElementValueDate.builder()
                                  .dateId(FIELD_ID_DATE_TWO)
                                  .date(LocalDate.now().plusDays(1))
                                  .build()))
                      .build())
              .build();

      final var validationErrors =
          elementValidationDateList.validate(elementData, categoryId, Collections.emptyList());
      assertEquals(expectedValidationError, validationErrors);
    }
  }

  @Nested
  class Mandatory {

    @Test
    void shallNotReturnErrorMessageIfMandatoryIsFalse() {
      elementValidationDateList = ElementValidationDateList.builder().build();
      final var categoryId = Optional.of(CATEGORY_ID);
      final var elementData =
          ElementData.builder()
              .value(
                  ElementValueDateList.builder()
                      .dateListId(FIELD_ID)
                      .dateList(Collections.emptyList())
                      .build())
              .build();

      final var validationErrors =
          elementValidationDateList.validate(elementData, categoryId, Collections.emptyList());
      assertEquals(Collections.emptyList(), validationErrors);
    }

    @Test
    void shallReturnErrorMessageIfMandatoryIsTrueAndQuestionIsMissing() {
      elementValidationDateList = ElementValidationDateList.builder().mandatory(true).build();
      final var expectedValidationError =
          getExpectedValidationError("Välj minst ett alternativ.", FIELD_ID);
      final var categoryId = Optional.of(CATEGORY_ID);
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(
                  ElementValueDateList.builder()
                      .dateListId(FIELD_ID)
                      .dateList(Collections.emptyList())
                      .build())
              .build();

      final var validationErrors =
          elementValidationDateList.validate(elementData, categoryId, Collections.emptyList());
      assertEquals(expectedValidationError, validationErrors);
    }

    @Test
    void shallNotReturnErrorMessageIfMandatoryIsTrueAndQuestionHasAnswer() {
      elementValidationDateList = ElementValidationDateList.builder().mandatory(true).build();

      final var categoryId = Optional.of(CATEGORY_ID);
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(
                  ElementValueDateList.builder()
                      .dateListId(FIELD_ID)
                      .dateList(
                          List.of(
                              ElementValueDate.builder()
                                  .dateId(FIELD_ID_DATE_ONE)
                                  .date(LocalDate.now().plusDays(1))
                                  .build()))
                      .build())
              .build();

      final var validationErrors =
          elementValidationDateList.validate(elementData, categoryId, Collections.emptyList());
      assertEquals(Collections.emptyList(), validationErrors);
    }
  }

  private static List<ValidationError> getExpectedValidationError(String message, FieldId fieldId) {
    return List.of(
        ValidationError.builder()
            .elementId(ELEMENT_ID)
            .fieldId(fieldId)
            .categoryId(CATEGORY_ID)
            .message(new ErrorMessage(message))
            .build());
  }
}
