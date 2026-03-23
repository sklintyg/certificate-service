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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PlaceholderCertificateTest {

  @Nested
  class RevokedTests {

    @Test
    void shouldThrowIfStatusIsNotSigned() {
      final var placeholderCertificate =
          PlaceholderCertificate.builder().status(Status.REVOKED).build();

      final var illegalStateException =
          assertThrows(
              IllegalStateException.class, () -> placeholderCertificate.revoke(null, null));

      assertEquals(
          "Incorrect status '%s' - required status is '%s' or '%s' to revoke"
              .formatted(Status.REVOKED, Status.SIGNED, Status.LOCKED_DRAFT),
          illegalStateException.getMessage());
    }

    @Test
    void shouldSetStatusToRevoked() {
      final var placeholderCertificate =
          PlaceholderCertificate.builder().status(Status.SIGNED).build();

      placeholderCertificate.revoke(null, null);
      assertEquals(Status.REVOKED, placeholderCertificate.status());
    }
  }
}
