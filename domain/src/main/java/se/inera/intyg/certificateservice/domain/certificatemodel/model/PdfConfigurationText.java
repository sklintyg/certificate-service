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
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;

@Value
@Builder
public class PdfConfigurationText implements PdfConfiguration {

  PdfFieldId pdfFieldId;
  Integer maxLength;
  PdfFieldId overflowSheetFieldId;
  Integer offset;

  @Override
  public List<PdfField> toPdfFields(ElementSpecification elementSpec, Certificate certificate) {
    return certificate.getElementDataById(elementSpec.id()).map(ElementData::value).stream()
        .flatMap(this::toTextField)
        .toList();
  }

  private Stream<PdfField> toTextField(ElementValue value) {
    if (value.asString() == null) {
      return Stream.empty();
    }

    return Stream.of(
        PdfField.builder().fieldId(pdfFieldId).value(value.asString()).offset(offset).build());
  }
}
