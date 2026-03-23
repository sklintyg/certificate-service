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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.application.testdata.TestDataUnitEntity.ALFA_ALLERGIMOTTAGNINGEN_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataUnitEntity.ALFA_MEDICINCENTRUM_ENTITY;
import static se.inera.intyg.certificateservice.application.testdata.TestDataUnitEntity.ALFA_REGIONEN_ENTITY;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProvider.ALFA_REGIONEN;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareUnit.ALFA_MEDICINCENTRUM;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataSubUnit.ALFA_ALLERGIMOTTAGNINGEN;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UnitEntityMapperTest {

  @Nested
  class ToEntity {

    @Test
    void shallMapCareProvider() {
      assertEquals(ALFA_REGIONEN_ENTITY, UnitEntityMapper.toEntity(ALFA_REGIONEN));
    }

    @Test
    void shallMapCareUnit() {
      assertEquals(ALFA_MEDICINCENTRUM_ENTITY, UnitEntityMapper.toEntity(ALFA_MEDICINCENTRUM));
    }

    @Test
    void shallMapSubUnit() {
      assertEquals(
          ALFA_ALLERGIMOTTAGNINGEN_ENTITY, UnitEntityMapper.toEntity(ALFA_ALLERGIMOTTAGNINGEN));
    }
  }

  @Nested
  class ToDomain {

    @Test
    void shallMapCareProvider() {
      assertEquals(ALFA_REGIONEN, UnitEntityMapper.toCareProviderDomain(ALFA_REGIONEN_ENTITY));
    }

    @Test
    void shallMapCareUnit() {
      assertEquals(
          ALFA_MEDICINCENTRUM, UnitEntityMapper.toCareUnitDomain(ALFA_MEDICINCENTRUM_ENTITY));
    }

    @Test
    void shallMapSubUnit() {
      assertEquals(
          ALFA_ALLERGIMOTTAGNINGEN,
          UnitEntityMapper.toSubUnitDomain(ALFA_ALLERGIMOTTAGNINGEN_ENTITY));
    }

    @Test
    void shallMapIssuedUnitOfCareUnit() {
      assertEquals(
          ALFA_MEDICINCENTRUM, UnitEntityMapper.toIssuingUnitDomain(ALFA_MEDICINCENTRUM_ENTITY));
    }

    @Test
    void shallMapIssuedUnitOfSubUnit() {
      assertEquals(
          ALFA_ALLERGIMOTTAGNINGEN,
          UnitEntityMapper.toIssuingUnitDomain(ALFA_ALLERGIMOTTAGNINGEN_ENTITY));
    }
  }
}
