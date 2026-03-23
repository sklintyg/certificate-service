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

import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.unit.model.IssuingUnit;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidator;

@Value
@Builder
public class ElementValueUnitContactInformation implements ElementValue {

  String address;
  String city;
  String zipCode;
  String phoneNumber;

  @Override
  public boolean isEmpty() {
    return !ElementValidator.isTextDefined(address)
        || !ElementValidator.isTextDefined(city)
        || !ElementValidator.isTextDefined(zipCode)
        || !ElementValidator.isTextDefined(phoneNumber);
  }

  public ElementValueUnitContactInformation copy(IssuingUnit issuingUnit) {
    return ElementValueUnitContactInformation.builder()
        .address(
            ElementValidator.isTextDefined(issuingUnit.address().address())
                ? issuingUnit.address().address()
                : this.address)
        .city(
            ElementValidator.isTextDefined(issuingUnit.address().city())
                ? issuingUnit.address().city()
                : this.city)
        .zipCode(
            ElementValidator.isTextDefined(issuingUnit.address().zipCode())
                ? issuingUnit.address().zipCode()
                : this.zipCode)
        .phoneNumber(
            ElementValidator.isTextDefined(issuingUnit.contactInfo().phoneNumber())
                ? issuingUnit.contactInfo().phoneNumber()
                : this.phoneNumber)
        .build();
  }
}
