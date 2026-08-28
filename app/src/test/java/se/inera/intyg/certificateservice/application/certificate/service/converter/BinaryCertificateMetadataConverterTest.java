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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProvider.ALFA_REGIONEN;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProviderConstants.ALFA_REGIONEN_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProviderConstants.ALFA_REGIONEN_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareUnit.ALFA_MEDICINCENTRUM;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModel.fk7210certificateModelBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModelConstants.FK7210_CODE_TYPE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModelConstants.FK7210_VERSION;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.athenaReactAnderssonBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatientConstants.ATHENA_REACT_ANDERSSON_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataStaff.AJLA_DOKTOR;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataSubUnit.ALFA_ALLERGIMOTTAGNINGEN;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataSubUnitConstants.ALFA_ALLERGIMOTTAGNINGEN_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataSubUnitConstants.ALFA_ALLERGIMOTTAGNINGEN_WORKPLACE_CODE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUserConstants.AJLA_DOCTOR_FULLNAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUserConstants.AJLA_DOCTOR_HEALTH_CARE_PROFESSIONAL_LICENCES;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUserConstants.AJLA_DOCTOR_HSA_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUserConstants.AJLA_DOCTOR_SPECIALITIES;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.BinaryCertificateUnitDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateMetaData;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Relation;
import se.inera.intyg.certificateservice.domain.certificate.model.RelationType;
import se.inera.intyg.certificateservice.domain.certificate.model.Revoked;
import se.inera.intyg.certificateservice.domain.certificate.model.Sent;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.common.model.Speciality;

@ExtendWith(MockitoExtension.class)
class BinaryCertificateMetadataConverterTest {

  private static final String CERTIFICATE_ID = "certificateId";
  private static final LocalDateTime SIGNED = LocalDateTime.now();

  @Mock private BinaryCertificateUnitConverter binaryCertificateUnitConverter;
  @InjectMocks private BinaryCertificateMetadataConverter binaryCertificateMetadataConverter;

  private MedicalCertificate certificate;

  @BeforeEach
  void setUp() {
    certificate =
        baseCertificateBuilder()
            .sent(Sent.builder().build())
            .revoked(Revoked.builder().build())
            .build();

    doReturn(
            BinaryCertificateUnitDTO.builder()
                .unitId(ALFA_ALLERGIMOTTAGNINGEN_ID)
                .workplaceCode(ALFA_ALLERGIMOTTAGNINGEN_WORKPLACE_CODE)
                .build())
        .when(binaryCertificateUnitConverter)
        .convert(ALFA_ALLERGIMOTTAGNINGEN, Optional.empty());
  }

  private MedicalCertificate.MedicalCertificateBuilder baseCertificateBuilder() {
    return MedicalCertificate.builder()
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
        .elementData(Collections.emptyList());
  }

  @Test
  void shallIncludeCertificateId() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertEquals(CERTIFICATE_ID, result.getCertificateId());
  }

  @Test
  void shallIncludeSignedAt() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertEquals(SIGNED, result.getSignedAt());
  }

  @Test
  void shallIncludeIssuedByPersonId() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertEquals(AJLA_DOCTOR_HSA_ID, result.getIssuedBy().getPersonId());
  }

  @Test
  void shallIncludeIssuedByFullName() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertEquals(AJLA_DOCTOR_FULLNAME, result.getIssuedBy().getFullName());
  }

  @Test
  void shallIncludeIssuedByTitles() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertEquals(2, result.getIssuedBy().getTitles().size());
  }

  @Test
  void shallIncludeIssuedByUnitWorkplaceCode() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertEquals(
        ALFA_ALLERGIMOTTAGNINGEN_WORKPLACE_CODE, result.getIssuedBy().getUnit().getWorkplaceCode());
  }

  @Test
  void shallIncludeIssuedByUnitCareProvider() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertEquals(ALFA_REGIONEN_ID, result.getIssuedBy().getUnit().getCareProvider().getId());
    assertEquals(ALFA_REGIONEN_NAME, result.getIssuedBy().getUnit().getCareProvider().getName());
  }

  @Test
  void shallNotIncludeParentRelationIfNoParent() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertNull(result.getRelations().getParent());
  }

  @Test
  void shallReturnNullRevokedAtWhenCertificateIsNotRevoked() {
    final var certificateWithoutRevoked = baseCertificateBuilder().build();
    final var result = binaryCertificateMetadataConverter.convert(certificateWithoutRevoked);
    assertNull(result.getRevokedAt());
  }

  @Test
  void shallIncludeRevokedAtWhenCertificateIsRevoked() {
    final var revokedAt = LocalDateTime.now();
    final var certificateWithRevoked =
        baseCertificateBuilder().revoked(Revoked.builder().revokedAt(revokedAt).build()).build();
    final var result = binaryCertificateMetadataConverter.convert(certificateWithRevoked);
    assertEquals(revokedAt, result.getRevokedAt());
  }

  @Test
  void shallReturnNullSentAtWhenCertificateIsNotSent() {
    final var certificateWithoutSent = baseCertificateBuilder().build();
    final var result = binaryCertificateMetadataConverter.convert(certificateWithoutSent);
    assertNull(result.getSentAt());
  }

  @Test
  void shallIncludeSentAtWhenCertificateIsSent() {
    final var sentAt = LocalDateTime.now();
    final var certificateWithSent =
        baseCertificateBuilder().sent(Sent.builder().sentAt(sentAt).build()).build();
    final var result = binaryCertificateMetadataConverter.convert(certificateWithSent);
    assertEquals(sentAt, result.getSentAt());
  }

  @Test
  void shallIncludeType() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertEquals(FK7210_CODE_TYPE.code(), result.getType().getCode());
    assertEquals(FK7210_CODE_TYPE.codeSystem(), result.getType().getCodeSystem());
    assertEquals(FK7210_CODE_TYPE.displayName(), result.getType().getDisplayName());
  }

  @Test
  void shallIncludeVersion() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertEquals(FK7210_VERSION.version(), result.getVersion());
  }

  @Test
  void shallIncludePatientPersonId() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertEquals(ATHENA_REACT_ANDERSSON_ID, result.getPatient().getPersonId());
  }

  @Test
  void shallIncludeIssuedBySpecialities() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    final var expectedSpecialities =
        AJLA_DOCTOR_SPECIALITIES.stream().map(Speciality::value).toList();
    assertEquals(expectedSpecialities, result.getIssuedBy().getSpecialities());
  }

  @Test
  void shallIncludeIssuedByLicences() {
    final var result = binaryCertificateMetadataConverter.convert(certificate);
    assertEquals(
        AJLA_DOCTOR_HEALTH_CARE_PROFESSIONAL_LICENCES.size(),
        result.getIssuedBy().getLicences().size());
  }

  @Test
  void shallIncludeChildrenRelationsWhenPresent() {
    final var cert =
        baseCertificateBuilder()
            .children(
                List.of(
                    Relation.builder()
                        .certificate(baseCertificateBuilder().build())
                        .created(LocalDateTime.now())
                        .type(RelationType.RENEW)
                        .build()))
            .build();
    final var result = binaryCertificateMetadataConverter.convert(cert);
    assertEquals(1, result.getRelations().getChildren().size());
  }
}
