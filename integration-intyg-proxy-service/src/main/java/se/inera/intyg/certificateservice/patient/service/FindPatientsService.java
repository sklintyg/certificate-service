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
package se.inera.intyg.certificateservice.patient.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;
import se.inera.intyg.certificateservice.patient.converter.PatientConverter;
import se.inera.intyg.certificateservice.patient.dto.PersonResponseDTO;
import se.inera.intyg.certificateservice.patient.dto.PersonsRequestDTO;
import se.inera.intyg.certificateservice.patient.dto.StatusDTOType;
import se.inera.intyg.certificateservice.patient.integration.IPSIntegrationService;

@Service
@RequiredArgsConstructor
public class FindPatientsService {

  private final IPSIntegrationService ipsIntegrationService;
  private final PatientConverter patientConverter;

  public List<Patient> find(List<PersonId> personIds) {
    final var personResponse =
        ipsIntegrationService.findPersons(
            PersonsRequestDTO.builder()
                .personIds(personIds.stream().map(PersonId::id).distinct().toList())
                .build());

    return personResponse.getPersons().stream()
        .filter(response -> response.getStatus().equals(StatusDTOType.FOUND))
        .map(PersonResponseDTO::getPerson)
        .map(patientConverter::convert)
        .toList();
  }
}
