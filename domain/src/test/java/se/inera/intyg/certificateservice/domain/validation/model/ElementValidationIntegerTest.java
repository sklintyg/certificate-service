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
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueInteger;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@ExtendWith(MockitoExtension.class)
class ElementValidationIntegerTest {

  private static final ElementId ELEMENT_ID = new ElementId("elementId");
  private static final FieldId FIELD_ID = new FieldId("code");
  private static final ElementId CATEGORY_ID = new ElementId("categoryId");
  private ElementValidationInteger elementValidationInteger;

  @Nested
  class IllegalStates {

    @BeforeEach
    void setUp() {
      elementValidationInteger = ElementValidationInteger.builder().build();
    }

    @Test
    void shallThrowIllegalArgumentExceptionIfDataIsNull() {
      final Optional<ElementId> categoryId = Optional.empty();
      assertThrows(
          IllegalArgumentException.class,
          () -> elementValidationInteger.validate(null, categoryId, Collections.emptyList()));
    }

    @Test
    void shallThrowIllegalArgumentExceptionIfValueIsNull() {
      final Optional<ElementId> categoryId = Optional.empty();
      final var elementData = ElementData.builder().id(ELEMENT_ID).build();

      assertThrows(
          IllegalArgumentException.class,
          () ->
              elementValidationInteger.validate(elementData, categoryId, Collections.emptyList()));
    }

    @Test
    void shallThrowIllegalArgumentExceptionIfValueIsWrongType() {
      final Optional<ElementId> categoryId = Optional.empty();
      final var elementData =
          ElementData.builder().id(ELEMENT_ID).value(ElementValueText.builder().build()).build();

      assertThrows(
          IllegalArgumentException.class,
          () ->
              elementValidationInteger.validate(elementData, categoryId, Collections.emptyList()));
    }
  }

  @Nested
  class Mandatory {

    @BeforeEach
    void setUp() {
      elementValidationInteger =
          ElementValidationInteger.builder().mandatory(true).min(1).max(10).build();
    }

    @Test
    void shallReturnValidationErrorIfIntegerIsNull() {
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
              .value(ElementValueInteger.builder().integerId(FIELD_ID).build())
              .build();

      final var actualResult =
          elementValidationInteger.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(expectedValidationError, actualResult);
    }

    @Test
    void shallReturnValidationErrorIfIntegerIsBelowMin() {
      final var expectedValidationError =
          List.of(
              ValidationError.builder()
                  .elementId(ELEMENT_ID)
                  .fieldId(FIELD_ID)
                  .categoryId(CATEGORY_ID)
                  .message(new ErrorMessage("Ange ett värde mellan 1-10."))
                  .build());
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueInteger.builder().integerId(FIELD_ID).value(0).build())
              .build();

      final var actualResult =
          elementValidationInteger.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(expectedValidationError, actualResult);
    }

    @Test
    void shallReturnValidationErrorIfIntegerIsAboveMax() {
      final var expectedValidationError =
          List.of(
              ValidationError.builder()
                  .elementId(ELEMENT_ID)
                  .fieldId(FIELD_ID)
                  .categoryId(CATEGORY_ID)
                  .message(new ErrorMessage("Ange ett värde mellan 1-10."))
                  .build());
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueInteger.builder().integerId(FIELD_ID).value(11).build())
              .build();

      final var actualResult =
          elementValidationInteger.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(expectedValidationError, actualResult);
    }

    @Test
    void shallNotReturnValidationErrorIfIntegerIsWithinLimits() {
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueInteger.builder().integerId(FIELD_ID).value(5).build())
              .build();

      final var actualResult =
          elementValidationInteger.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(Collections.emptyList(), actualResult);
    }
  }

  @Nested
  class NotMandatory {

    @BeforeEach
    void setUp() {
      elementValidationInteger =
          ElementValidationInteger.builder().mandatory(false).min(1).max(10).build();
    }

    @Test
    void shallNotReturnValidationErrorIfIntegerIsNull() {
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueInteger.builder().integerId(FIELD_ID).build())
              .build();

      final var actualResult =
          elementValidationInteger.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(Collections.emptyList(), actualResult);
    }

    @Test
    void shallReturnValidationErrorIfIntegerIsBelowMin() {
      final var expectedValidationError =
          List.of(
              ValidationError.builder()
                  .elementId(ELEMENT_ID)
                  .fieldId(FIELD_ID)
                  .categoryId(CATEGORY_ID)
                  .message(new ErrorMessage("Ange ett värde mellan 1-10."))
                  .build());
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueInteger.builder().integerId(FIELD_ID).value(0).build())
              .build();

      final var actualResult =
          elementValidationInteger.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(expectedValidationError, actualResult);
    }

    @Test
    void shallReturnValidationErrorIfIntegerIsAboveMax() {
      final var expectedValidationError =
          List.of(
              ValidationError.builder()
                  .elementId(ELEMENT_ID)
                  .fieldId(FIELD_ID)
                  .categoryId(CATEGORY_ID)
                  .message(new ErrorMessage("Ange ett värde mellan 1-10."))
                  .build());
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueInteger.builder().integerId(FIELD_ID).value(11).build())
              .build();

      final var actualResult =
          elementValidationInteger.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(expectedValidationError, actualResult);
    }

    @Test
    void shallNotReturnValidationErrorIfIntegerIsWithinLimits() {
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueInteger.builder().integerId(FIELD_ID).value(5).build())
              .build();

      final var actualResult =
          elementValidationInteger.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(Collections.emptyList(), actualResult);
    }
  }

  @Nested
  class NoLimits {

    @BeforeEach
    void setUp() {
      elementValidationInteger = ElementValidationInteger.builder().build();
    }

    @Test
    void shallNotReturnValidationErrorIfIntegerExists() {
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueInteger.builder().integerId(FIELD_ID).value(5).build())
              .build();

      final var actualResult =
          elementValidationInteger.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(Collections.emptyList(), actualResult);
    }

    @Test
    void shallNotReturnValidationErrorIfIntegerIsNull() {
      final var elementData =
          ElementData.builder()
              .id(ELEMENT_ID)
              .value(ElementValueInteger.builder().integerId(FIELD_ID).build())
              .build();

      final var actualResult =
          elementValidationInteger.validate(
              elementData, Optional.of(CATEGORY_ID), Collections.emptyList());

      assertEquals(Collections.emptyList(), actualResult);
    }
  }
}
