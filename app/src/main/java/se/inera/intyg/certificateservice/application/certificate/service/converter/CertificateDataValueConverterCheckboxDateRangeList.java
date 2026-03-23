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
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDateRange;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDateRangeList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRangeList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxDateRangeList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;

@Component
public class CertificateDataValueConverterCheckboxDateRangeList
    implements CertificateDataValueConverter {

  @Override
  public ElementType getType() {
    return ElementType.CHECKBOX_DATE_RANGE_LIST;
  }

  @Override
  public CertificateDataValue convert(
      ElementSpecification elementSpecification, ElementValue elementValue) {
    if (elementValue != null && !(elementValue instanceof ElementValueDateRangeList)) {
      throw new IllegalStateException(
          "Invalid value type. Type was '%s'".formatted(elementValue.getClass()));
    }

    if (!(elementSpecification.configuration()
        instanceof
        ElementConfigurationCheckboxDateRangeList elementConfigurationCheckboxDateRangeList)) {
      throw new IllegalStateException(
          "Invalid configuration type. Type was '%s'"
              .formatted(elementSpecification.configuration().type()));
    }

    return CertificateDataValueDateRangeList.builder()
        .id(elementConfigurationCheckboxDateRangeList.id().value())
        .list(
            isValueDefined(elementValue)
                ? ((ElementValueDateRangeList) elementValue)
                    .dateRangeList().stream()
                        .map(
                            dateRange ->
                                CertificateDataValueDateRange.builder()
                                    .id(dateRange.dateRangeId().value())
                                    .from(dateRange.from())
                                    .to(dateRange.to())
                                    .build())
                        .toList()
                : Collections.emptyList())
        .build();
  }

  private static boolean isValueDefined(ElementValue elementValue) {
    if (elementValue == null) {
      return false;
    }

    final var value = (ElementValueDateRangeList) elementValue;

    return value.dateRangeList() != null && !value.dateRangeList().isEmpty();
  }
}
