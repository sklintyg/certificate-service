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

import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.config.CertificateDataConfig;
import se.inera.intyg.certificateservice.application.certificate.dto.config.CertificateDataConfigInteger;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationInteger;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;

@Component
public class CertificateDataIntegerConfigConverter implements CertificateDataConfigConverter {

  @Override
  public ElementType getType() {
    return ElementType.INTEGER;
  }

  public CertificateDataConfig convert(
      ElementSpecification elementSpecification, Certificate certificate) {
    if (!(elementSpecification.configuration()
        instanceof ElementConfigurationInteger configurationInteger)) {
      throw new IllegalStateException(
          "Invalid config type. Type was '%s'"
              .formatted(elementSpecification.configuration().type()));
    }

    return CertificateDataConfigInteger.builder()
        .id(configurationInteger.id().value())
        .description(configurationInteger.description())
        .text(elementSpecification.configuration().name())
        .header(configurationInteger.header())
        .min(configurationInteger.min())
        .max(configurationInteger.max())
        .unitOfMeasurement(configurationInteger.unitOfMeasurement())
        .build();
  }
}
