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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCodeList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueMapperCodeListTest {

  private ElementValueMapperCodeList elementValueMapper;

  @BeforeEach
  void setUp() {
    elementValueMapper = new ElementValueMapperCodeList();
  }

  @Test
  void shallReturnTrueIfClassMappedIsMappedElementValueCodeList() {
    assertTrue(elementValueMapper.supports(MappedElementValueCodeList.class));
  }

  @Test
  void shallReturnTrueIfClassElementValueIsElementValueCodeList() {
    assertTrue(elementValueMapper.supports(ElementValueCodeList.class));
  }

  @Test
  void shallReturnFalseForUnsupportedClass() {
    assertFalse(elementValueMapper.supports(String.class));
  }

  @Test
  void shallMapToDomain() {
    final var expectedValue =
        ElementValueCodeList.builder()
            .id(new FieldId("ID"))
            .list(
                List.of(
                    ElementValueCode.builder().codeId(new FieldId("CODE_ID")).code("code").build()))
            .build();

    final var mappedElementValue =
        MappedElementValueCodeList.builder()
            .id("ID")
            .list(List.of(MappedCode.builder().id("CODE_ID").code("code").build()))
            .build();

    final var actualValue = elementValueMapper.toDomain(mappedElementValue);

    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallMapToMapped() {
    final var expectedValue =
        MappedElementValueCodeList.builder()
            .id("ID")
            .list(List.of(MappedCode.builder().id("CODE_ID").code("code").build()))
            .build();

    final var elementValue =
        ElementValueCodeList.builder()
            .id(new FieldId("ID"))
            .list(
                List.of(
                    ElementValueCode.builder().codeId(new FieldId("CODE_ID")).code("code").build()))
            .build();

    final var actualValue = elementValueMapper.toMapped(elementValue);

    assertEquals(expectedValue, actualValue);
  }
}
