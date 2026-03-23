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

public class CodeSystemKvFkmu0001 {

  public static final String CODE_SYSTEM = "KV_FKMU_0001";

  public static final Code UNDERSOKNING =
      new Code("UNDERSOKNING", CODE_SYSTEM, "min undersökning av patienten");

  public static final Code JOURNALUPPGIFTER =
      new Code("JOURNALUPPGIFTER", CODE_SYSTEM, "journaluppgifter från den");

  public static final Code ANNAT = new Code("ANNAT", CODE_SYSTEM, "annat");

  public static final Code ANHORIG_V1 =
      new Code("ANHORIG", CODE_SYSTEM, "anhörig eller annans beskrivning av patienten");

  public static final Code ANHORIG_V2 =
      new Code("ANHORIG", CODE_SYSTEM, "anhörigs eller annans beskrivning av patienten");

  public static final Code FYSISKUNDERSOKNING =
      new Code("FYSISKUNDERSOKNING", CODE_SYSTEM, "min undersökning vid fysiskt vårdmöte");

  public static final Code DIGITALUNDERSOKNING =
      new Code("DIGITALUNDERSOKNING", CODE_SYSTEM, "min undersökning vid digitalt vårdmöte");

  public static final Code TELEFONKONTAKT =
      new Code("TELEFONKONTAKT", CODE_SYSTEM, "min telefonkontakt med patienten");

  public static final Code FORALDER =
      new Code("FORALDER", CODE_SYSTEM, "förälders beskrivning av barnet");

  private CodeSystemKvFkmu0001() {
    throw new IllegalStateException("Utility class");
  }
}
