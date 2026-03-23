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

public class CodeSystemKvIdKontroll {

  public static final String CODE_SYSTEM = "e7cc8f30-a353-4c42-b17a-a189b6876647";

  public static final Code IDK1 = new Code("IDK1", CODE_SYSTEM, "ID-kort");

  public static final Code IDK2 = new Code("IDK2", CODE_SYSTEM, "Företagskort eller tjänstekort");

  public static final Code IDK3 = new Code("IDK3", CODE_SYSTEM, "Svenskt körkort");

  public static final Code IDK4 = new Code("IDK4", CODE_SYSTEM, "Personlig kännedom");

  public static final Code IDK5 = new Code("IDK5", CODE_SYSTEM, "Försäkran enligt 18 kap. 4 §");

  public static final Code IDK6 = new Code("IDK6", CODE_SYSTEM, "Pass");

  private CodeSystemKvIdKontroll() {
    throw new IllegalStateException("Utility class");
  }
}
