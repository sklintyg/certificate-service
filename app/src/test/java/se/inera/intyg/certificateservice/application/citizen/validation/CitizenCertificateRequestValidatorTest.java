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
package se.inera.intyg.certificateservice.application.citizen.validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.common.dto.PersonIdDTO;
import se.inera.intyg.certificateservice.application.common.dto.PersonIdTypeDTO;

class CitizenCertificateRequestValidatorTest {

  private static final CitizenCertificateRequestValidator validator =
      new CitizenCertificateRequestValidator();
  private static final String CERTIFICATE_ID = "certificateId";
  private static final String PERSON_ID = "191212121212";

  @Nested
  class CertificateId {

    @Test
    void shouldThrowIfCertificateIdIsNull() {
      final var personId = PersonIdDTO.builder().id(PERSON_ID).build();

      assertThrows(IllegalArgumentException.class, () -> validator.validate(null, personId));
    }

    @Test
    void shouldThrowIfCertificateIdIsEmpty() {
      assertThrows(IllegalArgumentException.class, () -> validator.validate(""));
    }

    @Test
    void shouldThrowIfCertificateIdIsBlank() {
      assertThrows(IllegalArgumentException.class, () -> validator.validate("   "));
    }

    @Test
    void shouldNotThrowIfAllConditionsAreMet() {
      assertDoesNotThrow(() -> validator.validate(CERTIFICATE_ID));
    }
  }

  @Nested
  class PersonIdAndCertificateId {

    @Test
    void shouldThrowIfPersonIdIsNull() {
      assertThrows(IllegalArgumentException.class, () -> validator.validate(CERTIFICATE_ID, null));
    }

    @Test
    void shouldThrowIfPersonIdIdIsNull() {
      final var personId =
          PersonIdDTO.builder().type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER).build();

      assertThrows(
          IllegalArgumentException.class, () -> validator.validate(CERTIFICATE_ID, personId));
    }

    @Test
    void shouldThrowIfPersonIdIdIsEmpty() {
      final var personId =
          PersonIdDTO.builder().id("").type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER).build();

      assertThrows(
          IllegalArgumentException.class, () -> validator.validate(CERTIFICATE_ID, personId));
    }

    @Test
    void shouldThrowIfPersonIdTypeIsNull() {
      final var personId = PersonIdDTO.builder().id(PERSON_ID).build();

      assertThrows(
          IllegalArgumentException.class, () -> validator.validate(CERTIFICATE_ID, personId));
    }

    @Test
    void shouldNotThrowIfAllConditionsAreMet() {
      final var personId =
          PersonIdDTO.builder()
              .id(PERSON_ID)
              .type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER)
              .build();

      assertDoesNotThrow(() -> validator.validate(CERTIFICATE_ID, personId));
    }
  }

  @Nested
  class PersonId {

    @Test
    void shouldThrowIfPersonIdIdIsNull() {
      final var personId =
          PersonIdDTO.builder().type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER).build();

      assertThrows(IllegalArgumentException.class, () -> validator.validate(personId));
    }

    @Test
    void shouldThrowIfPersonIdIdIsEmpty() {
      final var personId =
          PersonIdDTO.builder().id("").type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER).build();

      assertThrows(IllegalArgumentException.class, () -> validator.validate(personId));
    }

    @Test
    void shouldThrowIfPersonIdTypeIsNull() {
      final var personId = PersonIdDTO.builder().id(PERSON_ID).build();

      assertThrows(IllegalArgumentException.class, () -> validator.validate(personId));
    }

    @Test
    void shouldNotThrowIfAllConditionsAreMet() {
      final var personId =
          PersonIdDTO.builder()
              .id(PERSON_ID)
              .type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER)
              .build();

      assertDoesNotThrow(() -> validator.validate(personId));
    }
  }
}
