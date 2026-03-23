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
package se.inera.intyg.certificateservice.domain.certificatemodel.model;

import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueTable;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRange;

@Value
@Builder
public class ElementConfigurationDateRange implements ElementConfiguration {

  @Getter(onMethod = @__(@Override))
  String name;

  @Getter(onMethod = @__(@Override))
  ElementType type = ElementType.DATE_RANGE;

  @Getter(onMethod = @__(@Override))
  ElementMessage message;

  FieldId id;
  String labelFrom;
  String labelTo;

  @Override
  public ElementValue emptyValue() {
    return ElementValueDateRange.builder().id(id).build();
  }

  @Override
  public Optional<ElementSimplifiedValue> simplified(ElementValue value) {
    if (!(value instanceof ElementValueDateRange elementValue)) {
      throw new IllegalStateException("Wrong value type");
    }
    if (elementValue.isEmpty()) {
      return Optional.of(ElementSimplifiedValueText.builder().text("Ej angivet").build());
    }

    return Optional.of(
        ElementSimplifiedValueTable.builder()
            .headings(List.of(name, labelFrom, labelTo))
            .values(
                List.of(
                    List.of(
                        elementValue.fromDate() != null ? elementValue.fromDate().toString() : "",
                        elementValue.toDate() != null ? elementValue.toDate().toString() : "")))
            .build());
  }
}
