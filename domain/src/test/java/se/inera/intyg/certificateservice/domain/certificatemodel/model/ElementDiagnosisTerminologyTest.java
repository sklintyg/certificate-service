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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ElementDiagnosisTerminologyTest {

  private static final ElementDiagnosisTerminology TERMINOLOGY =
      new ElementDiagnosisTerminology(
          "id", "label", "primaryCodeSystem", List.of("equivalentCodeSystem"));

  @Test
  void shouldHandleNullEquivalentCodeSystems() {
    ElementDiagnosisTerminology terminology =
        new ElementDiagnosisTerminology("id", "label", "primaryCodeSystem", null);

    assertNotNull(terminology.equivalentCodeSystems());
    assertTrue(terminology.equivalentCodeSystems().isEmpty());
  }

  @ParameterizedTest
  @ValueSource(strings = {"primaryCodeSystem", "equivalentCodeSystem"})
  void shouldReturnTrueForValidCodeSystem(String codeSystem) {
    assertTrue(TERMINOLOGY.isValidCodeSystem(codeSystem));
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldReturnFalseForInvalidCodeSystem(String codeSystem) {
    assertFalse(TERMINOLOGY.isValidCodeSystem(codeSystem));
  }

  @Test
  void shouldReturnFalseForUnrelatedCodeSystem() {
    assertFalse(TERMINOLOGY.isValidCodeSystem("unrelatedCodeSystem"));
  }
}
