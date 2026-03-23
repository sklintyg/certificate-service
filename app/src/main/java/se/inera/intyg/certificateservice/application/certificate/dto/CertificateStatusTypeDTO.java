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
package se.inera.intyg.certificateservice.application.certificate.dto;

import se.inera.intyg.certificateservice.domain.certificate.model.Status;

public enum CertificateStatusTypeDTO {
  UNSIGNED,
  LOCKED,
  SIGNED,
  REVOKED,
  LOCKED_REVOKED;

  public static CertificateStatusTypeDTO toType(Status status) {
    return switch (status) {
      case DRAFT -> UNSIGNED;
      case REVOKED -> REVOKED;
      case SIGNED -> SIGNED;
      case LOCKED_DRAFT -> LOCKED;
      case DELETED_DRAFT ->
          throw new IllegalArgumentException("Not able to convert status '%s'".formatted(status));
    };
  }

  public static Status toStatus(CertificateStatusTypeDTO statusTypeDTO) {
    return switch (statusTypeDTO) {
      case UNSIGNED -> Status.DRAFT;
      case LOCKED -> Status.LOCKED_DRAFT;
      case SIGNED -> Status.SIGNED;
      case REVOKED, LOCKED_REVOKED -> Status.REVOKED;
    };
  }
}
