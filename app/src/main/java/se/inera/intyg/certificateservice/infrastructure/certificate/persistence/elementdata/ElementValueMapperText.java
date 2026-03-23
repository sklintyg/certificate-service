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

import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@Component
public class ElementValueMapperText implements ElementValueMapper {

  @Override
  public boolean supports(Class<?> c) {
    return c.equals(MappedElementValueText.class) || c.equals(ElementValueText.class);
  }

  @Override
  public ElementValue toDomain(MappedElementValue mappedValue) {
    if (mappedValue instanceof MappedElementValueText valueText) {
      return ElementValueText.builder()
          .textId(new FieldId(valueText.getTextId()))
          .text(valueText.getText())
          .build();
    }
    throw new IllegalStateException("MappedElementValue not supported '%s'".formatted(mappedValue));
  }

  @Override
  public MappedElementValue toMapped(ElementValue value) {
    if (value instanceof ElementValueText elementValueText) {
      return MappedElementValueText.builder()
          .textId(elementValueText.textId().value())
          .text(elementValueText.text())
          .build();
    }
    throw new IllegalStateException("ElementValue not supported '%s'".formatted(value));
  }
}
