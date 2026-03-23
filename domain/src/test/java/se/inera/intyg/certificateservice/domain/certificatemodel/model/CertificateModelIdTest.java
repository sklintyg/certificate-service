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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CertificateModelIdTest {

  @Nested
  class MatchesTests {

    @Test
    void shouldReturnTrueIfTypeAndVersionMatches() {
      final var certificateModelId =
          CertificateModelId.builder()
              .type(new CertificateType("type"))
              .version(new CertificateVersion("version"))
              .build();

      assertTrue(certificateModelId.matches("type", List.of("version")));
    }

    @Test
    void shouldReturnFalseIfTypeMatchesButVersionDoesNot() {
      final var certificateModelId =
          CertificateModelId.builder()
              .type(new CertificateType("type"))
              .version(new CertificateVersion("version"))
              .build();

      assertFalse(certificateModelId.matches("type", List.of("anotherVersion")));
    }

    @Test
    void shouldReturnFalseIfTypeDoesNotMatchButVersionDoes() {
      final var certificateModelId =
          CertificateModelId.builder()
              .type(new CertificateType("type"))
              .version(new CertificateVersion("version"))
              .build();

      assertFalse(certificateModelId.matches("anotherType", List.of("version")));
    }
  }
}
