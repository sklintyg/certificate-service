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
package se.inera.intyg.certificateservice.application.unit.service.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUserDTO.AJLA_DOCTOR_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUserDTO.ajlaDoktorDtoBuilder;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.unit.dto.UnitStatisticsRequest;
import se.inera.intyg.certificateservice.application.unit.dto.UnitStatisticsRequest.UnitStatisticsRequestBuilder;

class UnitStatisticsRequestValidatorTest {

  private static final String UNIT_ID = "unitId";
  private UnitStatisticsRequestValidator getUnitCertificatesInfoRequestValidator;
  private UnitStatisticsRequestBuilder requestBuilder;

  @BeforeEach
  void setUp() {
    getUnitCertificatesInfoRequestValidator = new UnitStatisticsRequestValidator();
    requestBuilder =
        UnitStatisticsRequest.builder().user(AJLA_DOCTOR_DTO).issuedByUnitIds(List.of(UNIT_ID));
  }

  @Test
  void validRequest() {
    getUnitCertificatesInfoRequestValidator.validate(requestBuilder.build());
  }

  @Nested
  class UserValidation {

    @Test
    void shallThrowIfUserIsNull() {
      final var request = requestBuilder.user(null).build();

      final var illegalArgumentException =
          assertThrows(
              IllegalArgumentException.class,
              () -> getUnitCertificatesInfoRequestValidator.validate(request));

      assertEquals("Required parameter missing: User", illegalArgumentException.getMessage());
    }

    @Test
    void shallThrowIfIdIsNull() {
      final var request = requestBuilder.user(ajlaDoktorDtoBuilder().id(null).build()).build();

      final var illegalArgumentException =
          assertThrows(
              IllegalArgumentException.class,
              () -> getUnitCertificatesInfoRequestValidator.validate(request));

      assertEquals("Required parameter missing: User.id", illegalArgumentException.getMessage());
    }

    @Test
    void shallThrowIfIdIsEmpty() {
      final var request = requestBuilder.user(ajlaDoktorDtoBuilder().id("").build()).build();

      final var illegalArgumentException =
          assertThrows(
              IllegalArgumentException.class,
              () -> getUnitCertificatesInfoRequestValidator.validate(request));

      assertEquals("Required parameter missing: User.id", illegalArgumentException.getMessage());
    }

    @Test
    void shallThrowIfRoleIsNull() {
      final var request = requestBuilder.user(ajlaDoktorDtoBuilder().role(null).build()).build();

      final var illegalArgumentException =
          assertThrows(
              IllegalArgumentException.class,
              () -> getUnitCertificatesInfoRequestValidator.validate(request));

      assertEquals("Required parameter missing: User.role", illegalArgumentException.getMessage());
    }

    @Test
    void shallThrowIfBlockedIsNull() {
      final var request = requestBuilder.user(ajlaDoktorDtoBuilder().blocked(null).build()).build();

      final var illegalArgumentException =
          assertThrows(
              IllegalArgumentException.class,
              () -> getUnitCertificatesInfoRequestValidator.validate(request));

      assertEquals(
          "Required parameter missing: User.blocked", illegalArgumentException.getMessage());
    }

    @Test
    void shallThrowIfAgreementIsNull() {
      final var request =
          requestBuilder.user(ajlaDoktorDtoBuilder().agreement(null).build()).build();

      final var illegalArgumentException =
          assertThrows(
              IllegalArgumentException.class,
              () -> getUnitCertificatesInfoRequestValidator.validate(request));

      assertEquals(
          "Required parameter missing: User.agreement", illegalArgumentException.getMessage());
    }

    @Test
    void shallThrowIfAllowCopyIsNull() {
      final var request =
          requestBuilder.user(ajlaDoktorDtoBuilder().allowCopy(null).build()).build();

      final var illegalArgumentException =
          assertThrows(
              IllegalArgumentException.class,
              () -> getUnitCertificatesInfoRequestValidator.validate(request));

      assertEquals(
          "Required parameter missing: User.allowCopy", illegalArgumentException.getMessage());
    }
  }

  @Nested
  class UnitIdsValidation {

    @Test
    void shallThrowIfListIsNull() {
      final var request = requestBuilder.issuedByUnitIds(null).build();

      final var illegalArgumentException =
          assertThrows(
              IllegalArgumentException.class,
              () -> getUnitCertificatesInfoRequestValidator.validate(request));

      assertEquals(
          "Required parameter missing: IssuedByUnitIds", illegalArgumentException.getMessage());
    }

    @Test
    void shallThrowIfListIsEmpty() {
      final var request = requestBuilder.issuedByUnitIds(Collections.emptyList()).build();

      final var illegalArgumentException =
          assertThrows(
              IllegalArgumentException.class,
              () -> getUnitCertificatesInfoRequestValidator.validate(request));

      assertEquals(
          "Required parameter missing: IssuedByUnitIds", illegalArgumentException.getMessage());
    }
  }
}
