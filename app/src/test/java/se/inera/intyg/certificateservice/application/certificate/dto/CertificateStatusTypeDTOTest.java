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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO.LOCKED;
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO.REVOKED;
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO.SIGNED;
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO.UNSIGNED;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;

class CertificateStatusTypeDTOTest {

  @Test
  void shallConvertDraft() {
    assertEquals(UNSIGNED, CertificateStatusTypeDTO.toType(Status.DRAFT));
  }

  @Test
  void shallConvertRevoked() {
    assertEquals(REVOKED, CertificateStatusTypeDTO.toType(Status.REVOKED));
  }

  @Test
  void shallConvertSigned() {
    assertEquals(SIGNED, CertificateStatusTypeDTO.toType(Status.SIGNED));
  }

  @Test
  void shallConvertLockedDraft() {
    assertEquals(LOCKED, CertificateStatusTypeDTO.toType(Status.LOCKED_DRAFT));
  }

  @Test
  void shallThrowIfConvertDeletedDraft() {
    assertThrows(
        IllegalArgumentException.class,
        () -> CertificateStatusTypeDTO.toType(Status.DELETED_DRAFT));
  }
}
