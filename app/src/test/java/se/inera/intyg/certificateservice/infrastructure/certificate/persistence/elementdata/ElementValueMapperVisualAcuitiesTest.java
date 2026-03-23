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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.Correction;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueVisualAcuities;
import se.inera.intyg.certificateservice.domain.certificate.model.VisualAcuity;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueMapperVisualAcuitiesTest {

  private ElementValueMapperVisualAcuities elementValueMapperVisualAcuities;

  @BeforeEach
  void setUp() {
    elementValueMapperVisualAcuities = new ElementValueMapperVisualAcuities();
  }

  @Test
  void shallSupportClassMappedElementValuevisualAcuities() {
    assertTrue(elementValueMapperVisualAcuities.supports(MappedElementValueVisualAcuities.class));
  }

  @Test
  void shallSupportClassElementValuevisualAcuities() {
    assertTrue(elementValueMapperVisualAcuities.supports(ElementValueVisualAcuities.class));
  }

  @Test
  void shallReturnFalseForUnsupportedClass() {
    assertFalse(elementValueMapperVisualAcuities.supports(String.class));
  }

  @Test
  void shallMapToDomain() {
    final var expectedValue =
        ElementValueVisualAcuities.builder()
            .rightEye(
                VisualAcuity.builder()
                    .withoutCorrection(
                        Correction.builder().id(new FieldId("id1")).value(1.1).build())
                    .withCorrection(Correction.builder().id(new FieldId("id2")).value(1.2).build())
                    .build())
            .leftEye(
                VisualAcuity.builder()
                    .withoutCorrection(
                        Correction.builder().id(new FieldId("id3")).value(2.1).build())
                    .withCorrection(Correction.builder().id(new FieldId("id4")).value(2.2).build())
                    .build())
            .binocular(
                VisualAcuity.builder()
                    .withoutCorrection(
                        Correction.builder().id(new FieldId("id5")).value(3.1).build())
                    .withCorrection(Correction.builder().id(new FieldId("id6")).value(3.2).build())
                    .build())
            .build();

    final var mappedElementValueVisualAcuities =
        MappedElementValueVisualAcuities.builder()
            .rightEye(
                MappedElementValueVisualAcuity.builder()
                    .withoutCorrection(
                        MappedElementValueDouble.builder().id("id1").value(1.1).build())
                    .withCorrection(MappedElementValueDouble.builder().id("id2").value(1.2).build())
                    .build())
            .leftEye(
                MappedElementValueVisualAcuity.builder()
                    .withoutCorrection(
                        MappedElementValueDouble.builder().id("id3").value(2.1).build())
                    .withCorrection(MappedElementValueDouble.builder().id("id4").value(2.2).build())
                    .build())
            .binocular(
                MappedElementValueVisualAcuity.builder()
                    .withoutCorrection(
                        MappedElementValueDouble.builder().id("id5").value(3.1).build())
                    .withCorrection(MappedElementValueDouble.builder().id("id6").value(3.2).build())
                    .build())
            .build();

    final var actualValue =
        elementValueMapperVisualAcuities.toDomain(mappedElementValueVisualAcuities);

    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallMapToMapped() {
    final var elementValueVisualAcuities =
        ElementValueVisualAcuities.builder()
            .rightEye(
                VisualAcuity.builder()
                    .withoutCorrection(
                        Correction.builder().id(new FieldId("id1")).value(1.1).build())
                    .withCorrection(Correction.builder().id(new FieldId("id2")).value(1.2).build())
                    .build())
            .leftEye(
                VisualAcuity.builder()
                    .withoutCorrection(
                        Correction.builder().id(new FieldId("id3")).value(2.1).build())
                    .withCorrection(Correction.builder().id(new FieldId("id4")).value(2.2).build())
                    .build())
            .binocular(
                VisualAcuity.builder()
                    .withoutCorrection(
                        Correction.builder().id(new FieldId("id5")).value(3.1).build())
                    .withCorrection(Correction.builder().id(new FieldId("id6")).value(3.2).build())
                    .build())
            .build();

    final var expectedValue =
        MappedElementValueVisualAcuities.builder()
            .rightEye(
                MappedElementValueVisualAcuity.builder()
                    .withoutCorrection(
                        MappedElementValueDouble.builder().id("id1").value(1.1).build())
                    .withCorrection(MappedElementValueDouble.builder().id("id2").value(1.2).build())
                    .build())
            .leftEye(
                MappedElementValueVisualAcuity.builder()
                    .withoutCorrection(
                        MappedElementValueDouble.builder().id("id3").value(2.1).build())
                    .withCorrection(MappedElementValueDouble.builder().id("id4").value(2.2).build())
                    .build())
            .binocular(
                MappedElementValueVisualAcuity.builder()
                    .withoutCorrection(
                        MappedElementValueDouble.builder().id("id5").value(3.1).build())
                    .withCorrection(MappedElementValueDouble.builder().id("id6").value(3.2).build())
                    .build())
            .build();

    final var actualValue = elementValueMapperVisualAcuities.toMapped(elementValueVisualAcuities);

    assertEquals(expectedValue, actualValue);
  }
}
