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

import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper.StaffEntityMapper.toEntity;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;
import se.inera.intyg.certificateservice.domain.staff.model.Staff;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.StaffEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper.StaffEntityMapper;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.StaffEntityRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StaffRepository {

  private final StaffEntityRepository staffEntityRepository;
  private final MetadataVersionRepository metadataVersionRepository;

  public StaffEntity staff(Staff issuer) {
    return staffEntityRepository
        .findByHsaId(issuer.hsaId().id())
        .map(staffEntity -> updateStaffVersion(staffEntity, issuer))
        .orElseGet(() -> staffEntityRepository.save(toEntity(issuer)));
  }

  public Map<HsaId, StaffEntity> staffs(Certificate certificate) {
    final var staffs = new ArrayList<Staff>();
    staffs.add(certificate.certificateMetaData().issuer());
    if (certificate.sent() != null && certificate.sent().sentBy() != null) {
      staffs.add(certificate.sent().sentBy());
    }

    if (certificate.revoked() != null && certificate.revoked().revokedBy() != null) {
      staffs.add(certificate.revoked().revokedBy());
    }

    if (certificate.readyForSign() != null && certificate.readyForSign().readyForSignBy() != null) {
      staffs.add(certificate.readyForSign().readyForSignBy());
    }

    final var uniqueStaffs = staffs.stream().distinct().toList();

    final var staffEntities =
        staffEntityRepository.findStaffEntitiesByHsaIdIn(
            uniqueStaffs.stream().map(s -> s.hsaId().id()).toList());

    final var staffEntityMap =
        staffEntities.stream()
            .collect(
                Collectors.toMap(staff -> HsaId.create(staff.getHsaId()), Function.identity()));

    uniqueStaffs.forEach(
        staff -> {
          if (!staffEntityMap.containsKey(staff.hsaId())) {
            staffEntityMap.put(staff.hsaId(), staffEntityRepository.save(toEntity(staff)));
          } else {
            staffEntityMap.replace(
                staff.hsaId(), updateStaffVersion(staffEntityMap.get(staff.hsaId()), staff));
          }
        });

    return staffEntityMap;
  }

  private StaffEntity updateStaffVersion(StaffEntity staffEntity, Staff staff) {
    final var newStaffEntity = StaffEntityMapper.toEntity(staff);
    if (staffEntity.hasDiff(newStaffEntity)) {
      return metadataVersionRepository.saveStaffVersion(staffEntity, newStaffEntity);
    }
    return staffEntity;
  }
}
