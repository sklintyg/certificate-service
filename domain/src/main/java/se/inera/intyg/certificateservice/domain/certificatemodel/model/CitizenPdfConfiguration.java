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
package se.inera.intyg.certificateservice.domain.certificatemodel.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValue;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;

@Value
@Builder
public class CitizenPdfConfiguration implements PdfConfiguration {

  ElementId hiddenBy;
  Predicate<List<ElementData>> shouldHide;
  ElementSimplifiedValue replacementValue;

  public Optional<ElementSimplifiedValue> replacementElementValue(
      PdfGeneratorOptions pdfGeneratorOptions, List<ElementData> allElementData) {
    if (!pdfGeneratorOptions.citizenFormat()) {
      return Optional.empty();
    }

    final var shouldHideByCitizenChoice =
        hiddenBy != null && pdfGeneratorOptions.hiddenElements().contains(hiddenBy);
    final var shouldHideByValue = shouldHide != null && shouldHide.test(allElementData);

    if (shouldHideByCitizenChoice || shouldHideByValue) {
      return Optional.of(replacementValue);
    }

    return Optional.empty();
  }
}
