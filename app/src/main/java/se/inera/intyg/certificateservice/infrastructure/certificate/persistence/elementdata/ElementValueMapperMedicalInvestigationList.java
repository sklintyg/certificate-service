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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.elementdata;

import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueMedicalInvestigationList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalInvestigation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@Component
public class ElementValueMapperMedicalInvestigationList implements ElementValueMapper {

  @Override
  public boolean supports(Class<?> c) {
    return c.equals(MappedElementValueMedicalInvestigationList.class)
        || c.equals(ElementValueMedicalInvestigationList.class);
  }

  @Override
  public ElementValue toDomain(MappedElementValue mappedValue) {
    if (mappedValue
        instanceof
        MappedElementValueMedicalInvestigationList mappedElementValueMedicalInvestigationList) {
      return ElementValueMedicalInvestigationList.builder()
          .id(new FieldId(mappedElementValueMedicalInvestigationList.getId()))
          .list(
              mappedElementValueMedicalInvestigationList.getList().stream()
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
    throw new IllegalStateException("MappedElementValue not supported '%s'".formatted(mappedValue));
  }

  @Override
  public MappedElementValue toMapped(ElementValue value) {
    if (value
        instanceof ElementValueMedicalInvestigationList elementValueMedicalInvestigationList) {
      return MappedElementValueMedicalInvestigationList.builder()
          .id(elementValueMedicalInvestigationList.id().value())
          .list(
              elementValueMedicalInvestigationList.list().stream()
                  .map(
                      medicalInvestigation ->
                          MappedMedicalInvestigation.builder()
                              .id(medicalInvestigation.id().value())
                              .date(
                                  MappedDate.builder()
                                      .id(medicalInvestigation.date().dateId().value())
                                      .date(medicalInvestigation.date().date())
                                      .build())
                              .investigationType(
                                  MappedCode.builder()
                                      .id(medicalInvestigation.investigationType().codeId().value())
                                      .code(medicalInvestigation.investigationType().code())
                                      .build())
                              .informationSource(
                                  MappedText.builder()
                                      .id(medicalInvestigation.informationSource().textId().value())
                                      .text(medicalInvestigation.informationSource().text())
                                      .build())
                              .build())
                  .toList())
          .build();
    }
    throw new IllegalStateException("ElementValue not supported '%s'".formatted(value));
  }
}
