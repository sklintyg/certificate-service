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

import java.util.ArrayList;
import java.util.List;
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
  public List<PdfField> toPdfFields(ElementSpecification elementSpec, Certificate certificate) {
    return elementSpec.valueAs(certificate, ElementValueDiagnosisList.class).stream()
        .flatMap(value -> value.diagnoses().stream())
        .flatMap(this::toPdfFields)
        .toList();
  }

  private Stream<PdfField> toPdfFields(ElementValueDiagnosis diagnosis) {
    final var pdfConfigurationDiagnosis = diagnoses.get(diagnosis.id());
    if (pdfConfigurationDiagnosis == null) {
      throw new IllegalArgumentException("Diagnosis " + diagnosis.id() + " not found");
    }

    final var fields = new ArrayList<PdfField>();
    fields.add(
        PdfField.builder()
            .fieldId(pdfConfigurationDiagnosis.pdfNameFieldId())
            .value(diagnosis.description())
            .appearance(appearance)
            .build());
    fields.addAll(getDiagnosisCodeFields(diagnosis, pdfConfigurationDiagnosis.pdfCodeFieldIds()));
    return fields.stream();
  }

  private static List<PdfField> getDiagnosisCodeFields(
      ElementValueDiagnosis diagnosis, List<PdfFieldId> codeIds) {
    final var fields = new ArrayList<PdfField>();
    if (diagnosis.code() == null) {
      return fields;
    }

    final var codes = diagnosis.code().toCharArray();
    for (var i = 0; i < codes.length; i++) {
      fields.add(
          PdfField.builder().fieldId(codeIds.get(i)).value(String.valueOf(codes[i])).build());
    }

    return fields;
  }
}
