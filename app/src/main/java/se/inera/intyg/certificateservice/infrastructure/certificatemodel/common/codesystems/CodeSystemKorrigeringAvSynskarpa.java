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

import se.inera.intyg.certificateservice.domain.common.model.Code;

public class CodeSystemKorrigeringAvSynskarpa {

  public static final String CODE_SYSTEM = "KORRIGERING_AV_SYNSKARPA";

  private CodeSystemKorrigeringAvSynskarpa() {
    throw new IllegalStateException("Utility class");
  }

  public static final Code GLASOGON_INGEN_STYRKA_OVER_8_DIOPTRIER =
      new Code(
          "6.1",
          CODE_SYSTEM,
          "Glasögon och inget av glasen har en styrka över plus 8 dioptrier i den mest brytande meridianen");

  public static final Code GLASOGON_MED_STYRKA_OVER_8_DIOPTRIER =
      new Code(
          "6.3",
          CODE_SYSTEM,
          "Glasögon och något av glasen har en styrka över plus 8 dioptrier i den mest brytande meridianen");

  public static final Code KONTAKTLINSER = new Code("6.5", CODE_SYSTEM, "Kontaktlinser");
}
