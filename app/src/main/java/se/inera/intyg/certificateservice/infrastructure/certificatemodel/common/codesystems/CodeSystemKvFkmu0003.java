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

public class CodeSystemKvFkmu0003 {

  public static final String CODE_SYSTEM = "KV_FKMU_0003";

  public static final Code HELT_NEDSATT = new Code("HELT_NEDSATT", CODE_SYSTEM, "100 procent");

  public static final Code TRE_FJARDEDEL = new Code("TRE_FJARDEDEL", CODE_SYSTEM, "75 procent");

  public static final Code HALFTEN = new Code("HALFTEN", CODE_SYSTEM, "50 procent");

  public static final Code EN_FJARDEDEL = new Code("EN_FJARDEDEL", CODE_SYSTEM, "25 procent");

  private CodeSystemKvFkmu0003() {
    throw new IllegalStateException("Utility class");
  }
}
