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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueMedicalInvestigationList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalInvestigation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueMapperMedicalInvestigationListTest {

  private ElementValueMapperMedicalInvestigationList elementValueMapper;

  @BeforeEach
  void setUp() {
    elementValueMapper = new ElementValueMapperMedicalInvestigationList();
  }

  @Test
  void shallReturnTrueIfClassMappedIsMappedElementValueMedicalInvestigationList() {
    assertTrue(elementValueMapper.supports(MappedElementValueMedicalInvestigationList.class));
  }

  @Test
  void shallReturnTrueIfClassElementValueIsElementValueMedicalInvestigationList() {
    assertTrue(elementValueMapper.supports(ElementValueMedicalInvestigationList.class));
  }

  @Test
  void shallReturnFalseForUnsupportedClass() {
    assertFalse(elementValueMapper.supports(String.class));
  }

  @Test
  void shallThrowErrorIfWrongMappedValueType() {
    final var mappedElementValue = MappedElementValueCode.builder().build();
    assertThrows(
        IllegalStateException.class, () -> elementValueMapper.toDomain(mappedElementValue));
  }

  @Test
  void shallThrowErrorIfWrongValueType() {
    final var mappedElementValue = ElementValueCode.builder().build();
    assertThrows(
        IllegalStateException.class, () -> elementValueMapper.toMapped(mappedElementValue));
  }

  @Test
  void shallMapToDomain() {
    final var date = LocalDate.now();
    final var text = "text";
    final var code = "code";

    final var expectedValue =
        ElementValueMedicalInvestigationList.builder()
            .id(new FieldId("ID"))
            .list(
                List.of(
                    MedicalInvestigation.builder()
                        .id(new FieldId("MEDICALINVESTIGATION_ID"))
                        .date(
                            ElementValueDate.builder()
                                .dateId(new FieldId("DATE"))
                                .date(date)
                                .build())
                        .informationSource(
                            ElementValueText.builder()
                                .text(text)
                                .textId(new FieldId("TEXT"))
                                .build())
                        .investigationType(
                            ElementValueCode.builder()
                                .codeId(new FieldId("CODE"))
                                .code(code)
                                .build())
                        .build()))
            .build();

    final var mappedElementValue =
        MappedElementValueMedicalInvestigationList.builder()
            .id("ID")
            .list(
                List.of(
                    MappedMedicalInvestigation.builder()
                        .id("MEDICALINVESTIGATION_ID")
                        .date(MappedDate.builder().id("DATE").date(date).build())
                        .informationSource(MappedText.builder().id("TEXT").text(text).build())
                        .investigationType(MappedCode.builder().id("CODE").code(code).build())
                        .build()))
            .build();

    final var actualValue = elementValueMapper.toDomain(mappedElementValue);

    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallMapToMapped() {
    final var date = LocalDate.now();
    final var text = "text";
    final var code = "code";

    final var expectedValue =
        MappedElementValueMedicalInvestigationList.builder()
            .id("ID")
            .list(
                List.of(
                    MappedMedicalInvestigation.builder()
                        .id("MEDICALINVESTIGATION_ID")
                        .date(MappedDate.builder().id("DATE").date(date).build())
                        .informationSource(MappedText.builder().id("TEXT").text(text).build())
                        .investigationType(MappedCode.builder().id("CODE").code(code).build())
                        .build()))
            .build();

    final var elementValue =
        ElementValueMedicalInvestigationList.builder()
            .id(new FieldId("ID"))
            .list(
                List.of(
                    MedicalInvestigation.builder()
                        .id(new FieldId("MEDICALINVESTIGATION_ID"))
                        .date(
                            ElementValueDate.builder()
                                .dateId(new FieldId("DATE"))
                                .date(date)
                                .build())
                        .informationSource(
                            ElementValueText.builder()
                                .text(text)
                                .textId(new FieldId("TEXT"))
                                .build())
                        .investigationType(
                            ElementValueCode.builder()
                                .codeId(new FieldId("CODE"))
                                .code(code)
                                .build())
                        .build()))
            .build();

    final var actualValue = elementValueMapper.toMapped(elementValue);

    assertEquals(expectedValue, actualValue);
  }
}
