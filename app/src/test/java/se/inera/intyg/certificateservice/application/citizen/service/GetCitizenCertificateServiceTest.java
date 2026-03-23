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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModel.fk7804certificateModelBuilder;

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
import se.inera.intyg.certificateservice.application.citizen.dto.CertificateTextDTO;
import se.inera.intyg.certificateservice.application.citizen.dto.GetCitizenCertificateRequest;
import se.inera.intyg.certificateservice.application.citizen.service.converter.CertificateTextConverter;
import se.inera.intyg.certificateservice.application.citizen.validation.CitizenCertificateRequestValidator;
import se.inera.intyg.certificateservice.application.common.dto.PersonIdDTO;
import se.inera.intyg.certificateservice.application.common.dto.PersonIdTypeDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.citizen.service.GetCitizenCertificateDomainService;
import se.inera.intyg.certificateservice.domain.common.model.CertificateText;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.common.model.PersonIdType;

@ExtendWith(MockitoExtension.class)
class GetCitizenCertificateServiceTest {

  private static final String CERTIFICATE_ID = "CERTIFICATE_ID";
  private static final CertificateDTO CONVERTED_CERTIFICATE = CertificateDTO.builder().build();
  private static final CertificateText CERTIFICATE_TEXT = CertificateText.builder().build();
  private static final CertificateTextDTO CONVERTED_TEXT = CertificateTextDTO.builder().build();
  private static final Certificate CERTIFICATE =
      MedicalCertificate.builder()
          .certificateModel(
              fk7804certificateModelBuilder().name("Test").texts(List.of(CERTIFICATE_TEXT)).build())
          .build();
  private static final String PERSON_ID = "PERSON_ID";
  private static final PersonIdDTO PERSON_ID_DTO =
      PersonIdDTO.builder().id(PERSON_ID).type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER).build();
  private static final GetCitizenCertificateRequest REQUEST =
      GetCitizenCertificateRequest.builder().personId(PERSON_ID_DTO).build();
  private static final PersonId CONVERTED_PERSON_ID =
      PersonId.builder().id(PERSON_ID).type(PersonIdType.PERSONAL_IDENTITY_NUMBER).build();

  @Mock GetCitizenCertificateDomainService getCitizenCertificateDomainService;

  @Mock CertificateConverter certificateConverter;

  @Mock CitizenCertificateRequestValidator citizenCertificateRequestValidator;

  @Mock CertificateTextConverter certificateTextConverter;

  @InjectMocks GetCitizenCertificateService getCitizenCertificateService;

  @BeforeEach
  void setup() {
    when(getCitizenCertificateDomainService.get(
            new CertificateId(CERTIFICATE_ID), CONVERTED_PERSON_ID))
        .thenReturn(CERTIFICATE);

    when(certificateConverter.convertForCitizen(CERTIFICATE, Collections.emptyList()))
        .thenReturn(CONVERTED_CERTIFICATE);

    when(certificateTextConverter.convert(CERTIFICATE_TEXT)).thenReturn(CONVERTED_TEXT);
  }

  @Test
  void shouldValidateCertificateId() {
    getCitizenCertificateService.get(REQUEST, CERTIFICATE_ID);

    verify(citizenCertificateRequestValidator).validate(CERTIFICATE_ID, PERSON_ID_DTO);
  }

  @Test
  void shouldReturnConvertedCertificate() {
    final var result = getCitizenCertificateService.get(REQUEST, CERTIFICATE_ID);

    assertEquals(CONVERTED_CERTIFICATE, result.getCertificate());
  }

  @Test
  void shouldReturnConvertedText() {
    final var result = getCitizenCertificateService.get(REQUEST, CERTIFICATE_ID);

    assertEquals(List.of(CONVERTED_TEXT), result.getTexts());
  }

  @Test
  void shouldReturnAvailableFunctions() {
    final var result = getCitizenCertificateService.get(REQUEST, CERTIFICATE_ID);

    assertEquals(2, result.getAvailableFunctions().size());
  }
}
