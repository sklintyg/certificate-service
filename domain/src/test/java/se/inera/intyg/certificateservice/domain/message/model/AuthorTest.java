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
package se.inera.intyg.certificateservice.domain.message.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AuthorTest {

  @Test
  void shallReturnForsakringskassanIfAuthorIsFK() {
    final var author = new Author("FK");
    assertEquals("Försäkringskassan", author.name());
  }

  @Test
  void shallReturnAuthor() {
    final var author = new Author("WC");
    assertEquals("WC", author.name());
  }

  @Test
  void shallReturnFalseIfAuthorIsWC() {
    final var author = new Author("WC");
    assertFalse(author.fromFK());
  }

  @Test
  void shallReturnTrueIfAuthorIsFK() {
    final var author = new Author("FK");
    assertTrue(author.fromFK());
  }
}
