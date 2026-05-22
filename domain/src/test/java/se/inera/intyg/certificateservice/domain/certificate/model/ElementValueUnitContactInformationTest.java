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
package se.inera.intyg.certificateservice.domain.certificate.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataElementData.contactInfoElementValueBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataSubUnit.alfaAllergimottagningenBuilder;

import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.unit.model.UnitAddress;
import se.inera.intyg.certificateservice.domain.unit.model.UnitContactInfo;

class ElementValueUnitContactInformationTest {

  private static final CharsetEncoder ISO_8859_1_ENCODER = StandardCharsets.ISO_8859_1.newEncoder();
  private static final String INVALID_CHAR = "\u0400";

  @Nested
  class Encoding {

    @Test
    void shouldReturnCanEncodeWhenAllFieldsAreNull() {
      final var result =
          ElementValueUnitContactInformation.builder().build().encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCanEncodeWhenAllFieldsAreValid() {
      final var result =
          ElementValueUnitContactInformation.builder()
              .address("Main Street 1")
              .city("Stockholm")
              .zipCode("12345")
              .phoneNumber("0701234567")
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertTrue(result.canEncode());
      assertTrue(result.invalidChars().isEmpty());
    }

    @Test
    void shouldReturnCannotEncodeWhenAddressHasInvalidChars() {
      final var result =
          ElementValueUnitContactInformation.builder()
              .address(INVALID_CHAR)
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertFalse(result.canEncode());
      assertEquals(List.of(INVALID_CHAR), result.invalidChars());
    }

    @Test
    void shouldReturnCannotEncodeWhenCityHasInvalidChars() {
      final var result =
          ElementValueUnitContactInformation.builder()
              .city(INVALID_CHAR)
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertFalse(result.canEncode());
      assertEquals(List.of(INVALID_CHAR), result.invalidChars());
    }

    @Test
    void shouldReturnCannotEncodeWhenZipCodeHasInvalidChars() {
      final var result =
          ElementValueUnitContactInformation.builder()
              .zipCode(INVALID_CHAR)
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertFalse(result.canEncode());
      assertEquals(List.of(INVALID_CHAR), result.invalidChars());
    }

    @Test
    void shouldReturnCannotEncodeWhenPhoneNumberHasInvalidChars() {
      final var result =
          ElementValueUnitContactInformation.builder()
              .phoneNumber(INVALID_CHAR)
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertFalse(result.canEncode());
      assertEquals(List.of(INVALID_CHAR), result.invalidChars());
    }

    @Test
    void shouldReturnDistinctInvalidCharsAcrossAllFields() {
      final var result =
          ElementValueUnitContactInformation.builder()
              .address(INVALID_CHAR)
              .city(INVALID_CHAR)
              .zipCode(INVALID_CHAR)
              .phoneNumber(INVALID_CHAR)
              .build()
              .encoding(ISO_8859_1_ENCODER);
      assertFalse(result.canEncode());
      assertEquals(1, result.invalidChars().size());
    }
  }

  @Nested
  class IsEmpty {

    @Test
    void shouldReturnTrueIfNull() {
      assertTrue(ElementValueUnitContactInformation.builder().build().isEmpty());
    }

    @Test
    void shouldReturnFalseIfUnitContactInformationIsDefined() {
      assertFalse(
          ElementValueUnitContactInformation.builder()
              .address("Address")
              .city("City")
              .zipCode("ZipCode")
              .phoneNumber("PhoneNumber")
              .build()
              .isEmpty());
    }

    @Test
    void shouldReturnTrueIfOneValueIsEmpty() {
      assertTrue(
          ElementValueUnitContactInformation.builder()
              .address("Address")
              .city("City")
              .zipCode("ZipCode")
              .build()
              .isEmpty());
    }
  }

  @Nested
  class CopyBasedOnIssuedUnitTest {

    @Test
    void shouldReturnAddressIfIssuedUnitAddressNull() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder().address(UnitAddress.builder().build()).build();

      final var actual = original.copy(issuingUnit);
      assertEquals(original.address(), actual.address());
    }

    @Test
    void shouldReturnAddressIfIssuedUnitAddressEmpty() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder()
              .address(UnitAddress.builder().address("").build())
              .build();

      final var actual = original.copy(issuingUnit);
      assertEquals(original.address(), actual.address());
    }

    @Test
    void shouldReturnIssuedUnitAddressIfIssuedUnitAddressExists() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder()
              .address(UnitAddress.builder().address("New Address").build())
              .build();

      final var actual = original.copy(issuingUnit);
      assertEquals("New Address", actual.address());
    }

    @Test
    void shouldReturnCityIfIssuedUnitCityNull() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder().address(UnitAddress.builder().build()).build();

      final var actual = original.copy(issuingUnit);
      assertEquals(original.city(), actual.city());
    }

    @Test
    void shouldReturnCityIfIssuedUnitCityEmpty() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder().address(UnitAddress.builder().city("").build()).build();

      final var actual = original.copy(issuingUnit);
      assertEquals(original.city(), actual.city());
    }

    @Test
    void shouldReturnIssuedUnitCityIfIssuedUnitCityExists() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder()
              .address(UnitAddress.builder().city("New City").build())
              .build();

      final var actual = original.copy(issuingUnit);
      assertEquals("New City", actual.city());
    }

    @Test
    void shouldReturnZipCodeIfIssuedUnitZipCodeNull() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder().address(UnitAddress.builder().build()).build();

      final var actual = original.copy(issuingUnit);
      assertEquals(original.zipCode(), actual.zipCode());
    }

    @Test
    void shouldReturnZipCodeIfIssuedUnitZipCodeEmpty() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder()
              .address(UnitAddress.builder().zipCode("").build())
              .build();

      final var actual = original.copy(issuingUnit);
      assertEquals(original.zipCode(), actual.zipCode());
    }

    @Test
    void shouldReturnIssuedUnitZipCodeIfIssuedUnitZipCodeExists() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder()
              .address(UnitAddress.builder().zipCode("New ZipCode").build())
              .build();

      final var actual = original.copy(issuingUnit);
      assertEquals("New ZipCode", actual.zipCode());
    }

    @Test
    void shouldReturnPhoneNumberIfIssuedUnitPhoneNumberNull() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder().contactInfo(UnitContactInfo.builder().build()).build();

      final var actual = original.copy(issuingUnit);
      assertEquals(original.phoneNumber(), actual.phoneNumber());
    }

    @Test
    void shouldReturnPhoneNumberIfIssuedUnitPhoneNumberEmpty() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder()
              .contactInfo(UnitContactInfo.builder().phoneNumber("").build())
              .build();

      final var actual = original.copy(issuingUnit);
      assertEquals(original.phoneNumber(), actual.phoneNumber());
    }

    @Test
    void shouldReturnIssuedUnitPhoneNumberIfIssuedUnitPhoneNumberExists() {
      final var original = contactInfoElementValueBuilder().build();
      final var issuingUnit =
          alfaAllergimottagningenBuilder()
              .contactInfo(UnitContactInfo.builder().phoneNumber("New PhoneNumber").build())
              .build();

      final var actual = original.copy(issuingUnit);
      assertEquals("New PhoneNumber", actual.phoneNumber());
    }
  }
}
