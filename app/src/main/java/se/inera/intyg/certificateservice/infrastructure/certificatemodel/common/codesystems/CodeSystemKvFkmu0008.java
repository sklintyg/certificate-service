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

public class CodeSystemKvFkmu0008 {

  public static final String CODE_SYSTEM = "KV_FKMU_0008";

  public static final Code EN_ATTONDEL =
      new Code("EN_ATTONDEL", CODE_SYSTEM, "En åttondel av den ordinarie tiden");

  public static final Code EN_FJARDEDEL =
      new Code("EN_FJARDEDEL", CODE_SYSTEM, "En fjärdedel av den ordinarie tiden");

  public static final Code HALVA = new Code("HALVA", CODE_SYSTEM, "Halva den ordinarie tiden");

  public static final Code TRE_FJARDEDELAR =
      new Code("TRE_FJARDEDELAR", CODE_SYSTEM, "Tre fjärdedelar av den ordinarie tiden");

  public static final Code HELA = new Code("HELA", CODE_SYSTEM, "Hela den ordinarie tiden");

  private CodeSystemKvFkmu0008() {
    throw new IllegalStateException("Utility class");
  }
}
