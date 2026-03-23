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

public class CodeSystemAktivitetsbegransning {

  public static final String CODE_SYSTEM = "AKTIVITETSBEGRANSNING";

  private CodeSystemAktivitetsbegransning() {
    throw new IllegalStateException("Utility class");
  }

  public static final Code LARANDE_BEGRANSNING =
      new Code(
          "LARANDE_BEGRANSNING",
          CODE_SYSTEM,
          "Lärande, tillämpa kunskap samt allmänna uppgifter och krav");

  public static final Code KOMMUNIKATION_BEGRANSNING =
      new Code("KOMMUNIKATION_BEGRANSNING", CODE_SYSTEM, "Kommunikation");

  public static final Code FORFLYTTNING_BEGRANSNING =
      new Code("FORFLYTTNING_BEGRANSNING", CODE_SYSTEM, "Förflyttning");

  public static final Code PERSONLIG_VARD_BEGRANSNING =
      new Code("PERSONLIG_VARD_BEGRANSNING", CODE_SYSTEM, "Personlig vård och sköta sin hälsa");

  public static final Code OVRIGA_BEGRANSNING =
      new Code("OVRIGA_BEGRANSNING", CODE_SYSTEM, "Övriga aktivitetsbegränsningar");
}
