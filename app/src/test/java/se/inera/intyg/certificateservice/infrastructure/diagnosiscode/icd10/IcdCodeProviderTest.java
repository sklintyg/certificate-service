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
package se.inera.intyg.certificateservice.infrastructure.diagnosiscode.icd10;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IcdCodeProviderTest {

  IcdCodeProvider icdCodeProvider;

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    icdCodeProvider = new IcdCodeProvider();
    final var field = icdCodeProvider.getClass().getDeclaredField("diagnosisCodesFile");
    field.setAccessible(true);
    field.set(icdCodeProvider, "diagnosiscode/icd10se/icd-10-se.tsv");
  }

  @Test
  void shallReturnListOfDiagnosisFromFile() {
    final var diagnosisList = icdCodeProvider.get();
    assertFalse(diagnosisList.isEmpty());
  }

  @Test
  void shallIncludeCodeInDiagnosis() {
    final var diagnosisList = icdCodeProvider.get();
    diagnosisList.forEach(diagnosis -> assertNotNull(diagnosis.code()));
  }

  @Test
  void shallIncludeDescriptionInDiagnosis() {
    final var diagnosisList = icdCodeProvider.get();
    diagnosisList.forEach(diagnosis -> assertNotNull(diagnosis.description()));
  }
}
