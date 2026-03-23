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
import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.RevokedReason.INCORRECT_PATIENT;
import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.RevokedReason.OTHER_SERIOUS_ERROR;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RevokedReasonEntityMapperTest {

  @Nested
  class MapWrongPatient {

    private static final String REASON_WRONG_PATIENT = INCORRECT_PATIENT.name();

    @Test
    void shallMapReasonToRevokedReasonEntity() {
      final var entity = RevokedReasonEntityMapper.toEntity(REASON_WRONG_PATIENT);
      assertEquals(INCORRECT_PATIENT.name(), entity.getReason());
    }

    @Test
    void shallMapKeyToRevokedReasonEntity() {
      final var entity = RevokedReasonEntityMapper.toEntity(REASON_WRONG_PATIENT);
      assertEquals(INCORRECT_PATIENT.getKey(), entity.getKey());
    }
  }

  @Nested
  class MapOtherSeriousError {

    private static final String REASON_OTHER_SERIOUS_ERROR = OTHER_SERIOUS_ERROR.name();

    @Test
    void shallMapReasonToRevokedReasonEntity() {
      final var entity = RevokedReasonEntityMapper.toEntity(REASON_OTHER_SERIOUS_ERROR);
      assertEquals(OTHER_SERIOUS_ERROR.name(), entity.getReason());
    }

    @Test
    void shallMapKeyToRevokedReasonEntity() {
      final var entity = RevokedReasonEntityMapper.toEntity(REASON_OTHER_SERIOUS_ERROR);
      assertEquals(OTHER_SERIOUS_ERROR.getKey(), entity.getKey());
    }
  }
}
