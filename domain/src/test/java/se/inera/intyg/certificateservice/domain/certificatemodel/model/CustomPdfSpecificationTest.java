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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_PDF_PATIENT_ID_FIELD_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_PDF_SIGNATURE_PAGE_INDEX;

import java.util.List;
import org.junit.jupiter.api.Test;

class CustomPdfSpecificationTest {

  @Test
  void shallBeAPdfSpecification() {
    final var expected = CustomPdfSpecification.builder().build();

    assertInstanceOf(PdfSpecification.class, expected);
  }

  @Test
  void shallIncludePatientIdFieldIds() {
    final var expected = List.of(FK7210_PDF_PATIENT_ID_FIELD_ID);

    final var specification = CustomPdfSpecification.builder().patientIdFieldIds(expected).build();

    assertEquals(expected, specification.patientIdFieldIds());
  }

  @Test
  void shallIncludeSignature() {
    final var expected =
        CustomPdfSignature.builder().signaturePageIndex(FK7210_PDF_SIGNATURE_PAGE_INDEX).build();
    final var spec = CustomPdfSpecification.builder().signature(expected).build();

    assertEquals(expected, spec.signature());
  }
}
