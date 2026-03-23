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

public class CodeSystemKvFkmu0010 {

  public static final String CODE_SYSTEM = "KV_FKMU_0010";

  public static final Code INGEN_TIDIGARE =
      new Code("INGEN_TIDIGARE", CODE_SYSTEM, "Ingen tidigare kännedom");

  public static final Code MINDRE_AN_ETT_AR =
      new Code("MINDRE_AN_ETT_AR", CODE_SYSTEM, "Kännedom i mindre än ett år");

  public static final Code MER_AN_ETT_AR =
      new Code("MER_AN_ETT_AR", CODE_SYSTEM, "Kännedom i mer än ett år");

  private CodeSystemKvFkmu0010() {
    throw new IllegalStateException("Utility class");
  }
}
