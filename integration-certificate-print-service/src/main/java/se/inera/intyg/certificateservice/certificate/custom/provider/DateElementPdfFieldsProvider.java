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
package se.inera.intyg.certificateservice.certificate.custom.provider;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPdfFieldDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDate;

@Component
public class DateElementPdfFieldsProvider implements PdfFieldsProvider {

  @Override
  public Map<String, CustomPdfFieldDTO> fields(
      Certificate certificate, CustomPdfSpecification spec) {
    return certificate.certificateModel().elementSpecifications().stream()
        .flatMap(ElementSpecification::flatten)
        .filter(es -> es.pdfConfiguration() instanceof PdfConfigurationDate)
        .flatMap(
            elementSpec ->
                elementSpec
                    .valueAs(certificate, ElementValueDate.class)
                    .map(getElementValueDateEntry(elementSpec))
                    .stream())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  private static Function<ElementValueDate, Entry<String, CustomPdfFieldDTO>>
      getElementValueDateEntry(ElementSpecification elementSpec) {
    return elementValue -> {
      var config = (PdfConfigurationDate) elementSpec.pdfConfiguration();
      return Map.entry(
          config.pdfFieldId().id(), new CustomPdfFieldDTO(elementValue.date().toString()));
    };
  }
}
