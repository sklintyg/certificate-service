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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PatientEntity;

public class PatientEntitySpecification {

  private PatientEntitySpecification() {}

  public static Specification<CertificateEntity> equalsPatient(PersonId personId) {
    return (root, query, criteriaBuilder) -> {
      Join<PatientEntity, CertificateEntity> patient = root.join("patient");
      return criteriaBuilder.equal(patient.get("id"), personId.idWithoutDash());
    };
  }

  public static Specification<MessageEntity> equalsPatientForMessage(PersonId personId) {
    return (root, query, criteriaBuilder) -> {
      Join<CertificateEntity, MessageEntity> certificate = root.join("certificate");
      Join<PatientEntity, CertificateEntity> patient = certificate.join("patient");
      return criteriaBuilder.equal(patient.get("id"), personId.idWithoutDash());
    };
  }
}
