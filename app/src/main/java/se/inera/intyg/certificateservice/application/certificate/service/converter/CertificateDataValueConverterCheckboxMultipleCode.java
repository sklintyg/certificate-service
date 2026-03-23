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
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValue;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueCode;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueCodeList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCodeList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;

@Component
public class CertificateDataValueConverterCheckboxMultipleCode
    implements CertificateDataValueConverter {

  @Override
  public ElementType getType() {
    return ElementType.CHECKBOX_MULTIPLE_CODE;
  }

  @Override
  public CertificateDataValue convert(
      ElementSpecification elementSpecification, ElementValue elementValue) {
    if (elementValue != null && !(elementValue instanceof ElementValueCodeList)) {
      throw new IllegalStateException(
          "Invalid value type. Type was '%s'".formatted(elementValue.getClass()));
    }

    if (!(elementSpecification.configuration()
        instanceof
        ElementConfigurationCheckboxMultipleCode elementConfigurationCheckboxMultipleCode)) {
      throw new IllegalStateException(
          "Invalid configuration type. Type was '%s'"
              .formatted(elementSpecification.configuration().type()));
    }

    return CertificateDataValueCodeList.builder()
        .id(elementConfigurationCheckboxMultipleCode.id().value())
        .list(
            isValueDefined(elementValue)
                ? ((ElementValueCodeList) elementValue)
                    .list().stream()
                        .map(
                            code ->
                                CertificateDataValueCode.builder()
                                    .id(code.codeId().value())
                                    .code(code.code())
                                    .build())
                        .toList()
                : Collections.emptyList())
        .build();
  }

  private static boolean isValueDefined(ElementValue elementValue) {
    if (elementValue == null) {
      return false;
    }

    final var value = (ElementValueCodeList) elementValue;

    return value.list() != null && !value.list().isEmpty();
  }
}
