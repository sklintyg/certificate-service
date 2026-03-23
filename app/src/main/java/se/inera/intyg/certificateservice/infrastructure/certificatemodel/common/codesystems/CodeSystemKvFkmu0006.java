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

public class CodeSystemKvFkmu0006 {

  public static final String CODE_SYSTEM = "KV_FKMU_0006";

  public static final Code STOR_SANNOLIKHET =
      new Code(
          "STOR_SANNOLIKHET",
          CODE_SYSTEM,
          "Patienten förväntas kunna återgå helt i nuvarande sysselsättning efter detta intygs slutdatum");

  public static final Code ATER_X_ANTAL_MANADER =
      new Code(
          "ATER_X_ANTAL_MANADER",
          CODE_SYSTEM,
          "Patienten förväntas kunna återgå helt i nuvarande sysselsättning efter angiven tid");

  public static final Code SANNOLIKT_INTE =
      new Code(
          "SANNOLIKT_INTE",
          CODE_SYSTEM,
          "Patienten förväntas inte kunna återgå helt i nuvarande sysselsättning");

  public static final Code PROGNOS_OKLAR =
      new Code(
          "PROGNOS_OKLAR",
          CODE_SYSTEM,
          "Prognosen för återgång i nuvarande sysselsättning är svårbedömd");

  private CodeSystemKvFkmu0006() {
    throw new IllegalStateException("Utility class");
  }
}
