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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PatientEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PatientVersionEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.StaffEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.StaffVersionEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.UnitEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.UnitVersionEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper.PatientVersionEntityMapper;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper.StaffVersionEntityMapper;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper.UnitVersionEntityMapper;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.PatientEntityRepository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.PatientVersionEntityRepository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.StaffEntityRepository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.StaffVersionEntityRepository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.UnitEntityRepository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.UnitVersionEntityRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MetadataVersionRepository {

  private final StaffEntityRepository staffEntityRepository;
  private final UnitEntityRepository unitEntityRepository;
  private final PatientEntityRepository patientEntityRepository;
  private final StaffVersionEntityRepository staffVersionEntityRepository;
  private final PatientVersionEntityRepository patientVersionEntityRepository;
  private final UnitVersionEntityRepository unitVersionEntityRepository;

  @Transactional
  public StaffEntity saveStaffVersion(StaffEntity staffEntity, StaffEntity newStaffEntity) {
    final var staffVersionEntity = StaffVersionEntityMapper.toStaffVersion(staffEntity);
    staffEntity.updateWith(newStaffEntity);
    updateStaffVersionHistory(staffVersionEntity);
    return staffEntityRepository.save(staffEntity);
  }

  private void updateStaffVersionHistory(StaffVersionEntity staffVersionEntity) {
    final var latestVersion =
        staffVersionEntityRepository.findFirstByHsaIdOrderByValidFromDesc(
            staffVersionEntity.getHsaId());
    latestVersion.ifPresent(
        versionEntity -> staffVersionEntity.setValidFrom(versionEntity.getValidTo()));
    staffVersionEntityRepository.save(staffVersionEntity);
  }

  @Transactional
  public UnitEntity saveUnitVersion(UnitEntity unitEntity, UnitEntity newUnitEntity) {
    final var unitVersionEntity = UnitVersionEntityMapper.toUnitVersion(unitEntity);
    unitEntity.updateWith(newUnitEntity);
    updateUnitVersionHistory(unitVersionEntity);
    return unitEntityRepository.save(unitEntity);
  }

  private void updateUnitVersionHistory(UnitVersionEntity unitVersionEntity) {
    final var latestVersion =
        unitVersionEntityRepository.findFirstByHsaIdOrderByValidFromDesc(
            unitVersionEntity.getHsaId());
    latestVersion.ifPresent(
        versionEntity -> unitVersionEntity.setValidFrom(versionEntity.getValidTo()));
    unitVersionEntityRepository.save(unitVersionEntity);
  }

  @Transactional
  public PatientEntity savePatientVersion(
      PatientEntity patientEntity, PatientEntity newPatientEntity) {
    final var patientVersionEntity = PatientVersionEntityMapper.toPatientVersion(patientEntity);
    patientEntity.updateWith(newPatientEntity);
    updatePatientVersionHistory(patientVersionEntity);
    return patientEntityRepository.save(patientEntity);
  }

  private void updatePatientVersionHistory(PatientVersionEntity patientVersionEntity) {
    final var latestVersion =
        patientVersionEntityRepository.findFirstByIdOrderByValidFromDesc(
            patientVersionEntity.getId());
    latestVersion.ifPresent(
        versionEntity -> patientVersionEntity.setValidFrom(versionEntity.getValidTo()));
    patientVersionEntityRepository.save(patientVersionEntity);
  }
}
