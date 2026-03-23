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

import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

@Value
@Getter(AccessLevel.NONE)
@Builder
public class ElementValidationDate implements ElementValidation {

  boolean mandatory;
  TemporalAmount min;
  TemporalAmount max;

  @Override
  public List<ValidationError> validate(
      ElementData data, Optional<ElementId> categoryId, List<ElementData> dataList) {
    if (data == null) {
      throw new IllegalArgumentException("Element data is null");
    }

    final var dateValue = getValue(data.value());

    if (mandatory && dateValue.isEmpty()) {
      return errorMessage(data, dateValue, categoryId, ErrorMessageFactory.missingDate());
    }

    if (ElementValidator.isDateBeforeMin(dateValue.date(), min)) {
      return errorMessage(data, dateValue, categoryId, ErrorMessageFactory.minDate(min));
    }

    if (ElementValidator.isDateAfterMax(dateValue.date(), max)) {
      return errorMessage(data, dateValue, categoryId, ErrorMessageFactory.maxDate(max));
    }

    return Collections.emptyList();
  }

  private ElementValueDate getValue(ElementValue value) {
    if (value == null) {
      throw new IllegalArgumentException("Element data value is null");
    }

    if (value instanceof ElementValueDate dateValue) {
      return dateValue;
    }

    throw new IllegalArgumentException(
        "Element data value %s is of wrong type".formatted(value.getClass()));
  }

  private static List<ValidationError> errorMessage(
      ElementData data,
      ElementValueDate dateValue,
      Optional<ElementId> categoryId,
      ErrorMessage message) {
    return List.of(
        ValidationError.builder()
            .elementId(data.id())
            .fieldId(dateValue.dateId())
            .categoryId(categoryId.orElse(null))
            .message(message)
            .build());
  }
}
