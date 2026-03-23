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

import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosis;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosisList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

public class DiagnosisSummaryValue {

  private DiagnosisSummaryValue() {
    throw new IllegalStateException("Utility class");
  }

  public static String value(ElementId elementId, FieldId fieldId, Certificate certificate) {
    final var elementDataDiagnosis =
        certificate.elementData().stream()
            .filter(elementData -> elementId.equals(elementData.id()))
            .findFirst();

    if (elementDataDiagnosis.isEmpty()) {
      return "";
    }

    if (!(elementDataDiagnosis.get().value()
        instanceof ElementValueDiagnosisList elementValueDiagnosisList)) {
      throw new IllegalStateException(
          "Invalid value type. Type was '%s'".formatted(elementDataDiagnosis.get().value()));
    }

    return elementValueDiagnosisList.diagnoses().stream()
        .filter(elementValueDiagnosis -> elementValueDiagnosis.id().equals(fieldId))
        .findFirst()
        .map(ElementValueDiagnosis::description)
        .orElse("");
  }
}
