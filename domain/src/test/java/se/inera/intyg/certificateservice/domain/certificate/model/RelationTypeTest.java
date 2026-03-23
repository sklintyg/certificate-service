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
import static se.inera.intyg.certificateservice.domain.certificate.model.RelationType.COMPLEMENT;
import static se.inera.intyg.certificateservice.domain.certificate.model.RelationType.RENEW;
import static se.inera.intyg.certificateservice.domain.certificate.model.RelationType.REPLACE;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RelationTypeTest {

  @Nested
  class ToRelationKodTests {

    @Test
    void shallConvertReplace() {
      assertEquals("ERSATT", REPLACE.toRelationKod());
    }

    @Test
    void shallConvertRenew() {
      assertEquals("FRLANG", RENEW.toRelationKod());
    }

    @Test
    void shallConvertComplement() {
      assertEquals("KOMPLT", COMPLEMENT.toRelationKod());
    }
  }

  @Nested
  class ToRelationKodTextTests {

    @Test
    void shallConvertReplace() {
      assertEquals("Ersätter", REPLACE.toRelationKodText());
    }

    @Test
    void shallConvertRenew() {
      assertEquals("Förlänger", RENEW.toRelationKodText());
    }

    @Test
    void shallConvertComplement() {
      assertEquals("Kompletterar", COMPLEMENT.toRelationKodText());
    }
  }
}
