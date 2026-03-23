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

import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@Value
@Getter(AccessLevel.NONE)
@Builder
public class ElementValidationCategory implements ElementValidation {

  boolean mandatory;
  List<ElementId> elements;

  @Override
  public List<ValidationError> validate(
      ElementData data, Optional<ElementId> categoryId, List<ElementData> dataList) {
    if (data == null) {
      throw new IllegalArgumentException("Element data is null");
    }

    final var elementsThatShouldBeValidated =
        dataList.stream().filter(elementData -> elements.contains(elementData.id())).toList();

    if (mandatory && validateElements(elementsThatShouldBeValidated)) {
      return List.of(
          ValidationError.builder()
              .elementId(new ElementId(data.id().id()))
              .categoryId(data.id())
              .fieldId(new FieldId(data.id().id()))
              .message(new ErrorMessage("Besvara minst en av frågorna."))
              .build());
    }

    return List.of();
  }

  private boolean validateElements(List<ElementData> elementsThatShouldBeValidated) {
    final var invalidElements =
        elementsThatShouldBeValidated.stream()
            .map(ElementData::value)
            .filter(ElementValue::isEmpty)
            .toList();

    return invalidElements.size() == elementsThatShouldBeValidated.size();
  }
}
