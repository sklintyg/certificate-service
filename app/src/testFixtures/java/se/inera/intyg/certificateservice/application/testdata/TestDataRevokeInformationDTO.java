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
package se.inera.intyg.certificateservice.application.testdata;

import se.inera.intyg.certificateservice.application.certificate.dto.RevokeInformationDTO;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.RevokedReason;

public class TestDataRevokeInformationDTO {

  private static final String REASON = RevokedReason.INCORRECT_PATIENT.name();
  private static final String MESSAGE = "REVOKE_MESSAGE";

  private TestDataRevokeInformationDTO() {
    throw new IllegalStateException("Utility class");
  }

  public static final RevokeInformationDTO REVOKE_INFORMATION_DTO =
      revokeInformationDTOBuilder().build();

  public static RevokeInformationDTO.RevokeInformationDTOBuilder revokeInformationDTOBuilder() {
    return RevokeInformationDTO.builder().reason(REASON).message(MESSAGE);
  }
}
