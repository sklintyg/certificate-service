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
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosis;

@Value
@Builder
public class PdfConfigurationDiagnosis implements PdfConfiguration {

  PdfFieldId pdfNameFieldId;
  List<PdfFieldId> pdfCodeFieldIds;

  public Stream<PdfField> toPdfFields(
      ElementValueDiagnosis diagnosis,
      String appearance,
      Integer maxLength,
      OverflowConfig overflowConfig) {
    return Stream.concat(
        Stream.of(nameField(diagnosis, appearance, maxLength, overflowConfig)),
        codeFields(diagnosis));
  }

  private PdfField nameField(
      ElementValueDiagnosis diagnosis,
      String appearance,
      Integer maxLength,
      OverflowConfig overflowConfig) {
    return PdfField.builder()
        .fieldId(pdfNameFieldId)
        .value(buildValueWithinMaxLenght(diagnosis, maxLength))
        .appearance(appearance)
        .shouldRemoveLineBreaks(true)
        .overflowConfig(overflowConfig)
        .build();
  }

  private static String buildValueWithinMaxLenght(
      ElementValueDiagnosis diagnosis, Integer maxLength) {
    return diagnosis.description().length() < maxLength
        ? diagnosis.description()
        : diagnosis.description().substring(0, maxLength - 3) + "...";
  }

  private Stream<PdfField> codeFields(ElementValueDiagnosis diagnosis) {
    if (diagnosis.code() == null) {
      return Stream.empty();
    }

    final var code = diagnosis.code();

    if (code.length() > pdfCodeFieldIds.size()) {
      throw new IllegalArgumentException(
          "Diagnosis code '%s' has more characters than configured PDF fields".formatted(code));
    }

    return IntStream.range(0, code.length())
        .mapToObj(index -> codeField(index, code.charAt(index)));
  }

  private PdfField codeField(int index, char value) {
    return PdfField.builder()
        .fieldId(pdfCodeFieldIds.get(index))
        .value(String.valueOf(value))
        .build();
  }
}
