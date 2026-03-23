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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import java.util.Optional;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.UnitDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.unit.model.IssuingUnit;

@Component
public class CertificateUnitConverter {

  public UnitDTO convert(IssuingUnit issuingUnit, Optional<ElementData> elementData) {
    return elementData
        .map(
            data -> {
              final var elementValue = (ElementValueUnitContactInformation) data.value();
              return UnitDTO.builder()
                  .unitId(issuingUnit.hsaId().id())
                  .unitName(issuingUnit.name().name())
                  .address(elementValue.address())
                  .city(elementValue.city())
                  .zipCode(elementValue.zipCode())
                  .phoneNumber(elementValue.phoneNumber())
                  .email(
                      issuingUnit.contactInfo() == null ? null : issuingUnit.contactInfo().email())
                  .isInactive(
                      issuingUnit.inactive() == null
                          ? Boolean.FALSE
                          : issuingUnit.inactive().value())
                  .build();
            })
        .orElse(
            UnitDTO.builder()
                .unitId(issuingUnit.hsaId().id())
                .unitName(issuingUnit.name().name())
                .address(issuingUnit.address() == null ? null : issuingUnit.address().address())
                .city(issuingUnit.address() == null ? null : issuingUnit.address().city())
                .zipCode(issuingUnit.address() == null ? null : issuingUnit.address().zipCode())
                .phoneNumber(
                    issuingUnit.contactInfo() == null
                        ? null
                        : issuingUnit.contactInfo().phoneNumber())
                .email(issuingUnit.contactInfo() == null ? null : issuingUnit.contactInfo().email())
                .isInactive(
                    issuingUnit.inactive() == null ? Boolean.FALSE : issuingUnit.inactive().value())
                .build());
  }
}
