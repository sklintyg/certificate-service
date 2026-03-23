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
package se.inera.intyg.certificateservice.domain.patient.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_FIRST_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_LAST_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_MIDDLE_NAME;

import org.junit.jupiter.api.Test;

class NameTest {

  @Test
  void shallCombineFirstMiddleLastToFullName() {
    final var expectedFullName =
        "%s %s %s"
            .formatted(
                ATHENA_REACT_ANDERSSON_FIRST_NAME,
                ATHENA_REACT_ANDERSSON_MIDDLE_NAME,
                ATHENA_REACT_ANDERSSON_LAST_NAME);

    final var name =
        Name.builder()
            .firstName(ATHENA_REACT_ANDERSSON_FIRST_NAME)
            .middleName(ATHENA_REACT_ANDERSSON_MIDDLE_NAME)
            .lastName(ATHENA_REACT_ANDERSSON_LAST_NAME)
            .build();

    assertEquals(expectedFullName, name.fullName());
  }

  @Test
  void shallCombineFirstLastToFullName() {
    final var expectedFullName =
        "%s %s".formatted(ATHENA_REACT_ANDERSSON_FIRST_NAME, ATHENA_REACT_ANDERSSON_LAST_NAME);

    final var name =
        Name.builder()
            .firstName(ATHENA_REACT_ANDERSSON_FIRST_NAME)
            .lastName(ATHENA_REACT_ANDERSSON_LAST_NAME)
            .build();

    assertEquals(expectedFullName, name.fullName());
  }

  @Test
  void shallCombineMiddleLastToFullName() {
    final var expectedFullName =
        "%s %s".formatted(ATHENA_REACT_ANDERSSON_MIDDLE_NAME, ATHENA_REACT_ANDERSSON_LAST_NAME);

    final var name =
        Name.builder()
            .middleName(ATHENA_REACT_ANDERSSON_MIDDLE_NAME)
            .lastName(ATHENA_REACT_ANDERSSON_LAST_NAME)
            .build();

    assertEquals(expectedFullName, name.fullName());
  }

  @Test
  void shallCombineFirstMiddleToFullName() {
    final var expectedFullName =
        "%s %s".formatted(ATHENA_REACT_ANDERSSON_FIRST_NAME, ATHENA_REACT_ANDERSSON_MIDDLE_NAME);

    final var name =
        Name.builder()
            .firstName(ATHENA_REACT_ANDERSSON_FIRST_NAME)
            .middleName(ATHENA_REACT_ANDERSSON_MIDDLE_NAME)
            .build();

    assertEquals(expectedFullName, name.fullName());
  }

  @Test
  void shallCombineLastToFullName() {
    final var expectedFullName = "%s".formatted(ATHENA_REACT_ANDERSSON_LAST_NAME);

    final var name = Name.builder().lastName(ATHENA_REACT_ANDERSSON_LAST_NAME).build();

    assertEquals(expectedFullName, name.fullName());
  }

  @Test
  void shallCombineFirstToFullName() {
    final var expectedFullName = "%s".formatted(ATHENA_REACT_ANDERSSON_FIRST_NAME);

    final var name = Name.builder().firstName(ATHENA_REACT_ANDERSSON_FIRST_NAME).build();

    assertEquals(expectedFullName, name.fullName());
  }

  @Test
  void shallCombineMiddleToFullName() {
    final var expectedFullName = "%s".formatted(ATHENA_REACT_ANDERSSON_MIDDLE_NAME);

    final var name = Name.builder().middleName(ATHENA_REACT_ANDERSSON_MIDDLE_NAME).build();

    assertEquals(expectedFullName, name.fullName());
  }
}
