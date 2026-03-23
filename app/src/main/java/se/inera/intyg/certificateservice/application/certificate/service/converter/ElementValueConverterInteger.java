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

import static se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType.INTEGER;

import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValue;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueInteger;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueInteger;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@Component
public class ElementValueConverterInteger implements ElementValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return INTEGER;
  }

  @Override
  public ElementValue convert(CertificateDataValue value) {
    if (!(value instanceof CertificateDataValueInteger valueInteger)) {
      throw new IllegalStateException(
          "Invalid value type. Type was '%s'".formatted(value.getType()));
    }
    return ElementValueInteger.builder()
        .integerId(new FieldId(valueInteger.getId()))
        .value(valueInteger.getValue())
        .unitOfMeasurement(valueInteger.getUnitOfMeasurement())
        .build();
  }
}
