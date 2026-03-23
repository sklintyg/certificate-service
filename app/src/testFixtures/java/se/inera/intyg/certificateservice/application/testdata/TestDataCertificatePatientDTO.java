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
package se.inera.intyg.certificateservice.application.testdata;

import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_CITY;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_FIRST_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_FULL_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_LAST_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_MIDDLE_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_STREET;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ZIP_CODE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.DECEASED_FALSE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.PROTECTED_PERSON_FALSE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.TEST_INDICATED_FALSE;

import se.inera.intyg.certificateservice.application.certificate.dto.PatientDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.PatientDTO.PatientDTOBuilder;
import se.inera.intyg.certificateservice.application.certificate.dto.PersonIdDTO;
import se.inera.intyg.certificateservice.application.common.dto.PersonIdTypeDTO;

public class TestDataCertificatePatientDTO {

  private TestDataCertificatePatientDTO() {
    throw new IllegalStateException("Utility class");
  }

  public static final PatientDTO ATHENA_REACT_ANDERSSON_DTO =
      athenaReactAnderssonDtoBuilder().build();

  public static PatientDTOBuilder athenaReactAnderssonDtoBuilder() {
    return PatientDTO.builder()
        .deceased(DECEASED_FALSE.value())
        .protectedPerson(PROTECTED_PERSON_FALSE.value())
        .testIndicated(TEST_INDICATED_FALSE.value())
        .personId(
            PersonIdDTO.builder()
                .type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER.name())
                .id(ATHENA_REACT_ANDERSSON_ID)
                .build())
        .city(ATHENA_REACT_ANDERSSON_CITY)
        .street(ATHENA_REACT_ANDERSSON_STREET)
        .zipCode(ATHENA_REACT_ANDERSSON_ZIP_CODE)
        .firstName(ATHENA_REACT_ANDERSSON_FIRST_NAME)
        .lastName(ATHENA_REACT_ANDERSSON_LAST_NAME)
        .middleName(ATHENA_REACT_ANDERSSON_MIDDLE_NAME)
        .fullName(ATHENA_REACT_ANDERSSON_FULL_NAME);
  }
}
