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
package se.inera.intyg.certificateservice.domain.certificate.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ElementValueCodeListTest {

  @Nested
  class IsEmpty {

    @Test
    void shouldReturnTrueIfNull() {
      assertTrue(ElementValueCodeList.builder().build().isEmpty());
    }

    @Test
    void shouldReturnFalseIfCodeListWithValue() {
      assertFalse(
          ElementValueCodeList.builder()
              .list(List.of(ElementValueCode.builder().code("CodeList 1").build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnFalseIfCodeListWithValueAndEmptyValue() {
      assertFalse(
          ElementValueCodeList.builder()
              .list(
                  List.of(
                      ElementValueCode.builder().code("CodeList 1").build(),
                      ElementValueCode.builder().build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnTrueIfCodeListWithEmptyValue() {
      assertTrue(
          ElementValueCodeList.builder()
              .list(List.of(ElementValueCode.builder().build()))
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnTrueIfEmpty() {
      assertTrue(ElementValueCodeList.builder().list(Collections.emptyList()).build().isEmpty());
    }
  }
}
