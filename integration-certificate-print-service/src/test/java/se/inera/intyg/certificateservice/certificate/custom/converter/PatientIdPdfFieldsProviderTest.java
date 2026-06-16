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
package se.inera.intyg.certificateservice.certificate.custom.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7210CertificateBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID_WITHOUT_DASH;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPdfSpecificationConstants.FK7210_PDF_PATIENT_ID_FIELD_ID;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.certificate.custom.provider.PatientIdPdfFieldsProvider;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.FK7210TemplatePathProvider;

class PatientIdPdfFieldsProviderTest {

  private final PatientIdPdfFieldsProvider provider = new PatientIdPdfFieldsProvider();
  private final CustomPdfSpecification spec =
      CustomPdfSpecification.builder()
          .pdfTemplatePathProvider(new FK7210TemplatePathProvider())
          .patientIdFieldIds(List.of(FK7210_PDF_PATIENT_ID_FIELD_ID))
          .build();

  @Test
  void shallAddPatientIdForEachPatientFieldId() {
    final var certificate = fk7210CertificateBuilder().signed(null).build();

    final var fields = provider.fields(certificate, spec);

    assertEquals(
        ATHENA_REACT_ANDERSSON_ID_WITHOUT_DASH,
        fields.get(FK7210_PDF_PATIENT_ID_FIELD_ID.id()).value());
  }
}
