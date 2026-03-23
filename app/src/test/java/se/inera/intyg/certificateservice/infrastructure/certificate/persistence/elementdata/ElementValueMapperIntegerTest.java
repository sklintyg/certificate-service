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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueInteger;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueMapperIntegerTest {

  private static final String ID = "id";
  private ElementValueMapperInteger elementValueMapperInteger;

  @BeforeEach
  void setUp() {
    elementValueMapperInteger = new ElementValueMapperInteger();
  }

  @Test
  void shallReturnTrueIfClassMappedElementValueInteger() {
    assertTrue(elementValueMapperInteger.supports(MappedElementValueInteger.class));
  }

  @Test
  void shallReturnTrueIfClassElementValueInteger() {
    assertTrue(elementValueMapperInteger.supports(ElementValueInteger.class));
  }

  @Test
  void shallReturnFalseForUnsupportedClass() {
    assertFalse(elementValueMapperInteger.supports(String.class));
  }

  @Test
  void shallMapToDomain() {
    final var expectedValue =
        ElementValueInteger.builder().integerId(new FieldId(ID)).value(42).build();

    final var mappedValue = MappedElementValueInteger.builder().integerId(ID).value(42).build();

    final var actualValue = elementValueMapperInteger.toDomain(mappedValue);

    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallMapToMapped() {
    final var expectedValue = MappedElementValueInteger.builder().integerId(ID).value(42).build();

    final var elementValue =
        ElementValueInteger.builder().integerId(new FieldId(ID)).value(42).build();

    final var actualValue = elementValueMapperInteger.toMapped(elementValue);

    assertEquals(expectedValue, actualValue);
  }
}
