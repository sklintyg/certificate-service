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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosis;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDiagnosisList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueMapperDiagosisListTest {

  private static final String EXPECTED_ID = "expectedId";
  private static final String EXPECTED_DESCRIPTION = "expectedDescription";
  private static final String EXPECTED_TERMINOLOGY = "expectedTerminology";
  private static final String EXPECTED_CODE = "expectedCode";
  private ElementValueMapperDiagosisList elementValueMapperDiagosisList;

  @BeforeEach
  void setUp() {
    elementValueMapperDiagosisList = new ElementValueMapperDiagosisList();
  }

  @Test
  void shallReturnTrueIfClassMappedElementValueCode() {
    assertTrue(elementValueMapperDiagosisList.supports(MappedElementValueDiagnosisList.class));
  }

  @Test
  void shallReturnTrueIfClassElementValueCode() {
    assertTrue(elementValueMapperDiagosisList.supports(ElementValueDiagnosisList.class));
  }

  @Test
  void shallReturnFalseForUnsupportedClass() {
    assertFalse(elementValueMapperDiagosisList.supports(String.class));
  }

  @Test
  void shallMapToDomain() {
    final var expectedValue =
        ElementValueDiagnosisList.builder()
            .diagnoses(
                List.of(
                    ElementValueDiagnosis.builder()
                        .id(new FieldId(EXPECTED_ID))
                        .description(EXPECTED_DESCRIPTION)
                        .terminology(EXPECTED_TERMINOLOGY)
                        .code(EXPECTED_CODE)
                        .build()))
            .build();

    final var mappedElementValueDiagnosisList =
        MappedElementValueDiagnosisList.builder()
            .mappedDiagnoses(
                List.of(
                    MappedDiagnosis.builder()
                        .id(EXPECTED_ID)
                        .description(EXPECTED_DESCRIPTION)
                        .terminology(EXPECTED_TERMINOLOGY)
                        .code(EXPECTED_CODE)
                        .build()))
            .build();

    final var actualValue =
        elementValueMapperDiagosisList.toDomain(mappedElementValueDiagnosisList);

    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallMapToMapped() {
    final var elementValueDiagnosisList =
        ElementValueDiagnosisList.builder()
            .diagnoses(
                List.of(
                    ElementValueDiagnosis.builder()
                        .id(new FieldId(EXPECTED_ID))
                        .description(EXPECTED_DESCRIPTION)
                        .terminology(EXPECTED_TERMINOLOGY)
                        .code(EXPECTED_CODE)
                        .build()))
            .build();

    final var expectedValue =
        MappedElementValueDiagnosisList.builder()
            .mappedDiagnoses(
                List.of(
                    MappedDiagnosis.builder()
                        .id(EXPECTED_ID)
                        .description(EXPECTED_DESCRIPTION)
                        .terminology(EXPECTED_TERMINOLOGY)
                        .code(EXPECTED_CODE)
                        .build()))
            .build();

    final var actualValue = elementValueMapperDiagosisList.toMapped(elementValueDiagnosisList);

    assertEquals(expectedValue, actualValue);
  }
}
