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
package se.inera.intyg.certificateservice.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;
import se.inera.intyg.certificateservice.patient.service.FindPatientsService;

@ExtendWith(MockitoExtension.class)
class PatientInformationServiceTest {

  @Mock FindPatientsService findPatientsService;
  @InjectMocks PatientInformationService patientInformationService;

  @Nested
  class FindPersonTests {

    @Test
    void shouldReturnOptionalOfPatient() {
      final var personId = PersonId.builder().build();
      final var patient = Patient.builder().build();
      final var expectedResult = Optional.of(patient);

      when(findPatientsService.find(List.of(personId))).thenReturn(List.of(patient));

      final var result = patientInformationService.findPatient(personId);
      assertEquals(expectedResult, result);
    }

    @Test
    void shouldReturnOptionalEmptyIfPatientIsNotFound() {
      final var personId = PersonId.builder().build();

      when(findPatientsService.find(List.of(personId))).thenReturn(Collections.emptyList());

      final var result = patientInformationService.findPatient(personId);
      assertTrue(result.isEmpty());
    }
  }

  @Nested
  class FindPersonsTests {

    @Test
    void shouldReturnListOfPatient() {
      final var personId = PersonId.builder().build();
      final var patient = Patient.builder().build();
      final var expectedResult = List.of(patient);
      final var personIds = List.of(personId);

      when(findPatientsService.find(personIds)).thenReturn(List.of(patient));

      final var result = patientInformationService.findPatients(personIds);
      assertEquals(expectedResult, result);
    }

    @Test
    void shouldReturnEmptyListIfPatientIsNotFound() {
      final var personId = PersonId.builder().build();
      final var personIds = List.of(personId);

      when(findPatientsService.find(personIds)).thenReturn(Collections.emptyList());

      final var result = patientInformationService.findPatients(personIds);
      assertTrue(result.isEmpty());
    }
  }
}
