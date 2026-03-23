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
package se.inera.intyg.certificateservice.domain.message.model;

public enum MessageType {
  COMPLEMENT("KOMPLT", "Komplettering"),
  REMINDER("PAMINN", "Påminnelse"),
  CONTACT("KONTKT", "Kontakt"),
  OTHER("OVRIGT", "Övrigt"),
  ANSWER("SVAR", "Svar"),
  COORDINATION("AVSTMN", "Avstämningsmöte"),
  MISSING("SAKNAS", "Välj typ av fråga");

  public static final String OID = "ffa59d8f-8d7e-46ae-ac9e-31804e8e8499";

  private final String code;
  private final String displayName;

  MessageType(String code, String displayName) {
    this.code = code;
    this.displayName = displayName;
  }

  public String code() {
    return code;
  }

  public String displayName() {
    return displayName;
  }
}
