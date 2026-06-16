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
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;

@Value
@Builder
public class PdfConfigurationBoolean implements PdfConfiguration {

  PdfFieldId checkboxTrue;
  PdfFieldId checkboxFalse;
  boolean isRadioButton;
  String valueTrue;
  String valueFalse;

  @Override
  public List<PdfField> toPdfFields(ElementSpecification elementSpec, Certificate certificate) {
    return elementSpec
        .valueAs(certificate, ElementValueBoolean.class)
        .flatMap(this::toField)
        .stream()
        .toList();
  }

  private java.util.Optional<PdfField> toField(ElementValueBoolean value) {
    if (value.value() == null) {
      return java.util.Optional.empty();
    }

    if (checkboxFalse == null && !value.value()) {
      return java.util.Optional.empty();
    }

    final var fieldId = Boolean.FALSE.equals(value.value()) ? checkboxFalse : checkboxTrue;
    return java.util.Optional.of(
        PdfField.builder().fieldId(fieldId).value(CHECKED_BOX_VALUE).build());
  }
}
