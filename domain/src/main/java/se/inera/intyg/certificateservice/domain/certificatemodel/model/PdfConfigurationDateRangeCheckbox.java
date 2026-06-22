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

import static se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFormConstants.CHECKED_BOX_VALUE;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.DateRange;

@Value
@Builder
public class PdfConfigurationDateRangeCheckbox implements PdfConfiguration {

  PdfFieldId from;
  PdfFieldId to;
  PdfFieldId checkbox;

  public Stream<PdfField> toPdfFields(DateRange dateRange) {
    return Stream.of(
            Optional.of(checkboxField()),
            dateField(from, dateRange.from()),
            dateField(to, dateRange.to()))
        .flatMap(Optional::stream);
  }

  private Optional<PdfField> dateField(PdfFieldId fieldId, LocalDate date) {
    return Optional.ofNullable(date).map(value -> field(fieldId, value.toString()));
  }

  private PdfField checkboxField() {
    return field(checkbox, CHECKED_BOX_VALUE);
  }

  private PdfField field(PdfFieldId fieldId, String value) {
    return PdfField.builder().fieldId(fieldId).value(value).build();
  }
}
