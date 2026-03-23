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
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCodeList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@Component
public class ElementValueMapperCodeList implements ElementValueMapper {

  @Override
  public boolean supports(Class<?> c) {
    return c.equals(MappedElementValueCodeList.class) || c.equals(ElementValueCodeList.class);
  }

  @Override
  public ElementValue toDomain(MappedElementValue mappedValue) {
    if (mappedValue instanceof MappedElementValueCodeList mappedElementValueCodeList) {
      return ElementValueCodeList.builder()
          .id(new FieldId(mappedElementValueCodeList.getId()))
          .list(
              mappedElementValueCodeList.getList().stream()
                  .map(
                      code ->
                          ElementValueCode.builder()
                              .codeId(new FieldId(code.getId()))
                              .code(code.getCode())
                              .build())
                  .toList())
          .build();
    }
    throw new IllegalStateException("MappedElementValue not supported '%s'".formatted(mappedValue));
  }

  @Override
  public MappedElementValue toMapped(ElementValue value) {
    if (value instanceof ElementValueCodeList elementValueCodeList) {
      return MappedElementValueCodeList.builder()
          .id(elementValueCodeList.id().value())
          .list(
              elementValueCodeList.list().stream()
                  .map(
                      code ->
                          MappedCode.builder().id(code.codeId().value()).code(code.code()).build())
                  .toList())
          .build();
    }
    throw new IllegalStateException("ElementValue not supported '%s'".formatted(value));
  }
}
