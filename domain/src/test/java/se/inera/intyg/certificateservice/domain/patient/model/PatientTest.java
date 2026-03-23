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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;

class PatientTest {

  @Test
  void shouldReturnAge0() {
    final var patient =
        Patient.builder()
            .id(
                PersonId.builder()
                    .id(
                        LocalDate.now().minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE)
                            + "-0101")
                    .build())
            .build();

    assertEquals(0, patient.getAge());
  }

  @Test
  void shouldReturnAge1() {
    final var patient =
        Patient.builder()
            .id(
                PersonId.builder()
                    .id(
                        LocalDate.now()
                                .minusDays(1)
                                .minusYears(1)
                                .format(DateTimeFormatter.BASIC_ISO_DATE)
                            + "-0101")
                    .build())
            .build();

    assertEquals(1, patient.getAge());
  }

  @Test
  void shouldReturnAge10() {
    final var patient =
        Patient.builder()
            .id(
                PersonId.builder()
                    .id(
                        LocalDate.now()
                                .minusDays(1)
                                .minusYears(10)
                                .format(DateTimeFormatter.BASIC_ISO_DATE)
                            + "-0101")
                    .build())
            .build();

    assertEquals(10, patient.getAge());
  }
}
