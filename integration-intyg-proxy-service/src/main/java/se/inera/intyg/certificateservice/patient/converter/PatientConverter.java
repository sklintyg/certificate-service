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
package se.inera.intyg.certificateservice.patient.converter;

import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.common.model.PersonIdType;
import se.inera.intyg.certificateservice.domain.patient.model.Deceased;
import se.inera.intyg.certificateservice.domain.patient.model.Name;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;
import se.inera.intyg.certificateservice.domain.patient.model.PersonAddress;
import se.inera.intyg.certificateservice.domain.patient.model.ProtectedPerson;
import se.inera.intyg.certificateservice.domain.patient.model.TestIndicated;
import se.inera.intyg.certificateservice.patient.dto.PersonDTO;

@Component
public class PatientConverter {

  public Patient convert(PersonDTO person) {
    return Patient.builder()
        .id(getPersonId(person.getPersonnummer()))
        .name(
            Name.builder()
                .firstName(person.getFornamn())
                .middleName(person.getMellannamn())
                .lastName(person.getEfternamn())
                .build())
        .deceased(new Deceased(person.isAvliden()))
        .address(
            PersonAddress.builder()
                .city(person.getPostort())
                .street(person.getPostadress())
                .zipCode(person.getPostnummer())
                .build())
        .protectedPerson(new ProtectedPerson(person.isSekretessmarkering()))
        .testIndicated(new TestIndicated(person.isTestIndicator()))
        .build();
  }

  private PersonId getPersonId(String patientId) {
    return PersonId.builder()
        .type(
            isCoordinationNumber(patientId)
                ? PersonIdType.COORDINATION_NUMBER
                : PersonIdType.PERSONAL_IDENTITY_NUMBER)
        .id(patientId)
        .build();
  }

  private boolean isCoordinationNumber(String patientId) {
    return Character.getNumericValue(patientId.charAt(6)) >= 6;
  }
}
