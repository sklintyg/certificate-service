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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCodeList;

@Value
@Builder
public class PdfConfigurationCode implements PdfConfiguration {

  Map<FieldId, PdfFieldId> codes;

  @Override
  public List<PdfField> toPdfFields(ElementSpecification elementSpec, Certificate certificate) {
    return certificate.getElementDataById(elementSpec.id()).map(ElementData::value).stream()
        .flatMap(this::toPdfFields)
        .toList();
  }

  private Stream<PdfField> toPdfFields(ElementValue value) {
    return switch (value) {
      case ElementValueCode code -> toPdfField(code).stream();
      case ElementValueCodeList codeList ->
          Optional.ofNullable(codeList.list()).orElse(List.of()).stream()
              .map(this::toPdfField)
              .flatMap(Optional::stream);
      default -> Stream.empty();
    };
  }

  private Optional<PdfField> toPdfField(ElementValueCode code) {
    if (codeIsInvalid(code)) {
      return Optional.empty();
    }

    final var fieldId = codes.get(code.codeId());

    if (fieldId == null) {
      throw new IllegalArgumentException(
          "PDF field id for code '%s' was not configured".formatted(code.codeId()));
    }

    return Optional.of(PdfField.builder().fieldId(fieldId).value(CHECKED_BOX_VALUE).build());
  }

  private static boolean codeIsInvalid(ElementValueCode code) {
    return code == null || code.isEmpty() || code.codeId() == null || code.codeId().value() == null;
  }
}
