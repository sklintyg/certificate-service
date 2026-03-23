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
import java.util.List;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.HealthcareProfessionalLicenceEmbeddable;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.HealthcareProfessionalLicenceVersionEmbeddable;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PaTitleEmbeddable;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PaTitleVersionEmbeddable;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.SpecialityEmbeddable;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.SpecialityVersionEmbeddable;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.StaffEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.StaffVersionEntity;

public class StaffVersionEntityMapper {

  private StaffVersionEntityMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static StaffVersionEntity toStaffVersion(StaffEntity staffEntity) {
    return StaffVersionEntity.builder()
        .hsaId(staffEntity.getHsaId())
        .firstName(staffEntity.getFirstName())
        .middleName(staffEntity.getMiddleName())
        .lastName(staffEntity.getLastName())
        .validFrom(null)
        .validTo(LocalDateTime.now())
        .role(staffEntity.getRole())
        .paTitles(copyPaTitles(staffEntity))
        .specialities(copySpecialities(staffEntity))
        .healthcareProfessionalLicences(copyLicences(staffEntity))
        .staff(staffEntity)
        .build();
  }

  public static StaffEntity toStaff(StaffVersionEntity entity) {
    return StaffEntity.builder()
        .hsaId(entity.getHsaId())
        .firstName(entity.getFirstName())
        .middleName(entity.getMiddleName())
        .lastName(entity.getLastName())
        .role(entity.getRole())
        .paTitles(copyPaTitles(entity))
        .specialities(copySpecialities(entity))
        .healthcareProfessionalLicences(copyLicences(entity))
        .build();
  }

  private static List<PaTitleVersionEmbeddable> copyPaTitles(StaffEntity existing) {
    if (existing.getPaTitles() == null) {
      return List.of();
    }
    return existing.getPaTitles().stream()
        .map(pt -> new PaTitleVersionEmbeddable(pt.getCode(), pt.getDescription()))
        .toList();
  }

  private static List<PaTitleEmbeddable> copyPaTitles(StaffVersionEntity existing) {
    if (existing.getPaTitles() == null) {
      return List.of();
    }
    return existing.getPaTitles().stream()
        .map(pt -> new PaTitleEmbeddable(pt.getCode(), pt.getDescription()))
        .toList();
  }

  private static List<SpecialityVersionEmbeddable> copySpecialities(StaffEntity existing) {
    if (existing.getSpecialities() == null) {
      return List.of();
    }
    return existing.getSpecialities().stream()
        .map(sp -> new SpecialityVersionEmbeddable(sp.getSpeciality()))
        .toList();
  }

  private static List<SpecialityEmbeddable> copySpecialities(StaffVersionEntity existing) {
    if (existing.getSpecialities() == null) {
      return List.of();
    }
    return existing.getSpecialities().stream()
        .map(sp -> new SpecialityEmbeddable(sp.getSpeciality()))
        .toList();
  }

  private static List<HealthcareProfessionalLicenceVersionEmbeddable> copyLicences(
      StaffEntity existing) {
    if (existing.getHealthcareProfessionalLicences() == null) {
      return List.of();
    }
    return existing.getHealthcareProfessionalLicences().stream()
        .map(
            l ->
                new HealthcareProfessionalLicenceVersionEmbeddable(
                    l.getHealthcareProfessionalLicence()))
        .toList();
  }

  private static List<HealthcareProfessionalLicenceEmbeddable> copyLicences(
      StaffVersionEntity existing) {
    if (existing.getHealthcareProfessionalLicences() == null) {
      return List.of();
    }
    return existing.getHealthcareProfessionalLicences().stream()
        .map(l -> new HealthcareProfessionalLicenceEmbeddable(l.getHealthcareProfessionalLicence()))
        .toList();
  }
}
