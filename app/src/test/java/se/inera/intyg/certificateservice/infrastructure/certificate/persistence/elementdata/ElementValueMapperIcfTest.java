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
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueIcf;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueMapperIcfTest {

  private static final String ID = "id";
  private static final String TEXT = "text";
  private static final List<String> VALUES = List.of("code1", "code2");
  private ElementValueMapperIcf elementValueMapperIcf;

  @BeforeEach
  void setUp() {
    elementValueMapperIcf = new ElementValueMapperIcf();
  }

  @Test
  void shallReturnTrueIfClassMappedElementValueIcf() {
    assertTrue(elementValueMapperIcf.supports(MappedElementValueIcf.class));
  }

  @Test
  void shallReturnTrueIfClassElementValueIcf() {
    assertTrue(elementValueMapperIcf.supports(ElementValueIcf.class));
  }

  @Test
  void shallReturnFalseForUnsupportedClass() {
    assertFalse(elementValueMapperIcf.supports(String.class));
  }

  @Test
  void shallMapToDomain() {
    final var expectedValue =
        ElementValueIcf.builder().id(new FieldId(ID)).text(TEXT).icfCodes(VALUES).build();

    final var mappedElementValueIcf =
        MappedElementValueIcf.builder().id(ID).text(TEXT).icfCodes(VALUES).build();

    final var actualValue = elementValueMapperIcf.toDomain(mappedElementValueIcf);

    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallMapToMapped() {
    final var expectedValue =
        MappedElementValueIcf.builder().id(ID).text(TEXT).icfCodes(VALUES).build();

    final var elementValueIcf =
        ElementValueIcf.builder().id(new FieldId(ID)).text(TEXT).icfCodes(VALUES).build();

    final var actualValue = elementValueMapperIcf.toMapped(elementValueIcf);

    assertEquals(expectedValue, actualValue);
  }
}
