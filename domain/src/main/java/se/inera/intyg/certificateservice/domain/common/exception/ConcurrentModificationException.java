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
package se.inera.intyg.certificateservice.domain.common.exception;

import lombok.Getter;
import se.inera.intyg.certificateservice.domain.unit.model.IssuingUnit;
import se.inera.intyg.certificateservice.domain.user.model.User;

@Getter
public class ConcurrentModificationException extends RuntimeException {

  private final transient User user;
  private final transient IssuingUnit unit;

  public ConcurrentModificationException(String message, User user, IssuingUnit unit) {
    super(message);
    this.unit = unit;
    this.user = user;
  }
}
