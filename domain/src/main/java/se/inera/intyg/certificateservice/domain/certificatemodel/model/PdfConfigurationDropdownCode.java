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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;

@Value
@Builder
public class PdfConfigurationDropdownCode implements PdfConfiguration {

  PdfFieldId fieldId;
  Map<FieldId, String> codes;

  public static Map<FieldId, String> fromCodeConfig(List<ElementConfigurationCode> dropdownItems) {
    return dropdownItems.stream()
        .collect(Collectors.toMap(ElementConfigurationCode::id, ElementConfigurationCode::label));
  }

  @Override
  public Stream<PdfField> toPdfFields(
      ElementSpecification elementSpec,
      Certificate certificate,
      CustomPdfSpecification pdfSpecification) {
    return elementSpec
        .valueAs(certificate, ElementValueCode.class)
        .flatMap(this::toPdfField)
        .stream();
  }

  private Optional<PdfField> toPdfField(ElementValueCode code) {
    if (codeIsInvalid(code)) {
      return Optional.empty();
    }

    final var value = codes.get(code.codeId());

    if (value == null) {
      throw new IllegalArgumentException(
          "PDF value for dropdown code '%s' was not configured".formatted(code.codeId()));
    }

    return Optional.of(PdfField.builder().fieldId(fieldId).value(value).build());
  }

  private static boolean codeIsInvalid(ElementValueCode code) {
    return code == null || code.isEmpty() || code.codeId() == null || code.codeId().value() == null;
  }
}
