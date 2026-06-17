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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPdfFieldDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfField;

@Component
public class ElementPdfFieldsProvider implements PdfFieldsProvider {

  @Override
  public Map<String, CustomPdfFieldDTO> fields(
      Certificate certificate, CustomPdfSpecification spec) {
    return certificate.certificateModel().elementSpecifications().stream()
        .flatMap(ElementSpecification::flatten)
        .flatMap(elementSpec -> getPdfFields(certificate, elementSpec))
        .collect(
            Collectors.toMap(
                field -> field.fieldId().id(),
                field -> new CustomPdfFieldDTO(field.value(), field.offset(), field.appearance()),
                (a, b) -> {
                  throw new IllegalStateException(
                      "Duplicate PDF field id detected, two pdf configurations produced the same key");
                }));
  }

  private static Stream<PdfField> getPdfFields(
      Certificate certificate, ElementSpecification elementSpec) {
    return Optional.ofNullable(elementSpec.pdfConfiguration()).stream()
        .flatMap(config -> config.toPdfFields(elementSpec, certificate));
  }
}
