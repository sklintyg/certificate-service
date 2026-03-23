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
package se.inera.intyg.certificateservice.domain.common.model;

public record HealthCareProfessionalLicence(String value) {

  public static final String OID = "1.2.752.29.23.1.6";

  public Code code() {
    return new Code(code(value), OID, value);
  }

  private String code(String value) {
    switch (value) {
      case "Apotekare":
        return "AP";
      case "Arbetsterapeut":
        return "AT";
      case "Audionom":
        return "AU";
      case "Barnmorska":
        return "BM";
      case "Biomedicinsk analytiker":
        return "BA";
      case "Dietist":
        return "DT";
      case "Fysioterapeut":
        return "FT";
      case "Hälso- och sjukvårdskurator":
        return "HK";
      case "Kiropraktor":
        return "KP";
      case "Logoped":
        return "LG";
      case "Läkare":
        return "LK";
      case "Naprapat":
        return "NA";
      case "Optiker":
        return "OP";
      case "Ortopedingenjör":
        return "OT";
      case "Psykolog":
        return "PS";
      case "Psykoterapeut":
        return "PT";
      case "Receptarie":
        return "RC";
      case "Röntgensjuksköterska":
        return "RS";
      case "Sjukgymnast":
        return "SG";
      case "Sjukhusfysiker":
        return "SF";
      case "Sjuksköterska":
        return "SJ";
      case "Tandhygienist":
        return "TH";
      case "Tandläkare":
        return "TL";
      default:
        return value;
    }
  }
}
