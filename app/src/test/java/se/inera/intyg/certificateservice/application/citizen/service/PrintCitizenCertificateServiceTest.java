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

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.citizen.dto.PrintCitizenCertificateRequest;
import se.inera.intyg.certificateservice.application.citizen.dto.PrintCitizenCertificateResponse;
import se.inera.intyg.certificateservice.application.citizen.validation.CitizenCertificateRequestValidator;
import se.inera.intyg.certificateservice.application.common.dto.PersonIdDTO;
import se.inera.intyg.certificateservice.application.common.dto.PersonIdTypeDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Pdf;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.citizen.service.PrintCitizenCertificateDomainService;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.common.model.PersonIdType;

@ExtendWith(MockitoExtension.class)
class PrintCitizenCertificateServiceTest {

  private static final String ADDITIONAL_INFO_TEXT = "additionalInfoText";
  private static final CertificateId CERTIFICATE_ID = new CertificateId("certificateId");
  private static final PersonIdDTO PERSON_ID_DTO =
      PersonIdDTO.builder()
          .id("191212121212")
          .type(PersonIdTypeDTO.PERSONAL_IDENTITY_NUMBER)
          .build();
  private static final Pdf PDF = new Pdf("pdfData".getBytes(), "fileName");
  private static final PrintCitizenCertificateRequest REQUEST =
      PrintCitizenCertificateRequest.builder()
          .additionalInfo(ADDITIONAL_INFO_TEXT)
          .personId(PERSON_ID_DTO)
          .customizationId("elementId")
          .build();
  private static final ElementId ELEMENT_ID = new ElementId("elementId");

  @Mock PrintCitizenCertificateDomainService printCitizenCertificateDomainService;

  @Mock CitizenCertificateRequestValidator citizenCertificateRequestValidator;

  @InjectMocks PrintCitizenCertificateService printCitizenCertificateService;

  @BeforeEach
  void setup() {
    when(printCitizenCertificateDomainService.get(
            CERTIFICATE_ID,
            PersonId.builder()
                .id(PERSON_ID_DTO.getId())
                .type(PersonIdType.PERSONAL_IDENTITY_NUMBER)
                .build(),
            ADDITIONAL_INFO_TEXT,
            List.of(ELEMENT_ID)))
        .thenReturn(PDF);
  }

  @Test
  void shouldValidateCertificateIdAndPersonId() {
    printCitizenCertificateService.get(REQUEST, CERTIFICATE_ID.id());

    verify(citizenCertificateRequestValidator).validate(CERTIFICATE_ID.id(), PERSON_ID_DTO);
  }

  @Test
  void shouldReturnPrintCitizenCertificateResponse() {
    final var expected =
        PrintCitizenCertificateResponse.builder()
            .filename(PDF.fileName())
            .pdfData(PDF.pdfData())
            .build();

    final var response = printCitizenCertificateService.get(REQUEST, CERTIFICATE_ID.id());

    assertEquals(expected, response);
  }
}
