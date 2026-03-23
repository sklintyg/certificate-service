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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementDiagnosisTerminology;

class CodeSystemIcd10SeTest {

  @Test
  void testDeprecatedTerminology() {
    ElementDiagnosisTerminology terminology = CodeSystemIcd10Se.deprecatedTerminology();

    assertAll(
        () -> assertEquals("ICD_10_SE", terminology.id()),
        () -> assertEquals("ICD-10-SE", terminology.label()),
        () -> assertEquals("1.2.752.116.1.1.1.1.8", terminology.codeSystem()),
        () ->
            assertEquals(
                List.of("1.2.752.116.1.1.1", "1.2.752.116.1.1.1.1.3"),
                terminology.equivalentCodeSystems()));
  }

  @Test
  void testTerminology() {
    ElementDiagnosisTerminology terminology = CodeSystemIcd10Se.terminology();
    assertAll(
        () -> assertEquals("ICD_10_SE", terminology.id()),
        () -> assertEquals("ICD-10-SE", terminology.label()),
        () -> assertEquals("1.2.752.116.1.1.1", terminology.codeSystem()),
        () ->
            assertEquals(
                List.of("1.2.752.116.1.1.1.1.8", "1.2.752.116.1.1.1.1.3"),
                terminology.equivalentCodeSystems()));
  }
}
