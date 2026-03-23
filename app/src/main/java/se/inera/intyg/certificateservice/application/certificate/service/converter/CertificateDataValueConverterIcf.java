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

import java.util.Collections;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataIcfValue;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueIcf;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationIcf;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;

@Component
public class CertificateDataValueConverterIcf implements CertificateDataValueConverter {

  @Override
  public ElementType getType() {
    return ElementType.ICF;
  }

  @Override
  public CertificateDataValue convert(
      ElementSpecification elementSpecification, ElementValue elementValue) {
    if (elementValue != null && !(elementValue instanceof ElementValueIcf)) {
      throw new IllegalStateException(
          "Invalid value type. Type was '%s'".formatted(elementValue.getClass()));
    }

    if (!(elementSpecification.configuration()
        instanceof ElementConfigurationIcf elementConfiguration)) {
      throw new IllegalStateException(
          "Invalid configuration type. Type was '%s'"
              .formatted(elementSpecification.configuration().type()));
    }

    return CertificateDataIcfValue.builder()
        .id(elementConfiguration.id().value())
        .text(elementValue != null ? ((ElementValueIcf) elementValue).text() : null)
        .icfCodes(
            elementValue != null
                ? ((ElementValueIcf) elementValue).icfCodes()
                : Collections.emptyList())
        .build();
  }
}
