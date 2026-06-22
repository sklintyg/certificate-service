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

import java.util.Map;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateList;

@Value
@Builder
public class PdfConfigurationDateList implements PdfConfiguration {

  Map<FieldId, PdfConfigurationDateCheckbox> dateCheckboxes;

  @Override
  public Stream<PdfField> toPdfFields(
      ElementSpecification elementSpec,
      Certificate certificate,
      CustomPdfSpecification pdfSpecification) {
    return elementSpec.valueAs(certificate, ElementValueDateList.class).stream()
        .flatMap(value -> value.dateList().stream())
        .flatMap(this::toPdfFields);
  }

  private Stream<PdfField> toPdfFields(ElementValueDate date) {
    final var checkboxConfig = dateCheckboxes.get(date.dateId());
    if (checkboxConfig == null) {
      throw new IllegalArgumentException("No checkbox found for date: " + date.dateId());
    }

    return checkboxConfig.toPdfFields(date);
  }
}
