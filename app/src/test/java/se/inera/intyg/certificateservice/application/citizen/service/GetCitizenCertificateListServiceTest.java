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
package se.inera.intyg.certificateservice.application.citizen.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7210CertificateBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7472CertificateBuilder;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDTO;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConverter;
import se.inera.intyg.certificateservice.application.citizen.dto.GetCitizenCertificateListRequest;
import se.inera.intyg.certificateservice.application.citizen.validation.CitizenCertificateRequestValidator;
import se.inera.intyg.certificateservice.application.common.dto.PersonIdDTO;
import se.inera.intyg.certificateservice.application.common.dto.PersonIdTypeDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateMetaData;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.model.CertificatesRequest;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.common.model.PersonIdType;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;
import se.inera.intyg.certificateservice.domain.patient.model.TestIndicated;

@ExtendWith(MockitoExtension.class)
class GetCitizenCertificateListServiceTest {

  private static final String PERSON_ID = "191212121212";
  private static final PersonIdDTO PERSON_ID_DTO =
      PersonIdDTO.builder().id(PERSON_ID).type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER).build();

  private static final GetCitizenCertificateListRequest REQUEST =
      GetCitizenCertificateListRequest.builder().personId(PERSON_ID_DTO).build();

  private static final CertificatesRequest CERTIFICATES_REQUEST =
      CertificatesRequest.builder()
          .personId(
              PersonId.builder().id(PERSON_ID).type(PersonIdType.PERSONAL_IDENTITY_NUMBER).build())
          .statuses(List.of(Status.SIGNED))
          .build();
  private static final Certificate FK7210 = fk7210CertificateBuilder().build();
  private static final Certificate FK7210_TEST =
      fk7210CertificateBuilder()
          .certificateMetaData(
              CertificateMetaData.builder()
                  .patient(Patient.builder().testIndicated(new TestIndicated(true)).build())
                  .build())
          .build();

  private static final Certificate FK7472 = fk7472CertificateBuilder().build();
  private static final CertificateDTO CONVERTED_CERTIFICATE = CertificateDTO.builder().build();

  @Mock CertificateConverter certificateConverter;

  @Mock CitizenCertificateRequestValidator citizenCertificateRequestValidator;

  @Mock CertificateRepository certificateRepository;

  @InjectMocks GetCitizenCertificateListService getCitizenCertificateListService;

  @BeforeEach
  void setUp() {
    when(certificateRepository.findByCertificatesRequest(CERTIFICATES_REQUEST))
        .thenReturn(List.of(FK7210, FK7472, FK7210_TEST));

    when(certificateConverter.convert(FK7210, Collections.emptyList(), null))
        .thenReturn(CONVERTED_CERTIFICATE);
  }

  @Test
  void shouldValidatePersonId() {
    getCitizenCertificateListService.get(REQUEST);
    verify(citizenCertificateRequestValidator).validate(PERSON_ID_DTO);
  }

  @Test
  void shouldUseCorrectRequestForCertificateRepository() {
    getCitizenCertificateListService.get(REQUEST);
    verify(certificateRepository).findByCertificatesRequest(CERTIFICATES_REQUEST);
  }

  @Test
  void shouldFilterCertificateNotAvailableForCitizenAndTestIndicated() {
    final var result = getCitizenCertificateListService.get(REQUEST);
    assertEquals(1, result.getCitizenCertificates().size());
    assertTrue(result.getCitizenCertificates().contains(CONVERTED_CERTIFICATE));
  }
}
