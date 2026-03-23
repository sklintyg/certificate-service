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
package se.inera.intyg.certificateservice.domain.common.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HsaIdTest {

  @Test
  void shouldCreateHsaId() {
    final var hsaId = HsaId.create("hsaid");
    assertEquals("hsaid", hsaId.id());
  }

  @Test
  void shouldThrowExceptionWhenIdIsNull() {
    IllegalArgumentException exception =
        org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class, () -> new HsaId(null));
    assertEquals("Missing id", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenIdIsBlank() {
    IllegalArgumentException exception =
        org.junit.jupiter.api.Assertions.assertThrows(
            IllegalArgumentException.class, () -> new HsaId(" "));
    assertEquals("Missing id", exception.getMessage());
  }

  @Test
  void shouldBeCaseInsensitiveWhenEquals() {
    final var hsaIdOne = new HsaId("hsaid");
    final var hsaIdTwo = new HsaId("HSAID");
    assertEquals(hsaIdOne, hsaIdTwo);
  }

  @Test
  void shouldProduceSameHashCodeIgnoringCase() {
    final var hsaIdOne = new HsaId("hsaid");
    final var hsaIdTwo = new HsaId("HSAID");
    assertEquals(hsaIdOne.hashCode(), hsaIdTwo.hashCode());
  }
}
