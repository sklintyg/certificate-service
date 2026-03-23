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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.application.testdata.TestDataStaffEntity.AJLA_DOKTOR_ENTITY;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataStaff.AJLA_DOKTOR;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.HealthcareProfessionalLicenceEmbeddable;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PaTitleEmbeddable;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.SpecialityEmbeddable;

class StaffEntityMapperTest {

  @Nested
  class ToEntity {

    @Test
    void shouldMapStaff() {
      assertEquals(AJLA_DOKTOR_ENTITY, StaffEntityMapper.toEntity(AJLA_DOKTOR));
    }

    @Test
    void shouldReturnMutableListsForHibernateToWork() {
      final var entity = StaffEntityMapper.toEntity(AJLA_DOKTOR);
      assertAll(
          () ->
              assertDoesNotThrow(
                  () -> entity.getPaTitles().add(PaTitleEmbeddable.builder().build())),
          () ->
              assertDoesNotThrow(
                  () -> entity.getSpecialities().add(SpecialityEmbeddable.builder().build())),
          () ->
              assertDoesNotThrow(
                  () ->
                      entity
                          .getHealthcareProfessionalLicences()
                          .add(HealthcareProfessionalLicenceEmbeddable.builder().build())));
    }
  }

  @Nested
  class ToDomain {

    @Test
    void shouldMapStaff() {
      assertEquals(AJLA_DOKTOR, StaffEntityMapper.toDomain(AJLA_DOKTOR_ENTITY));
    }
  }
}
