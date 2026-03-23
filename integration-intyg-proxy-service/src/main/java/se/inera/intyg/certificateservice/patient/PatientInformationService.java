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

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;
import se.inera.intyg.certificateservice.domain.patient.service.PatientInformationProvider;
import se.inera.intyg.certificateservice.patient.service.FindPatientsService;

@Service
@RequiredArgsConstructor
public class PatientInformationService implements PatientInformationProvider {

  private final FindPatientsService findPatientsService;

  @Override
  public Optional<Patient> findPatient(PersonId personId) {
    return findPatients(List.of(personId)).stream().findFirst();
  }

  @Override
  public List<Patient> findPatients(List<PersonId> personIds) {
    return findPatientsService.find(personIds);
  }
}
