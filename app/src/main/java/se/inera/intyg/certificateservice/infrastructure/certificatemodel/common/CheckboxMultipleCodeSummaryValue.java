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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.common;

import java.util.stream.Collectors;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCodeList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

public class CheckboxMultipleCodeSummaryValue {

  private CheckboxMultipleCodeSummaryValue() {
    throw new IllegalStateException("Utility class");
  }

  public static String value(ElementId elementId, Certificate certificate) {
    final var elementDataCode = certificate.getElementDataById(elementId);
    final var elementSpecification = certificate.certificateModel().elementSpecification(elementId);

    if (elementDataCode.isEmpty()) {
      return "";
    }

    if (!(elementDataCode.get().value() instanceof ElementValueCodeList elementValueCodeList)) {
      throw new IllegalStateException(
          "Invalid value type. Type was '%s'".formatted(elementDataCode.get().value()));
    }

    if (!(elementSpecification.configuration()
        instanceof ElementConfigurationCheckboxMultipleCode configuration)) {
      throw new IllegalStateException(
          "Invalid configuration type. Type was '%s'"
              .formatted(elementSpecification.configuration().getClass()));
    }

    return elementValueCodeList.list().stream()
        .map(value -> configuration.code(value).displayName())
        .collect(Collectors.joining(", "));
  }
}
