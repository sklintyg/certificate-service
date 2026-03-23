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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.elementdata;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

@Component
@RequiredArgsConstructor
public class ElementDataMapper {

  private final List<ElementValueMapper> elementValueMapper;

  public ElementData toDomain(MappedElementData mappedElement) {
    return ElementData.builder()
        .id(new ElementId(mappedElement.getId()))
        .value(
            elementValueMapper.stream()
                .filter(element -> element.supports(mappedElement.getValue().getClass()))
                .findFirst()
                .map(valueMapper -> valueMapper.toDomain(mappedElement.getValue()))
                .orElseThrow(
                    () ->
                        new IllegalStateException(
                            "Could not find converter for class '%s'"
                                .formatted(mappedElement.getValue().getClass()))))
        .build();
  }

  public MappedElementData toMapped(ElementData element) {
    return MappedElementData.builder()
        .id(element.id().id())
        .value(
            elementValueMapper.stream()
                .filter(elementData -> elementData.supports(element.value().getClass()))
                .findFirst()
                .map(valueMapper -> valueMapper.toMapped(element.value()))
                .orElseThrow(
                    () ->
                        new IllegalStateException(
                            "Could not find converter for class '%s'"
                                .formatted(element.value().getClass()))))
        .build();
  }
}
