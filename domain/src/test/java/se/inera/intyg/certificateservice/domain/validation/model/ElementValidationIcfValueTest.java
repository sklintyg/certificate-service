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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueIcf;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@ExtendWith(MockitoExtension.class)
class ElementValidationIcfValueTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final FieldId FIELD_ID = new FieldId("code");
  private static final ElementId CATEGORY_ID = new ElementId("categoryId");
  private ElementValidationIcfValue elementValidationIcfValue;

  @Nested
  class IllegalStates {

    @BeforeEach
    void setUp() {
      elementValidationIcfValue = ElementValidationIcfValue.builder().build();
    }

    @Test
    void shallThrowIllegalArgumentExceptionIfDataIsNull() {
      final Optional<ElementId> categoryId = Optional.empty();
      assertThrows(
          IllegalArgumentException.class,
          () -> elementValidationIcfValue.validate(null, categoryId, Collections.emptyList()));
    }

    @Test
    void shallThrowIllegalArgumentExceptionIfValueIsNull() {
      final Optional<ElementId> categoryId = Optional.empty();
      final var elementData = ElementData.builder().id(ELEMENT_ID).build();

      assertThrows(
          IllegalArgumentException.class,
          () ->
              elementValidationIcfValue.validate(elementData, categoryId, Collections.emptyList()));
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
              elementValidationIcfValue.validate(elementData, categoryId, Collections.emptyList()));
    }
  }

  @Nested
  class Mandatory {

    @BeforeEach
    void setUp() {
      elementValidationIcfValue =
          ElementValidationIcfValue.builder().mandatory(true).limit(4).build();
    }

    @Test
    void shallReturnValidationErrorIfIcfIsNull() {
      final var expectedValidationError =
          List.of(
              ValidationError.builder()
                  .elementId(ELEMENT_ID)
                  .fieldId(FIELD_ID)
                  .categoryId(CATEGORY_ID)
                  .message(new ErrorMessage("Ange ett svar."))
                  .build());
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueIcf.builder().id(FIELD_ID).build())
              .build();

      final var actualResult =
          elementValidationIcfValue.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(expectedValidationError, actualResult);
    }

    @Test
    void shallReturnValidationErrorIfTextIsOverLimit() {
      final var expectedValidationError =
          List.of(
              ValidationError.builder()
                  .elementId(ELEMENT_ID)
                  .fieldId(FIELD_ID)
                  .categoryId(CATEGORY_ID)
                  .message(new ErrorMessage("Ange en text som inte är längre än 4."))
                  .build());
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueIcf.builder().id(FIELD_ID).text("1234567891011213").build())
              .build();

      final var actualResult =
          elementValidationIcfValue.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(expectedValidationError, actualResult);
    }

    @Test
    void shallNotReturnValidationErrorIfTextIsUnderLimit() {
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueIcf.builder().id(FIELD_ID).text("123").build())
              .build();

      final var actualResult =
          elementValidationIcfValue.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(Collections.emptyList(), actualResult);
    }
  }

  @Nested
  class NotMandatory {

    @BeforeEach
    void setUp() {
      elementValidationIcfValue =
          ElementValidationIcfValue.builder().mandatory(false).limit(4).build();
    }

    @Test
    void shallNotReturnValidationErrorIfTextIsNull() {
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueIcf.builder().id(FIELD_ID).build())
              .build();

      final var actualResult =
          elementValidationIcfValue.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(Collections.emptyList(), actualResult);
    }

    @Test
    void shallNotReturnValidationErrorIfTextIsUnderLimit() {
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueIcf.builder().id(FIELD_ID).text("123").build())
              .build();

      final var actualResult =
          elementValidationIcfValue.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(Collections.emptyList(), actualResult);
    }

    @Test
    void shallReturnValidationErrorIfTextIsOverLimit() {
      final var expectedValidationError =
          List.of(
              ValidationError.builder()
                  .elementId(ELEMENT_ID)
                  .fieldId(FIELD_ID)
                  .categoryId(CATEGORY_ID)
                  .message(new ErrorMessage("Ange en text som inte är längre än 4."))
                  .build());
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueIcf.builder().id(FIELD_ID).text("1234567891011213").build())
              .build();

      final var actualResult =
          elementValidationIcfValue.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(expectedValidationError, actualResult);
    }
  }

  @Nested
  class NoLimits {

    @BeforeEach
    void setUp() {
      elementValidationIcfValue = ElementValidationIcfValue.builder().build();
    }

    @Test
    void shallNotReturnValidationErrorIfTextExists() {
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueIcf.builder().id(FIELD_ID).text("123").build())
              .build();

      final var actualResult =
          elementValidationIcfValue.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(Collections.emptyList(), actualResult);
    }

    @Test
    void shallNotReturnValidationErrorIfTextIsEmpty() {
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueIcf.builder().id(FIELD_ID).build())
              .build();

      final var actualResult =
          elementValidationIcfValue.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(Collections.emptyList(), actualResult);
    }
  }
}
