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

import static se.inera.intyg.certificateservice.application.testdata.TestDataCertificateEntity.CERTIFICATE_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCertificateEntity.PARENT_CERTIFICATE_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCertificateRelationTypeEntity.CERTIFICATE_RELATION_TYPE_ENTITY;

import java.time.LocalDateTime;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateRelationEntity;

public class TestDataCertificateRelationEntity {

  private TestDataCertificateRelationEntity() {
    throw new IllegalStateException("Utility class");
  }

  public static final CertificateRelationEntity CERTIFICATE_RELATION_ENTITY =
      certificateRelationEntityBuilder().build();
  public static final CertificateRelationEntity CERTIFICATE_PARENT_RELATION_ENTITY =
      certificateParentRelationEntityBuilder().build();

  public static CertificateRelationEntity.CertificateRelationEntityBuilder
      certificateRelationEntityBuilder() {
    return CertificateRelationEntity.builder()
        .parentCertificate(PARENT_CERTIFICATE_ENTITY)
        .childCertificate(CERTIFICATE_ENTITY)
        .created(LocalDateTime.now())
        .certificateRelationType(CERTIFICATE_RELATION_TYPE_ENTITY);
  }

  public static CertificateRelationEntity.CertificateRelationEntityBuilder
      certificateParentRelationEntityBuilder() {
    return CertificateRelationEntity.builder()
        .parentCertificate(CERTIFICATE_ENTITY)
        .childCertificate(PARENT_CERTIFICATE_ENTITY)
        .created(LocalDateTime.now())
        .certificateRelationType(CERTIFICATE_RELATION_TYPE_ENTITY);
  }
}
