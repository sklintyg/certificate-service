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
package se.inera.intyg.certificateservice.application.message.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.message.dto.GetCertificateMessageInternalResponse;
import se.inera.intyg.certificateservice.application.message.dto.QuestionDTO;
import se.inera.intyg.certificateservice.application.message.service.converter.QuestionConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.message.model.Message;

@ExtendWith(MockitoExtension.class)
class GetCertificateMessageInternalServiceTest {

  private static final String CERTIFICATE_ID = "certificateId";
  @Mock private CertificateRepository certificateRepository;
  @Mock private QuestionConverter questionConverter;
  @InjectMocks private GetCertificateMessageInternalService getCertificateMessageInternalService;

  @Test
  void shallReturnResponseWithMessages() {
    final var questionDTO = QuestionDTO.builder().build();
    final var expectedResponse =
        GetCertificateMessageInternalResponse.builder().questions(List.of(questionDTO)).build();

    final var message = mock(Message.class);
    final var certificate = mock(MedicalCertificate.class);
    doReturn(true).when(certificateRepository).exists(new CertificateId(CERTIFICATE_ID));
    doReturn(certificate).when(certificateRepository).getById(new CertificateId(CERTIFICATE_ID));

    final var messages = List.of(message);

    doReturn(messages).when(certificate).messages();
    doReturn(questionDTO).when(questionConverter).convert(message, Collections.emptyList());

    final var actualResult = getCertificateMessageInternalService.get(CERTIFICATE_ID);

    assertEquals(expectedResponse, actualResult);
  }

  @Test
  void shallReturnResponseWithMessagesIfCertificateDoesntExist() {
    final var expectedResponse =
        GetCertificateMessageInternalResponse.builder().questions(Collections.emptyList()).build();

    doReturn(false).when(certificateRepository).exists(new CertificateId(CERTIFICATE_ID));

    final var actualResult = getCertificateMessageInternalService.get(CERTIFICATE_ID);

    assertEquals(expectedResponse, actualResult);
  }
}
