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

public class CodeSystemFunktionsnedsattning {

  public static final String CODE_SYSTEM = "FUNKTIONSNEDSATTNING";

  private CodeSystemFunktionsnedsattning() {
    throw new IllegalStateException("Utility class");
  }

  public static final Code INTELLEKTUELL_FUNKTION =
      new Code("INTELLEKTUELL_FUNKTION", CODE_SYSTEM, "Intellektuell funktion");

  public static final Code KOMMUNIKATION_SOCIAL_INTERAKTION =
      new Code(
          "KOMMUNIKATION_SOCIAL_INTERAKTION", CODE_SYSTEM, "Övergripande psykosociala funktioner");

  public static final Code UPPMARKSAMHET =
      new Code("UPPMARKSAMHET", CODE_SYSTEM, "Uppmärksamhet, koncentration och exekutiv funktion");

  public static final Code PSYKISK_FUNKTION =
      new Code("PSYKISK_FUNKTION", CODE_SYSTEM, "Annan psykisk funktion");

  public static final Code HORSELFUNKTION =
      new Code("HORSELFUNKTION", CODE_SYSTEM, "Hörselfunktion");

  public static final Code SYNFUNKTION = new Code("SYNFUNKTION", CODE_SYSTEM, "Synfunktion");

  public static final Code SINNESFUNKTION_V1 =
      new Code("Sinnesfunktion", CODE_SYSTEM, "Övriga sinnesfunktioner och smärta");

  public static final Code SINNESFUNKTION_V2 =
      new Code("Sinnesfunktion", CODE_SYSTEM, "Sinnesfunktioner och smärta");

  public static final Code KOORDINATION =
      new Code("KOORDINATION", CODE_SYSTEM, "Balans, koordination och motorik");

  public static final Code ANNAN_KROPPSILIG_FUNKTION =
      new Code("ANNAN_KROPPSILIG_FUNKTION", CODE_SYSTEM, "Annan kroppslig funktion");

  public static final Code ANDNINGS_FUNKTION =
      new Code("ANDNINGS_FUNKTION", CODE_SYSTEM, "Andningsfunktion");
}
