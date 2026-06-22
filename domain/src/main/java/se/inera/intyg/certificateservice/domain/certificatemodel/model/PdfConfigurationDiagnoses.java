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
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosis;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosisList;

@Value
@Builder
public class PdfConfigurationDiagnoses implements PdfConfiguration {

  PdfFieldId prefix;
  Map<FieldId, PdfConfigurationDiagnosis> diagnoses;
  Integer maxLength;
  String appearance;
  PdfFieldId overflowSheetFieldId;

  @Override
  public Stream<PdfField> toPdfFields(
      ElementSpecification elementSpec,
      Certificate certificate,
      CustomPdfSpecification pdfSpecification) {
    return elementSpec.valueAs(certificate, ElementValueDiagnosisList.class).stream()
        .flatMap(value -> value.diagnoses().stream())
        .flatMap(
            elementValueDiagnosis ->
                toPdfFields(elementValueDiagnosis, elementSpec, pdfSpecification));
  }

  private Stream<PdfField> toPdfFields(
      ElementValueDiagnosis diagnosis,
      ElementSpecification elementSpecification,
      CustomPdfSpecification pdfSpecification) {
    final var pdfConfigurationDiagnosis = diagnoses.get(diagnosis.id());

    if (pdfConfigurationDiagnosis == null) {
      throw new IllegalArgumentException("Diagnosis " + diagnosis.id() + " not found");
    }

    return pdfConfigurationDiagnosis.toPdfFields(
        diagnosis,
        appearance,
        maxLength,
        OverflowConfig.builder()
            .overflowFieldId(pdfSpecification.overflowFieldId())
            .overflowLabel(elementSpecification.configuration().name())
            .build());
  }
}
