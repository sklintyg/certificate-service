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

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDate;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

@Component
public class PdfDateValueGenerator implements PdfElementValue<ElementValueDate> {

  @Override
  public Class<ElementValueDate> getType() {
    return ElementValueDate.class;
  }

  @Override
  public List<PdfField> generate(
      ElementSpecification elementSpecification, ElementValueDate elementValueDate) {
    if (elementValueDate.date() == null) {
      return Collections.emptyList();
    }

    final var pdfConfiguration = (PdfConfigurationDate) elementSpecification.pdfConfiguration();
    return List.of(
        PdfField.builder()
            .id(pdfConfiguration.pdfFieldId().id())
            .value(elementValueDate.date().toString())
            .build());
  }
}
