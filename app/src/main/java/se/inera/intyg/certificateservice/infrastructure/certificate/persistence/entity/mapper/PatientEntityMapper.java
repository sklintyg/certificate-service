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

import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.common.model.PersonIdType;
import se.inera.intyg.certificateservice.domain.patient.model.Deceased;
import se.inera.intyg.certificateservice.domain.patient.model.Name;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;
import se.inera.intyg.certificateservice.domain.patient.model.ProtectedPerson;
import se.inera.intyg.certificateservice.domain.patient.model.TestIndicated;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PatientEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PatientIdTypeEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PersonEntityIdType;

public class PatientEntityMapper {

  private PatientEntityMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static PatientEntity toEntity(Patient patient) {
    final var type = PersonEntityIdType.valueOf(patient.id().type().name());

    return PatientEntity.builder()
        .id(patient.id().idWithoutDash())
        .type(PatientIdTypeEntity.builder().key(type.getKey()).type(type.name()).build())
        .firstName(patient.name().firstName())
        .lastName(patient.name().lastName())
        .middleName(patient.name().middleName())
        .testIndicated(patient.testIndicated().value())
        .protectedPerson(patient.protectedPerson().value())
        .deceased(patient.deceased().value())
        .build();
  }

  public static Patient toDomain(PatientEntity patientEntity) {

    return Patient.builder()
        .id(
            PersonId.builder()
                .id(patientEntity.getId())
                .type(PersonIdType.valueOf(patientEntity.getType().getType()))
                .build())
        .protectedPerson(new ProtectedPerson(patientEntity.isProtectedPerson()))
        .name(
            Name.builder()
                .lastName(patientEntity.getLastName())
                .middleName(patientEntity.getMiddleName())
                .firstName(patientEntity.getFirstName())
                .build())
        .deceased(new Deceased(patientEntity.isDeceased()))
        .testIndicated(new TestIndicated(patientEntity.isTestIndicated()))
        .build();
  }
}
