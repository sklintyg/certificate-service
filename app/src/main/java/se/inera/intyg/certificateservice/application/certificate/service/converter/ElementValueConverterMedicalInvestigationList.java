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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import static se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType.MEDICAL_INVESTIGATION_LIST;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValue;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueMedicalInvestigationList;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueMedicalInvestigationList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalInvestigation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@Component
@RequiredArgsConstructor
public class ElementValueConverterMedicalInvestigationList implements ElementValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return MEDICAL_INVESTIGATION_LIST;
  }

  @Override
  public ElementValue convert(CertificateDataValue value) {
    if (!(value
        instanceof
        CertificateDataValueMedicalInvestigationList dataValueMedicalInvestigationList)) {
      throw new IllegalStateException(
          "Invalid value type. Type was '%s'".formatted(value.getType()));
    }

    return ElementValueMedicalInvestigationList.builder()
        .id(new FieldId(dataValueMedicalInvestigationList.getId()))
        .list(
            dataValueMedicalInvestigationList.getList().stream()
                .map(
                    medicalInvestigation ->
                        MedicalInvestigation.builder()
                            .id(new FieldId(medicalInvestigation.getId()))
                            .date(
                                ElementValueDate.builder()
                                    .dateId(new FieldId(medicalInvestigation.getDate().getId()))
                                    .date(medicalInvestigation.getDate().getDate())
                                    .build())
                            .investigationType(
                                ElementValueCode.builder()
                                    .codeId(
                                        new FieldId(
                                            medicalInvestigation.getInvestigationType().getId()))
                                    .code(medicalInvestigation.getInvestigationType().getCode())
                                    .build())
                            .informationSource(
                                ElementValueText.builder()
                                    .textId(
                                        new FieldId(
                                            medicalInvestigation.getInformationSource().getId()))
                                    .text(medicalInvestigation.getInformationSource().getText())
                                    .build())
                            .build())
                .toList())
        .build();
  }
}
