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
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.UnitEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.UnitVersionEntity;

public class UnitVersionEntityMapper {

  private UnitVersionEntityMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static UnitVersionEntity toUnitVersion(UnitEntity unitEntity) {
    return UnitVersionEntity.builder()
        .hsaId(unitEntity.getHsaId())
        .name(unitEntity.getName())
        .address(unitEntity.getAddress())
        .zipCode(unitEntity.getZipCode())
        .city(unitEntity.getCity())
        .phoneNumber(unitEntity.getPhoneNumber())
        .email(unitEntity.getEmail())
        .workplaceCode(unitEntity.getWorkplaceCode())
        .type(unitEntity.getType())
        .validTo(LocalDateTime.now())
        .validFrom(null)
        .unit(unitEntity)
        .build();
  }

  public static UnitEntity toUnit(UnitVersionEntity unitVersionEntity) {
    return UnitEntity.builder()
        .hsaId(unitVersionEntity.getHsaId())
        .name(unitVersionEntity.getName())
        .address(unitVersionEntity.getAddress())
        .zipCode(unitVersionEntity.getZipCode())
        .city(unitVersionEntity.getCity())
        .phoneNumber(unitVersionEntity.getPhoneNumber())
        .email(unitVersionEntity.getEmail())
        .workplaceCode(unitVersionEntity.getWorkplaceCode())
        .type(unitVersionEntity.getType())
        .build();
  }
}
