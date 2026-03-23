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
package se.inera.intyg.certificateservice.application.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateUnitConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;
import se.inera.intyg.certificateservice.domain.unit.model.CareUnit;
import se.inera.intyg.certificateservice.domain.unit.model.Inactive;
import se.inera.intyg.certificateservice.domain.unit.model.UnitAddress;
import se.inera.intyg.certificateservice.domain.unit.model.UnitContactInfo;
import se.inera.intyg.certificateservice.domain.unit.model.UnitName;

class CertificateUnitConverterTest {

  private static final String EXPECTED_UNIT_NAME = "expectedUnitName";
  private static final String EXPECTED_ADDRESS = "expectedAddress";
  private static final String EXPECTED_CITY = "expectedCity";
  private static final String EXPECTED_ZIP_CODE = "expectedZipCode";
  private static final String EXPECTED_PHONE_NUMBER = "expectedPhoneNumber";
  private static final String EXPECTED_EMAIL = "expectedEmail";
  private CertificateUnitConverter certificateUnitConverter;
  private static final String ISSUING_UNIT = "ISSUING_UNIT";
  private static final String EXPECTED_ID = "expectedId";
  private CareUnit.CareUnitBuilder careUnitBuilder;

  @BeforeEach
  void setUp() {
    certificateUnitConverter = new CertificateUnitConverter();
    careUnitBuilder =
        CareUnit.builder()
            .name(new UnitName("name"))
            .address(
                UnitAddress.builder().address("address").city("city").zipCode("zipCode").build())
            .hsaId(new HsaId("hsaId"))
            .contactInfo(
                UnitContactInfo.builder().phoneNumber("phoneNumber").email("email").build())
            .inactive(new Inactive(false));
  }

  @Test
  void shallConvertUnitId() {
    final var issuingUnit = careUnitBuilder.hsaId(new HsaId(EXPECTED_ID)).build();
    final var result = certificateUnitConverter.convert(issuingUnit, Optional.empty());
    assertEquals(EXPECTED_ID, result.getUnitId());
  }

  @Test
  void shallConvertName() {
    final var issuingUnit = careUnitBuilder.name(new UnitName(EXPECTED_UNIT_NAME)).build();
    final var result = certificateUnitConverter.convert(issuingUnit, Optional.empty());
    assertEquals(EXPECTED_UNIT_NAME, result.getUnitName());
  }

  @Test
  void shallConvertAddressFromIssuingUnit() {
    final var issuingUnit =
        careUnitBuilder
            .address(
                UnitAddress.builder()
                    .address(EXPECTED_ADDRESS)
                    .city("city")
                    .zipCode("zipCode")
                    .build())
            .build();
    final var result = certificateUnitConverter.convert(issuingUnit, Optional.empty());
    assertEquals(EXPECTED_ADDRESS, result.getAddress());
  }

  @Test
  void shallConvertAddressCityFromElementData() {
    final var issuingUnit = careUnitBuilder.build();
    final var elementData =
        Optional.of(getElementData(ISSUING_UNIT, EXPECTED_ADDRESS, null, null, null));
    final var result = certificateUnitConverter.convert(issuingUnit, elementData);
    assertEquals(EXPECTED_ADDRESS, result.getAddress());
  }

  @Test
  void shallConvertCityFromIssuingUnit() {
    final var issuingUnit =
        careUnitBuilder
            .address(
                UnitAddress.builder()
                    .address("address")
                    .city(EXPECTED_CITY)
                    .zipCode("zipCode")
                    .build())
            .build();
    final var result = certificateUnitConverter.convert(issuingUnit, Optional.empty());
    assertEquals(EXPECTED_CITY, result.getCity());
  }

  @Test
  void shallConvertCityFromElementData() {
    final var issuingUnit = careUnitBuilder.build();
    final var elementData =
        Optional.of(getElementData(ISSUING_UNIT, null, EXPECTED_CITY, null, null));
    final var result = certificateUnitConverter.convert(issuingUnit, elementData);
    assertEquals(EXPECTED_CITY, result.getCity());
  }

  @Test
  void shallConvertZipcodeFromIssuingUnit() {
    final var issuingUnit =
        careUnitBuilder
            .address(
                UnitAddress.builder()
                    .address("address")
                    .city("city")
                    .zipCode(EXPECTED_ZIP_CODE)
                    .build())
            .build();
    final var result = certificateUnitConverter.convert(issuingUnit, Optional.empty());
    assertEquals(EXPECTED_ZIP_CODE, result.getZipCode());
  }

  @Test
  void shallConvertZipcodeFromElementData() {
    final var issuingUnit = careUnitBuilder.build();
    final var elementData =
        Optional.of(getElementData(ISSUING_UNIT, null, null, EXPECTED_ZIP_CODE, null));
    final var result = certificateUnitConverter.convert(issuingUnit, elementData);
    assertEquals(EXPECTED_ZIP_CODE, result.getZipCode());
  }

  @Test
  void shallConvertPhoneNumberFromIssuingUnit() {
    final var issuingUnit =
        careUnitBuilder
            .contactInfo(UnitContactInfo.builder().phoneNumber(EXPECTED_PHONE_NUMBER).build())
            .build();
    final var result = certificateUnitConverter.convert(issuingUnit, Optional.empty());
    assertEquals(EXPECTED_PHONE_NUMBER, result.getPhoneNumber());
  }

  @Test
  void shallConvertPhoneNumberFromElementData() {
    final var issuingUnit = careUnitBuilder.build();
    final var elementData =
        Optional.of(getElementData(ISSUING_UNIT, null, null, null, EXPECTED_PHONE_NUMBER));
    final var result = certificateUnitConverter.convert(issuingUnit, elementData);
    assertEquals(EXPECTED_PHONE_NUMBER, result.getPhoneNumber());
  }

  @Test
  void shallConvertEmailFromIssuingUnit() {
    final var issuingUnit =
        careUnitBuilder
            .contactInfo(
                UnitContactInfo.builder().phoneNumber("phoneNumber").email(EXPECTED_EMAIL).build())
            .build();
    final var result = certificateUnitConverter.convert(issuingUnit, Optional.empty());
    assertEquals(EXPECTED_EMAIL, result.getEmail());
  }

  @Test
  void shallConvertIsInactiveFromIssuingUnit() {
    final var issuingUnit = careUnitBuilder.inactive(new Inactive(true)).build();
    final var result = certificateUnitConverter.convert(issuingUnit, Optional.empty());
    assertTrue(result.getIsInactive());
  }

  private static ElementData getElementData(
      String id, String address, String city, String zipcode, String phoneNumber) {
    return ElementData.builder()
        .id(new ElementId(id))
        .value(
            ElementValueUnitContactInformation.builder()
                .address(address)
                .city(city)
                .zipCode(zipcode)
                .phoneNumber(phoneNumber)
                .build())
        .build();
  }
}
