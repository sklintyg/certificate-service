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

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementDiagnosisTerminology;

public class CodeSystemIcd10Se {

  private CodeSystemIcd10Se() {
    throw new IllegalStateException("Utility class");
  }

  private static final String DEPRECATED_ICD_10_SE_CODE_SYSTEM = "1.2.752.116.1.1.1.1.8";
  private static final String DEPRECATED_LEGACY_ICD_10_SE_CODE_SYSTEM = "1.2.752.116.1.1.1.1.3";
  public static final String ICD_10_SE_CODE_SYSTEM = "1.2.752.116.1.1.1";
  public static final String DIAGNOS_ICD_10_ID = "ICD_10_SE";
  private static final String DIAGNOS_ICD_10_LABEL = "ICD-10-SE";

  /**
   * @deprecated Use {@link #terminology()} for all new certificate types. This method returns a
   *     deprecated ICD-10-SE code system OID that was faulty.
   */
  @Deprecated
  public static ElementDiagnosisTerminology deprecatedTerminology() {
    return new ElementDiagnosisTerminology(
        DIAGNOS_ICD_10_ID,
        DIAGNOS_ICD_10_LABEL,
        DEPRECATED_ICD_10_SE_CODE_SYSTEM,
        List.of(ICD_10_SE_CODE_SYSTEM, DEPRECATED_LEGACY_ICD_10_SE_CODE_SYSTEM));
  }

  public static ElementDiagnosisTerminology terminology() {
    return new ElementDiagnosisTerminology(
        DIAGNOS_ICD_10_ID,
        DIAGNOS_ICD_10_LABEL,
        ICD_10_SE_CODE_SYSTEM,
        List.of(DEPRECATED_ICD_10_SE_CODE_SYSTEM, DEPRECATED_LEGACY_ICD_10_SE_CODE_SYSTEM));
  }
}
