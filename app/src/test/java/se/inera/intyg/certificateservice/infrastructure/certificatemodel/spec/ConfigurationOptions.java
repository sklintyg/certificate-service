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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.spec;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfiguration;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxDateRangeList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDropdownCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioMultipleCode;
import se.inera.intyg.certificateservice.domain.common.model.Code;

/**
 * Reads the selectable codes out of a configuration, whatever shape it stores them in.
 *
 * <p>The specification calls these "Urval" and lists them for five different input components — not
 * only the plainly code-valued ones, but also the date and date-range checkboxes, where each
 * checkbox is itself a code.
 */
public class ConfigurationOptions {

  private ConfigurationOptions() {
    throw new IllegalStateException("Utility class");
  }

  /** The codes in the order the configuration declares them. */
  public static List<Code> of(ElementConfiguration configuration) {
    if (configuration == null) {
      return List.of();
    }
    return switch (configuration) {
      case ElementConfigurationCheckboxMultipleCode checkbox ->
          checkbox.list().stream().map(option -> option.code()).toList();
      case ElementConfigurationRadioMultipleCode radio ->
          radio.list().stream().map(option -> option.code()).toList();
      case ElementConfigurationDropdownCode dropdown ->
          dropdown.list().stream().map(option -> option.code()).toList();
      case ElementConfigurationCheckboxMultipleDate dates ->
          dates.dates().stream().map(date -> date.code()).toList();
      case ElementConfigurationCheckboxDateRangeList ranges ->
          ranges.dateRanges().stream().map(option -> option.code()).toList();
      default -> List.of();
    };
  }
}
