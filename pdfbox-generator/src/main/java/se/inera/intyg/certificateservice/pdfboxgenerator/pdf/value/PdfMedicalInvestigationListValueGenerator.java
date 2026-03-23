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
package se.inera.intyg.certificateservice.pdfboxgenerator.pdf.value;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueMedicalInvestigationList;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalInvestigation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationMedicalInvestigation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationMedicalInvestigationList;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

@Component
public class PdfMedicalInvestigationListValueGenerator
    implements PdfElementValue<ElementValueMedicalInvestigationList> {

  @Override
  public Class<ElementValueMedicalInvestigationList> getType() {
    return ElementValueMedicalInvestigationList.class;
  }

  @Override
  public List<PdfField> generate(
      ElementSpecification elementSpecification,
      ElementValueMedicalInvestigationList elementValue) {
    final var pdfConfiguration =
        (PdfConfigurationMedicalInvestigationList) elementSpecification.pdfConfiguration();
    return getFields(elementValue, pdfConfiguration);
  }

  private List<PdfField> getFields(
      ElementValueMedicalInvestigationList medicalInvestigationList,
      PdfConfigurationMedicalInvestigationList configuration) {
    return medicalInvestigationList.list().stream()
        .map(
            medicalInvestigation ->
                getMedicalInvestigationFields(
                    medicalInvestigation, configuration.list().get(medicalInvestigation.id())))
        .flatMap(List::stream)
        .toList();
  }

  private static List<PdfField> getMedicalInvestigationFields(
      MedicalInvestigation medicalInvestigation,
      PdfConfigurationMedicalInvestigation pdfConfigurationMedicalInvestigation) {

    final var fields = new ArrayList<PdfField>();

    if (medicalInvestigation.date() != null && medicalInvestigation.date().date() != null) {
      fields.add(
          PdfField.builder()
              .id(pdfConfigurationMedicalInvestigation.datePdfFieldId().id())
              .value(
                  medicalInvestigation.date().date() != null
                      ? medicalInvestigation.date().date().toString()
                      : "")
              .build());
    }

    if (medicalInvestigation.informationSource() != null
        && medicalInvestigation.informationSource().text() != null
        && !medicalInvestigation.informationSource().text().isEmpty()) {
      fields.add(
          PdfField.builder()
              .id(pdfConfigurationMedicalInvestigation.sourceTypePdfFieldId().id())
              .value(medicalInvestigation.informationSource().text())
              .build());
    }

    if (medicalInvestigation.investigationType() != null
        && medicalInvestigation.investigationType().code() != null
        && !medicalInvestigation.investigationType().code().isEmpty()) {
      fields.add(
          PdfField.builder()
              .id(pdfConfigurationMedicalInvestigation.investigationPdfFieldId().id())
              .value(
                  pdfConfigurationMedicalInvestigation
                      .investigationPdfOptions()
                      .get(medicalInvestigation.investigationType().code()))
              .build());
    }

    return fields;
  }
}
