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

import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PersonId {

  String id;
  PersonIdType type;

  public LocalDate birthDate() {
    return LocalDate.of(year(), month(), dayOfMonth());
  }

  private int year() {
    return Integer.parseInt(id.substring(0, 4));
  }

  private int month() {
    return Integer.parseInt(id.substring(4, 6));
  }

  private int dayOfMonth() {
    final var dayOfMonth = Integer.parseInt(id.substring(6, 8));
    if (PersonIdType.COORDINATION_NUMBER.equals(type)) {
      return dayOfMonth - 60;
    }
    return dayOfMonth;
  }

  public String idWithoutDash() {
    return id.replace("-", "");
  }

  public String idWithDash() {
    if (id.contains("-")) {
      return id;
    }
    return String.join("-", id.substring(0, 8), id.substring(8));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PersonId other)) {
      return false;
    }
    return type == other.type && idWithoutDash().equals(other.idWithoutDash());
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(type, idWithoutDash());
  }
}
