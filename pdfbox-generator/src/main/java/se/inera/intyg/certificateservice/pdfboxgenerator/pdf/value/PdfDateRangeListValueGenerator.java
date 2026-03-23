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
package se.inera.intyg.certificateservice.pdfboxgenerator.pdf.value;

import static se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfConstants.CHECKED_BOX_VALUE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.DateRange;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateRangeList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDateRangeCheckbox;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDateRangeList;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

@Component
public class PdfDateRangeListValueGenerator implements PdfElementValue<ElementValueDateRangeList> {

  @Override
  public Class<ElementValueDateRangeList> getType() {
    return ElementValueDateRangeList.class;
  }

  @Override
  public List<PdfField> generate(
      ElementSpecification elementSpecification, ElementValueDateRangeList elementValue) {
    final var pdfConfiguration =
        (PdfConfigurationDateRangeList) elementSpecification.pdfConfiguration();

    return elementValue.dateRangeList().stream()
        .map(dateRange -> getPeriodFields(dateRange, pdfConfiguration.dateRanges()))
        .flatMap(Collection::stream)
        .toList();
  }

  private List<PdfField> getPeriodFields(
      DateRange dateRange, Map<FieldId, PdfConfigurationDateRangeCheckbox> dateRanges) {
    final var fields = new ArrayList<PdfField>();
    final var dateRangeConfig = dateRanges.get(dateRange.dateRangeId());

    fields.add(
        PdfField.builder().id(dateRangeConfig.checkbox().id()).value(CHECKED_BOX_VALUE).build());

    if (dateRange.from() != null) {
      fields.add(
          PdfField.builder()
              .id(dateRangeConfig.from().id())
              .value(dateRange.from().toString())
              .build());
    }

    if (dateRange.to() != null) {
      fields.add(
          PdfField.builder()
              .id(dateRangeConfig.to().id())
              .value(dateRange.to().toString())
              .build());
    }

    return fields;
  }
}
