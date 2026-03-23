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

public class CodeSystemKvAnatomiskLokalisationHorapparat {

  public static final String CODE_SYSTEM = "1.2.752.129.5.1.68";

  public static final Code HOGER = new Code("25577004", CODE_SYSTEM, "Höger");

  public static final Code VANSTER = new Code("89644007", CODE_SYSTEM, "Vänster");

  public static final Code BADA_ORONEN = new Code("34338003", CODE_SYSTEM, "Båda öronen");

  private CodeSystemKvAnatomiskLokalisationHorapparat() {
    throw new IllegalStateException("Utility class");
  }
}
