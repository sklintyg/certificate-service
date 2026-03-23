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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDateList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDateList;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

@Component
public class PdfDateListValueGenerator implements PdfElementValue<ElementValueDateList> {

  @Override
  public Class<ElementValueDateList> getType() {
    return ElementValueDateList.class;
  }

  @Override
  public List<PdfField> generate(
      ElementSpecification elementSpecification, ElementValueDateList elementValueDateList) {
    final var pdfConfiguration = (PdfConfigurationDateList) elementSpecification.pdfConfiguration();
    return elementValueDateList.dateList().stream()
        .map(date -> getFields(date, pdfConfiguration))
        .flatMap(Collection::stream)
        .toList();
  }

  private List<PdfField> getFields(ElementValueDate date, PdfConfigurationDateList configuration) {
    if (date.date() == null) {
      return Collections.emptyList();
    }

    final var pdfConfigurationDateCheckbox = configuration.dateCheckboxes().get(date.dateId());
    if (pdfConfigurationDateCheckbox == null) {
      throw new IllegalArgumentException("No checkbox found for date: " + date.dateId());
    }

    final var checkboxId = pdfConfigurationDateCheckbox.checkboxFieldId();
    final var dateId = pdfConfigurationDateCheckbox.dateFieldId();

    return List.of(
        PdfField.builder().id(checkboxId.id()).value(CHECKED_BOX_VALUE).build(),
        PdfField.builder().id(dateId.id()).value(date.date().toString()).build());
  }
}
