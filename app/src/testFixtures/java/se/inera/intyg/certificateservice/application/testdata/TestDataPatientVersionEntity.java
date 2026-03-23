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

import static se.inera.intyg.certificateservice.application.testdata.TestDataPatientEntity.ATHENA_REACT_ANDERSSON_ENTITY;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_DECEASED;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_FIRST_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID_WITHOUT_DASH;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_LAST_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_MIDDLE_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_PROTECTED_PERSON;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_TEST_INDICATED;

import java.time.LocalDateTime;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PatientIdTypeEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PatientVersionEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PersonEntityIdType;

public class TestDataPatientVersionEntity {

  private TestDataPatientVersionEntity() {
    throw new IllegalStateException("Utility class");
  }

  public static final LocalDateTime VALID_TO = LocalDateTime.now().plusYears(1);
  public static final PatientVersionEntity ATHENA_REACT_ANDERSSON_VERSION_ENTITY =
      athenaReactAnderssonVersionEntityBuilder().build();

  public static PatientVersionEntity.PatientVersionEntityBuilder
      athenaReactAnderssonVersionEntityBuilder() {
    return PatientVersionEntity.builder()
        .id(ATHENA_REACT_ANDERSSON_ID_WITHOUT_DASH)
        .protectedPerson(ATHENA_REACT_ANDERSSON_PROTECTED_PERSON.value())
        .testIndicated(ATHENA_REACT_ANDERSSON_TEST_INDICATED.value())
        .deceased(ATHENA_REACT_ANDERSSON_DECEASED.value())
        .type(
            PatientIdTypeEntity.builder()
                .type(PersonEntityIdType.PERSONAL_IDENTITY_NUMBER.name())
                .key(PersonEntityIdType.PERSONAL_IDENTITY_NUMBER.getKey())
                .build())
        .firstName(ATHENA_REACT_ANDERSSON_FIRST_NAME)
        .middleName(ATHENA_REACT_ANDERSSON_MIDDLE_NAME)
        .lastName(ATHENA_REACT_ANDERSSON_LAST_NAME)
        .validTo(VALID_TO)
        .patient(ATHENA_REACT_ANDERSSON_ENTITY);
  }
}
