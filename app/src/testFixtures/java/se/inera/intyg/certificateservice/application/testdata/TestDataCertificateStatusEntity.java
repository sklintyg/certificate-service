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

import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateStatus;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateStatusEntity;

public class TestDataCertificateStatusEntity {

  private TestDataCertificateStatusEntity() {
    throw new IllegalStateException("Utility class");
  }

  public static final CertificateStatusEntity STATUS_SIGNED_ENTITY =
      statusSignedEntityBuilder().build();
  public static final CertificateStatusEntity STATUS_DRAFT_ENTITY =
      statusDraftEntityBuilder().build();

  public static CertificateStatusEntity.CertificateStatusEntityBuilder statusSignedEntityBuilder() {
    return CertificateStatusEntity.builder()
        .key(CertificateStatus.SIGNED.getKey())
        .status(CertificateStatus.SIGNED.name());
  }

  public static CertificateStatusEntity.CertificateStatusEntityBuilder statusDraftEntityBuilder() {
    return CertificateStatusEntity.builder()
        .key(CertificateStatus.DRAFT.getKey())
        .status(CertificateStatus.DRAFT.name());
  }
}
