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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.common.model.Code;

class ElementConfigurationDropdownCodeTest {

  private static final FieldId FIELD_ID = new FieldId("field1");
  private static final Code CODE1 = new Code("A", "CODESYSTEM", "Alpha");
  private static final Code CODE2 = new Code("B", "CODESYSTEM", "Beta");
  private static final ElementConfigurationCode CONFIG_CODE1 =
      new ElementConfigurationCode(new FieldId(CODE1.code()), CODE1.displayName(), CODE1);
  private static final ElementConfigurationCode CONFIG_CODE2 =
      new ElementConfigurationCode(new FieldId(CODE2.code()), CODE2.displayName(), CODE2);

  private final ElementConfigurationDropdownCode dropdown =
      ElementConfigurationDropdownCode.builder()
          .id(FIELD_ID)
          .name("Dropdown")
          .message(ElementMessage.builder().build())
          .list(List.of(CONFIG_CODE1, CONFIG_CODE2))
          .build();

  @Test
  void emptyValueShouldReturnElementValueCodeWithId() {
    ElementValue value = dropdown.emptyValue();
    assertInstanceOf(ElementValueCode.class, value);
    assertEquals(FIELD_ID, ((ElementValueCode) value).codeId());
    assertTrue(value.isEmpty());
  }

  @Test
  void simplifiedShouldReturnEmptyIfValueIsEmpty() {
    ElementValueCode emptyValue = ElementValueCode.builder().codeId(FIELD_ID).build();
    Optional<ElementSimplifiedValue> result = dropdown.simplified(emptyValue);
    assertTrue(result.isEmpty());
  }

  @Test
  void simplifiedShouldReturnDisplayNameIfValueIsPresent() {
    ElementValueCode value =
        ElementValueCode.builder().codeId(new FieldId(CODE1.code())).code(CODE1.code()).build();
    Optional<ElementSimplifiedValue> result = dropdown.simplified(value);
    assertTrue(result.isPresent());
    assertInstanceOf(ElementSimplifiedValueText.class, result.get());
    assertEquals("Alpha", ((ElementSimplifiedValueText) result.get()).text());
  }

  @Test
  void simplifiedShouldThrowIfWrongType() {
    var wrongValue = ElementValueText.builder().text("This is not a code value").build();
    assertThrows(IllegalStateException.class, () -> dropdown.simplified(wrongValue));
  }

  @Test
  void codeShouldReturnMatchingCode() {
    ElementValueCode value = ElementValueCode.builder().codeId(new FieldId(CODE1.code())).build();
    Code code = dropdown.code(value);
    assertEquals(CODE1, code);
  }

  @Test
  void codeShouldThrowIfNoMatch() {
    ElementValueCode value = ElementValueCode.builder().codeId(new FieldId("UnknownCode")).build();
    assertThrows(IllegalArgumentException.class, () -> dropdown.code(value));
  }
}
