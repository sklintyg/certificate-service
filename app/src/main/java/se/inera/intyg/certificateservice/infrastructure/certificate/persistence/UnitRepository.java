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

import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper.UnitEntityMapper.toEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateservice.domain.unit.model.CareProvider;
import se.inera.intyg.certificateservice.domain.unit.model.CareUnit;
import se.inera.intyg.certificateservice.domain.unit.model.IssuingUnit;
import se.inera.intyg.certificateservice.domain.unit.model.SubUnit;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.UnitEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.UnitEntityRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UnitRepository {

  private final UnitEntityRepository unitEntityRepository;
  private final MetadataVersionRepository metadataVersionRepository;

  public UnitEntity careProvider(CareProvider careProvider) {
    return unitEntityRepository
        .findByHsaId(careProvider.hsaId().id())
        .map(unitEntity -> saveUnit(unitEntity, toEntity(careProvider)))
        .orElseGet(() -> unitEntityRepository.save(toEntity(careProvider)));
  }

  public UnitEntity careUnit(CareUnit careUnit) {
    return unitEntityRepository
        .findByHsaId(careUnit.hsaId().id())
        .map(unitEntity -> saveUnit(unitEntity, toEntity(careUnit)))
        .orElseGet(() -> unitEntityRepository.save(toEntity(careUnit)));
  }

  public UnitEntity subUnit(SubUnit subUnit) {
    return unitEntityRepository
        .findByHsaId(subUnit.hsaId().id())
        .map(unitEntity -> saveUnit(unitEntity, toEntity(subUnit)))
        .orElseGet(() -> unitEntityRepository.save(toEntity(subUnit)));
  }

  public UnitEntity issuingUnit(IssuingUnit issuingUnit) {
    if (issuingUnit instanceof CareUnit careUnit) {
      return careUnit(careUnit);
    }

    if (issuingUnit instanceof SubUnit subUnit) {
      return subUnit(subUnit);
    }

    throw new IllegalStateException(
        "Could not map IssuingUnit due to unknown type: '%s'".formatted(issuingUnit));
  }

  private UnitEntity saveUnit(UnitEntity unitEntity, UnitEntity newUnitEntity) {
    if (unitEntity.hasDiff(newUnitEntity)) {
      return metadataVersionRepository.saveUnitVersion(unitEntity, newUnitEntity);
    }
    return unitEntity;
  }
}
