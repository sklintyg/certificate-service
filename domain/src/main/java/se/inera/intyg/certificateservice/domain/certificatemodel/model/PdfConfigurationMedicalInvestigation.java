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
import java.util.Optional;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalInvestigation;

@Value
@Builder
public class PdfConfigurationMedicalInvestigation implements PdfConfiguration {

  PdfFieldId datePdfFieldId;
  Map<String, String> investigationPdfOptions;
  PdfFieldId investigationPdfFieldId;
  PdfFieldId sourceTypePdfFieldId;

  public Stream<PdfField> toPdfFields(MedicalInvestigation investigation) {
    return Stream.of(
            dateField(investigation),
            sourceTypeField(investigation),
            investigationTypeField(investigation))
        .flatMap(Optional::stream);
  }

  private Optional<PdfField> dateField(MedicalInvestigation investigation) {
    return Optional.ofNullable(investigation.date())
        .map(ElementValueDate::date)
        .map(Object::toString)
        .map(value -> toField(datePdfFieldId, value));
  }

  private Optional<PdfField> sourceTypeField(MedicalInvestigation investigation) {
    return Optional.ofNullable(investigation.informationSource())
        .map(ElementValueText::text)
        .filter(this::notEmpty)
        .map(value -> toField(sourceTypePdfFieldId, value));
  }

  private Optional<PdfField> investigationTypeField(MedicalInvestigation investigation) {
    return Optional.ofNullable(investigation.investigationType())
        .map(ElementValueCode::code)
        .filter(this::notEmpty)
        .map(investigationPdfOptions::get)
        .filter(this::notEmpty)
        .map(value -> toField(investigationPdfFieldId, value));
  }

  private PdfField toField(PdfFieldId fieldId, String value) {
    return PdfField.builder().fieldId(fieldId).value(value).build();
  }

  private boolean notEmpty(String value) {
    return value != null && !value.isEmpty();
  }
}
