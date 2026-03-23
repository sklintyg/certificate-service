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

import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationIcf;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidator;

@Value
@Builder
public class ElementValueIcf implements ElementValue {

  FieldId id;
  @With String text;
  @With @Builder.Default List<String> icfCodes = Collections.emptyList();

  @Override
  public boolean isEmpty() {
    return !ElementValidator.isTextDefined(text);
  }

  public String formatIcfValueText(ElementConfigurationIcf elementConfigurationIcf) {
    if (icfCodes.isEmpty()) {
      return text;
    }

    return """
        %s %s

        %s
        """
        .formatted(elementConfigurationIcf.collectionsLabel(), String.join(" - ", icfCodes), text);
  }
}
