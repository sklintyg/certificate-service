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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueInteger;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@Value
@Getter(AccessLevel.NONE)
@Builder
public class ElementValidationInteger implements ElementValidation {

  boolean mandatory;
  Integer min;
  Integer max;

  @Override
  public List<ValidationError> validate(
      ElementData data, Optional<ElementId> categoryId, List<ElementData> dataList) {
    if (data == null) {
      throw new IllegalArgumentException("Element data is null");
    }

    final var value = getValue(data.value());

    if (mandatory && value.isEmpty()) {
      return List.of(
          errorMessage(data, value.integerId(), categoryId, ErrorMessageFactory.missingAnswer()));
    }

    if (value.value() != null && !ElementValidator.isIntegerWithinLimit(value.value(), min, max)) {
      return List.of(
          errorMessage(
              data, value.integerId(), categoryId, ErrorMessageFactory.integerInterval(min, max)));
    }

    return Collections.emptyList();
  }

  private static ValidationError errorMessage(
      ElementData data, FieldId fieldId, Optional<ElementId> categoryId, ErrorMessage message) {
    return ValidationError.builder()
        .elementId(data.id())
        .fieldId(fieldId)
        .categoryId(categoryId.orElse(null))
        .message(message)
        .build();
  }

  private ElementValueInteger getValue(ElementValue value) {
    if (value == null) {
      throw new IllegalArgumentException("Element data value is null");
    }

    if (value instanceof ElementValueInteger integerValue) {
      return integerValue;
    }

    throw new IllegalArgumentException(
        "Element data value %s is of wrong type".formatted(value.getClass()));
  }
}
