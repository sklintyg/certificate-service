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

public record HsaId(String id) {

  public static final String OID = "1.2.752.129.2.1.4.1";

  public static HsaId create(String id) {
    return new HsaId(id);
  }

  public HsaId {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("Missing id");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof HsaId(String otherId)) {
      return id.equalsIgnoreCase(otherId);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return id.toUpperCase().hashCode();
  }
}
