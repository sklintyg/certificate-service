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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper;

import java.time.LocalDateTime;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PatientEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PatientVersionEntity;

public class PatientVersionEntityMapper {

  private PatientVersionEntityMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static PatientVersionEntity toPatientVersion(PatientEntity patientEntity) {
    return PatientVersionEntity.builder()
        .id(patientEntity.getId())
        .type(patientEntity.getType())
        .firstName(patientEntity.getFirstName())
        .middleName(patientEntity.getMiddleName())
        .lastName(patientEntity.getLastName())
        .validFrom(null)
        .validTo(LocalDateTime.now())
        .protectedPerson(patientEntity.isProtectedPerson())
        .deceased(patientEntity.isDeceased())
        .testIndicated(patientEntity.isTestIndicated())
        .patient(patientEntity)
        .build();
  }

  public static PatientEntity toPatient(PatientVersionEntity patientVersionEntity) {
    return PatientEntity.builder()
        .id(patientVersionEntity.getId())
        .type(patientVersionEntity.getType())
        .firstName(patientVersionEntity.getFirstName())
        .middleName(patientVersionEntity.getMiddleName())
        .lastName(patientVersionEntity.getLastName())
        .protectedPerson(patientVersionEntity.isProtectedPerson())
        .deceased(patientVersionEntity.isDeceased())
        .testIndicated(patientVersionEntity.isTestIndicated())
        .build();
  }
}
