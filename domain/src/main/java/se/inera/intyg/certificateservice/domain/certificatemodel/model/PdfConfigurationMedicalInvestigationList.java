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
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueMedicalInvestigationList;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalInvestigation;

@Value
@Builder
public class PdfConfigurationMedicalInvestigationList implements PdfConfiguration {

  Map<FieldId, PdfConfigurationMedicalInvestigation> list;

  @Override
  public Stream<PdfField> toPdfFields(
      ElementSpecification elementSpec,
      Certificate certificate,
      CustomPdfSpecification pdfSpecification) {
    return elementSpec.valueAs(certificate, ElementValueMedicalInvestigationList.class).stream()
        .flatMap(value -> value.list().stream())
        .flatMap(
            investigation -> fieldsForInvestigation(investigation, list.get(investigation.id())));
  }

  private static Stream<PdfField> fieldsForInvestigation(
      MedicalInvestigation medicalInvestigation,
      PdfConfigurationMedicalInvestigation pdfConfigurationMedicalInvestigation) {
    final List<PdfField> fields = new ArrayList<>();

    if (medicalInvestigation.date() != null && medicalInvestigation.date().date() != null) {
      fields.add(
          PdfField.builder()
              .fieldId(pdfConfigurationMedicalInvestigation.datePdfFieldId())
              .value(medicalInvestigation.date().date().toString())
              .build());
    }

    if (medicalInvestigation.informationSource() != null
        && medicalInvestigation.informationSource().text() != null
        && !medicalInvestigation.informationSource().text().isEmpty()) {
      fields.add(
          PdfField.builder()
              .fieldId(pdfConfigurationMedicalInvestigation.sourceTypePdfFieldId())
              .value(medicalInvestigation.informationSource().text())
              .build());
    }

    if (medicalInvestigation.investigationType() != null
        && medicalInvestigation.investigationType().code() != null
        && !medicalInvestigation.investigationType().code().isEmpty()) {
      fields.add(
          PdfField.builder()
              .fieldId(pdfConfigurationMedicalInvestigation.investigationPdfFieldId())
              .value(
                  pdfConfigurationMedicalInvestigation
                      .investigationPdfOptions()
                      .get(medicalInvestigation.investigationType().code()))
              .build());
    }

    return fields.stream();
  }
}
