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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.CERTIFICATE_META_DATA;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7210CertificateBuilder;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

@ExtendWith(MockitoExtension.class)
class PdfPatientValueGeneratorTest {

  private static final String FIELD_ID = "FIELD_ID";
  private static final String FIELD_ID_2 = "FIELD_ID_2";

  @InjectMocks PdfPatientValueGenerator patientInformationHelper;

  @Test
  void shouldSetPatientIdToField() {
    final var certificate = buildCertificate();

    final var expected =
        List.of(
            PdfField.builder()
                .id(FIELD_ID)
                .value(CERTIFICATE_META_DATA.patient().id().idWithoutDash())
                .patientField(true)
                .build());

    final var result =
        patientInformationHelper.generate(certificate, List.of(new PdfFieldId(FIELD_ID)));

    assertEquals(expected, result);
  }

  @Test
  void shouldSetPatientIdToFieldForSeveralFields() {
    final var certificate = buildCertificate();

    final var expected =
        List.of(
            PdfField.builder()
                .id(FIELD_ID)
                .value(CERTIFICATE_META_DATA.patient().id().idWithoutDash())
                .patientField(true)
                .build(),
            PdfField.builder()
                .id(FIELD_ID_2)
                .value(CERTIFICATE_META_DATA.patient().id().idWithoutDash())
                .patientField(true)
                .build());

    final var result =
        patientInformationHelper.generate(
            certificate, List.of(new PdfFieldId(FIELD_ID), new PdfFieldId(FIELD_ID_2)));

    assertEquals(expected, result);
  }

  private Certificate buildCertificate() {
    return fk7210CertificateBuilder()
        .certificateMetaData(null)
        .metaDataFromSignInstance(CERTIFICATE_META_DATA)
        .build();
  }
}
