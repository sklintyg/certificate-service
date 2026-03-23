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

public class CodeSystemKvFkmu0005 {

  public static final String CODE_SYSTEM = "KV_FKMU_0005";

  public static final Code NEUROPSYKIATRISKT =
      new Code("NEUROPSYKIATRISKT", CODE_SYSTEM, "Neuropsykiatriskt utlåtande");

  public static final Code HABILITERING =
      new Code("HABILITERING", CODE_SYSTEM, "Underlag från habiliteringen");

  public static final Code ARBETSTERAPEUT =
      new Code("ARBETSTERAPEUT", CODE_SYSTEM, "Underlag från arbetsterapeut");

  public static final Code FYSIOTERAPEUT =
      new Code("FYSIOTERAPEUT", CODE_SYSTEM, "Underlag från fysioterapeut");

  public static final Code LOGOPED = new Code("LOGOPED", CODE_SYSTEM, "Underlag från logoped");

  public static final Code PSYKOLOG = new Code("PSYKOLOG", CODE_SYSTEM, "Underlag från psykolog");

  public static final Code VARDCENTRAL =
      new Code("VARDCENTRAL", CODE_SYSTEM, "Underlag från vårdcentral");

  public static final Code SPECIALISTKLINIK =
      new Code("SPECIALISTKLINIK", CODE_SYSTEM, "Utredning av annan specialistklinik");

  public static final Code SKOLHALSOVARD =
      new Code("SKOLHALSOVARD", CODE_SYSTEM, "Underlag från skolhälsovården");

  public static final Code VARD_UTOMLANDS =
      new Code("VARD_UTOMLANDS", CODE_SYSTEM, "Utredning från vårdinrättning utomlands");

  public static final Code OVRIGT = new Code("OVRIGT_UTLATANDE", CODE_SYSTEM, "Övrigt");

  public static final Code HORSELHABILITERING =
      new Code("HORSELHABILITERING", CODE_SYSTEM, "Underlag från hörselhabiliteringen");

  public static final Code SYNHABILITERINGEN =
      new Code("SYNHABILITERINGEN", CODE_SYSTEM, "Underlag från synhabiliteringen");

  public static final Code AUDIONOM = new Code("AUDIONOM", CODE_SYSTEM, "Underlag från audionom");

  public static final Code DIETIST = new Code("DIETIST", CODE_SYSTEM, "Underlag från dietist");

  public static final Code ORTOPTIST =
      new Code("ORTOPTIST", CODE_SYSTEM, "Underlag från ortoptist");

  public static final Code ORTOPEDTEKNIKER =
      new Code(
          "ORTOPEDTEKNIKER", CODE_SYSTEM, "Underlag från ortopedtekniker eller ortopedingenjör");

  private CodeSystemKvFkmu0005() {
    throw new IllegalStateException("Utility class");
  }
}
