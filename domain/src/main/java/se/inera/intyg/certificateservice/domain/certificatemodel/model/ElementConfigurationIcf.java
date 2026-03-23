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

import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueIcf;

@Value
@Builder
public class ElementConfigurationIcf implements ElementConfiguration {

  @Getter(onMethod = @__(@Override))
  String name;

  @Getter(onMethod = @__(@Override))
  String description;

  @Getter(onMethod = @__(@Override))
  ElementType type = ElementType.ICF;

  @Getter(onMethod = @__(@Override))
  ElementMessage message;

  FieldId id;
  String modalLabel;
  String collectionsLabel;
  String placeholder;
  IcfCodesPropertyType icfCodesPropertyName;

  @Override
  public ElementValue emptyValue() {
    return ElementValueIcf.builder().id(id).build();
  }

  @Override
  public Optional<ElementSimplifiedValue> simplified(ElementValue value) {
    if (!(value instanceof ElementValueIcf elementValue)) {
      throw new IllegalStateException("Wrong value type");
    }

    if (elementValue.isEmpty()) {
      return Optional.of(ElementSimplifiedValueText.builder().text("Ej angivet").build());
    }

    if (elementValue.icfCodes().isEmpty()) {
      return Optional.of(ElementSimplifiedValueText.builder().text(elementValue.text()).build());
    }

    return Optional.of(
        ElementSimplifiedValueText.builder()
            .text(
                """
                    %s

                    %s"""
                    .formatted(String.join(" - ", elementValue.icfCodes()), elementValue.text()))
            .build());
  }
}
