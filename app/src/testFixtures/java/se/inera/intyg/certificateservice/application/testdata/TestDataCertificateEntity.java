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

import static se.inera.intyg.certificateservice.application.testdata.TestDataCertificateDataEntity.CERTIFICATE_DATA_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCertificateExternalReferenceEntity.EXTERNAL_REFERENCE;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCertificateModelEntity.CERTIFICATE_MODEL_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCertificateRevokedEntity.REVOKED_MESSAGE;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCertificateRevokedEntity.REVOKED_REASON_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCertificateStatusEntity.STATUS_SIGNED_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCertificateXmlEntity.CERTIFICATE_XML_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataPatientEntity.ATHENA_REACT_ANDERSSON_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataStaffEntity.AJLA_DOKTOR_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataStaffEntity.ALF_DOKTOR_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataUnitEntity.ALFA_ALLERGIMOTTAGNINGEN_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataUnitEntity.ALFA_MEDICINCENTRUM_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataUnitEntity.ALFA_REGIONEN_ENTITY;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateConstants.CERTIFICATE_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateConstants.PARENT_CERTIFICATE_ID;

import java.time.LocalDateTime;
import java.time.ZoneId;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateEntity;

public class TestDataCertificateEntity {

  private TestDataCertificateEntity() {
    throw new IllegalStateException("Utility class");
  }

  public static final CertificateEntity CERTIFICATE_ENTITY = certificateEntityBuilder().build();
  public static final CertificateEntity PARENT_CERTIFICATE_ENTITY =
      certificateParentEntityBuilder().build();

  public static CertificateEntity.CertificateEntityBuilder certificateEntityBuilder() {
    return CertificateEntity.builder()
        .certificateId(CERTIFICATE_ID)
        .status(STATUS_SIGNED_ENTITY)
        .created(LocalDateTime.now().minusDays(1))
        .modified(LocalDateTime.now())
        .signed(LocalDateTime.now())
        .modified(LocalDateTime.now())
        .revision(1L)
        .careProvider(ALFA_REGIONEN_ENTITY)
        .careUnit(ALFA_MEDICINCENTRUM_ENTITY)
        .issuedOnUnit(ALFA_ALLERGIMOTTAGNINGEN_ENTITY)
        .issuedBy(AJLA_DOKTOR_ENTITY)
        .createdBy(ALF_DOKTOR_ENTITY)
        .patient(ATHENA_REACT_ANDERSSON_ENTITY)
        .data(CERTIFICATE_DATA_ENTITY)
        .xml(CERTIFICATE_XML_ENTITY)
        .sent(LocalDateTime.now(ZoneId.systemDefault()))
        .sentBy(AJLA_DOKTOR_ENTITY)
        .revoked(LocalDateTime.now(ZoneId.systemDefault()))
        .revokedBy(AJLA_DOKTOR_ENTITY)
        .revokedReason(REVOKED_REASON_ENTITY)
        .revokedMessage(REVOKED_MESSAGE)
        .externalReference(EXTERNAL_REFERENCE)
        .certificateModel(CERTIFICATE_MODEL_ENTITY)
        .readyForSignBy(AJLA_DOKTOR_ENTITY)
        .readyForSign(LocalDateTime.now())
        .forwarded(true);
  }

  public static CertificateEntity.CertificateEntityBuilder certificateParentEntityBuilder() {
    return CertificateEntity.builder()
        .certificateId(PARENT_CERTIFICATE_ID)
        .status(STATUS_SIGNED_ENTITY)
        .created(LocalDateTime.now().minusDays(1))
        .modified(LocalDateTime.now())
        .signed(LocalDateTime.now())
        .modified(LocalDateTime.now())
        .revision(1L)
        .careProvider(ALFA_REGIONEN_ENTITY)
        .careUnit(ALFA_MEDICINCENTRUM_ENTITY)
        .issuedOnUnit(ALFA_ALLERGIMOTTAGNINGEN_ENTITY)
        .createdBy(ALF_DOKTOR_ENTITY)
        .issuedBy(AJLA_DOKTOR_ENTITY)
        .patient(ATHENA_REACT_ANDERSSON_ENTITY)
        .data(CERTIFICATE_DATA_ENTITY)
        .xml(CERTIFICATE_XML_ENTITY)
        .sent(LocalDateTime.now(ZoneId.systemDefault()))
        .sentBy(AJLA_DOKTOR_ENTITY)
        .revoked(LocalDateTime.now(ZoneId.systemDefault()))
        .revokedBy(AJLA_DOKTOR_ENTITY)
        .revokedReason(REVOKED_REASON_ENTITY)
        .revokedMessage(REVOKED_MESSAGE)
        .externalReference(EXTERNAL_REFERENCE)
        .certificateModel(CERTIFICATE_MODEL_ENTITY);
  }
}
