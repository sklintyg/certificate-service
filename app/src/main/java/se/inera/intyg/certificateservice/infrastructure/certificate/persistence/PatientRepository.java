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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence;

import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper.PatientEntityMapper.toEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PatientEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.PatientEntityRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PatientRepository {

  private final PatientEntityRepository patientEntityRepository;
  private final MetadataVersionRepository metadataVersionRepository;

  public PatientEntity patient(Patient patient) {
    return patientEntityRepository
        .findById(patient.id().idWithoutDash())
        .map(patientEntity -> updatePatientVersion(patientEntity, patient))
        .orElseGet(() -> patientEntityRepository.save(toEntity(patient)));
  }

  private PatientEntity updatePatientVersion(PatientEntity patientEntity, Patient patient) {
    final var newPatientEntity = toEntity(patient);
    if (patientEntity.hasDiff(newPatientEntity)) {
      return metadataVersionRepository.savePatientVersion(patientEntity, newPatientEntity);
    }
    return patientEntity;
  }
}
