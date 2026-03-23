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
package se.inera.intyg.certificateservice.domain.common.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants;

class PersonIdTest {

  @Test
  void shallReturnBirthDateForPersonalIdentityNumber() {
    final var patientId =
        PersonId.builder()
            .id(TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID)
            .type(PersonIdType.PERSONAL_IDENTITY_NUMBER)
            .build();

    assertEquals(LocalDate.parse("1940-11-30"), patientId.birthDate());
  }

  @Test
  void shallReturnBirthDateForCoordinationNumber() {
    final var patientId =
        PersonId.builder().id("19400565-0512").type(PersonIdType.COORDINATION_NUMBER).build();

    assertEquals(LocalDate.parse("1940-05-05"), patientId.birthDate());
  }

  @Test
  void shallReturnPatientIdWithoutDashWhenWithDash() {
    final var patientIdWithDash =
        PersonId.builder()
            .id(TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID)
            .type(PersonIdType.PERSONAL_IDENTITY_NUMBER)
            .build();

    assertEquals(
        TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID_WITHOUT_DASH,
        patientIdWithDash.idWithoutDash());
  }

  @Test
  void shallReturnPatientIdWithoutDashWhenWithoutDash() {
    final var patientIdWithDash =
        PersonId.builder()
            .id(TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID_WITHOUT_DASH)
            .type(PersonIdType.PERSONAL_IDENTITY_NUMBER)
            .build();

    assertEquals(
        TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID_WITHOUT_DASH,
        patientIdWithDash.idWithoutDash());
  }

  @Test
  void shallReturnPatientIdWithDashWhenWithDash() {
    final var patientIdWithoutDash =
        PersonId.builder()
            .id(TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID)
            .type(PersonIdType.PERSONAL_IDENTITY_NUMBER)
            .build();

    assertEquals(
        TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID, patientIdWithoutDash.idWithDash());
  }

  @Test
  void shallReturnPatientIdWithDashWhenWithoutDash() {
    final var patientIdWithoutDash =
        PersonId.builder()
            .id(TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID_WITHOUT_DASH)
            .type(PersonIdType.PERSONAL_IDENTITY_NUMBER)
            .build();

    assertEquals(
        TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID, patientIdWithoutDash.idWithDash());
  }
}
