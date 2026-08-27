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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProvider.ALFA_REGIONEN;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProviderConstants.ALFA_REGIONEN_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProviderConstants.ALFA_REGIONEN_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareUnit.ALFA_MEDICINCENTRUM;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModel.fk7210certificateModelBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.athenaReactAnderssonBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataStaff.AJLA_DOKTOR;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataSubUnit.ALFA_ALLERGIMOTTAGNINGEN;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataSubUnitConstants.ALFA_ALLERGIMOTTAGNINGEN_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataSubUnitConstants.ALFA_ALLERGIMOTTAGNINGEN_WORKPLACE_CODE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUserConstants.AJLA_DOCTOR_FULLNAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUserConstants.AJLA_DOCTOR_HSA_ID;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.BinaryUnitDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateMetaData;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Pdf;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;

@ExtendWith(MockitoExtension.class)
class BinaryCertificateMetadataConverterTest {

  private static final String CERTIFICATE_ID = "certificateId";
  private static final LocalDateTime SIGNED = LocalDateTime.now();

  @Mock private CertificateUnitConverter certificateUnitConverter;
  @InjectMocks private BinaryCertificateMetadataConverter binaryCertificateMetadataConverter;

  private MedicalCertificate certificate;
  private Pdf pdf;

  @BeforeEach
  void setUp() {
    certificate =
        MedicalCertificate.builder()
            .id(new CertificateId(CERTIFICATE_ID))
            .status(Status.SIGNED)
            .signed(SIGNED)
            .certificateModel(fk7210certificateModelBuilder().build())
            .certificateMetaData(
                CertificateMetaData.builder()
                    .patient(athenaReactAnderssonBuilder().build())
                    .issuer(AJLA_DOKTOR)
                    .issuingUnit(ALFA_ALLERGIMOTTAGNINGEN)
                    .careUnit(ALFA_MEDICINCENTRUM)
                    .careProvider(ALFA_REGIONEN)
                    .build())
            .elementData(Collections.emptyList())
            .build();

    pdf = new Pdf("pdfData".getBytes(), "fileName");

    doReturn(BinaryUnitDTO.builder().unitId(ALFA_ALLERGIMOTTAGNINGEN_ID).build())
        .when(certificateUnitConverter)
        .convert(ALFA_ALLERGIMOTTAGNINGEN, Optional.empty());
  }

  @Test
  void shallIncludeCertificateId() {
    final var result = binaryCertificateMetadataConverter.convert(certificate, pdf);
    assertEquals(CERTIFICATE_ID, result.getCertificateId());
  }

  @Test
  void shallIncludeSignedAt() {
    final var result = binaryCertificateMetadataConverter.convert(certificate, pdf);
    assertEquals(SIGNED, result.getSignedAt());
  }

  @Test
  void shallIncludeIssuedByPersonId() {
    final var result = binaryCertificateMetadataConverter.convert(certificate, pdf);
    assertEquals(AJLA_DOCTOR_HSA_ID, result.getIssuedBy().getPersonId());
  }

  @Test
  void shallIncludeIssuedByFullName() {
    final var result = binaryCertificateMetadataConverter.convert(certificate, pdf);
    assertEquals(AJLA_DOCTOR_FULLNAME, result.getIssuedBy().getFullName());
  }

  @Test
  void shallIncludeIssuedByTitles() {
    final var result = binaryCertificateMetadataConverter.convert(certificate, pdf);
    assertEquals(2, result.getIssuedBy().getTitles().size());
  }

  @Test
  void shallIncludeIssuedByUnitWorkplaceCode() {
    final var result = binaryCertificateMetadataConverter.convert(certificate, pdf);
    assertEquals(
        ALFA_ALLERGIMOTTAGNINGEN_WORKPLACE_CODE, result.getIssuedBy().getUnit().getWorkplaceCode());
  }

  @Test
  void shallIncludeIssuedByUnitCareProvider() {
    final var result = binaryCertificateMetadataConverter.convert(certificate, pdf);
    assertEquals(ALFA_REGIONEN_ID, result.getIssuedBy().getUnit().getCareProvider().getUnitId());
    assertEquals(
        ALFA_REGIONEN_NAME, result.getIssuedBy().getUnit().getCareProvider().getUnitName());
  }

  @Test
  void shallHaveRawPdfDataWithoutManualBase64Encoding() {
    final var result = binaryCertificateMetadataConverter.convert(certificate, pdf);
    assertArrayEquals("pdfData".getBytes(), result.getPdfData());
  }

  @Test
  void shallReturnEmptyStatusesWhenNotSentOrRevoked() {
    final var result = binaryCertificateMetadataConverter.convert(certificate, pdf);
    assertEquals(0, result.getStatuses().size());
  }

  @Test
  void shallNotIncludeParentRelationIfNoParent() {
    final var result = binaryCertificateMetadataConverter.convert(certificate, pdf);
    assertNull(result.getRelations().getParent());
  }
}
