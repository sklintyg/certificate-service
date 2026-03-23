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

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.config.CertificateDataConfig;
import se.inera.intyg.certificateservice.application.certificate.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.certificateservice.application.certificate.dto.config.CheckboxMultipleDate;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;

@Component
public class CertificateDataCheckboxDateListConfigConverter
    implements CertificateDataConfigConverter {

  @Override
  public ElementType getType() {
    return ElementType.CHECKBOX_MULTIPLE_DATE;
  }

  public CertificateDataConfig convert(
      ElementSpecification elementSpecification, Certificate certificate) {
    if (!(elementSpecification.configuration()
        instanceof ElementConfigurationCheckboxMultipleDate configuration)) {
      throw new IllegalStateException(
          "Invalid config type. Type was '%s'"
              .formatted(elementSpecification.configuration().type()));
    }
    return CertificateDataConfigCheckboxMultipleDate.builder()
        .text(configuration.name())
        .label(configuration.label())
        .list(
            configuration.dates().stream()
                .map(
                    date ->
                        CheckboxMultipleDate.builder()
                            .id(date.id().value())
                            .label(date.label())
                            .minDate(date(date.min()))
                            .maxDate(date(date.max()))
                            .build())
                .toList())
        .build();
  }

  private static LocalDate date(Period period) {
    return period == null ? null : LocalDate.now(ZoneId.systemDefault()).plus(period);
  }
}
