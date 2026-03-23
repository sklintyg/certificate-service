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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_ALLERGIMOTTAGNINGEN_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_MEDICINCENTRUM_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_REGIONEN_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUserDTO.AJLA_DOCTOR_DTO;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.FK3226_CERTIFICATE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessage.CONTACT_MESSAGE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.message.dto.QuestionDTO;
import se.inera.intyg.certificateservice.application.message.dto.QuestionTypeDTO;
import se.inera.intyg.certificateservice.application.message.dto.SaveMessageRequest;
import se.inera.intyg.certificateservice.application.message.dto.SaveMessageResponse;
import se.inera.intyg.certificateservice.application.message.service.converter.QuestionConverter;
import se.inera.intyg.certificateservice.application.message.service.validator.SaveMessageRequestValidator;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.message.model.Content;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.model.MessageType;
import se.inera.intyg.certificateservice.domain.message.service.SaveMessageDomainService;

@ExtendWith(MockitoExtension.class)
class SaveMessageServiceTest {

  private static final String MESSAGE_ID = "messageId";
  private static final String CERTIFICATE_ID = "certificateId";
  private static final String MESSAGE = "message";
  private static final QuestionDTO QUESTION_DTO =
      QuestionDTO.builder()
          .certificateId(CERTIFICATE_ID)
          .type(QuestionTypeDTO.CONTACT)
          .message(MESSAGE)
          .build();
  @Mock CertificateRepository certificateRepository;
  @Mock SaveMessageRequestValidator saveMessageRequestValidator;
  @Mock ActionEvaluationFactory actionEvaluationFactory;
  @Mock SaveMessageDomainService saveMessageDomainService;
  @Mock QuestionConverter questionConverter;
  @InjectMocks SaveMessageService saveMessageService;

  @Test
  void shallThrowIfRequestIsInvalid() {
    final var request = SaveMessageRequest.builder().build();
    doThrow(IllegalArgumentException.class)
        .when(saveMessageRequestValidator)
        .validate(request, MESSAGE_ID);

    assertThrows(
        IllegalArgumentException.class, () -> saveMessageService.save(request, MESSAGE_ID));
  }

  @Test
  void shallReturnSaveMessageResponseWithQuestion() {
    final var request =
        SaveMessageRequest.builder()
            .user(AJLA_DOCTOR_DTO)
            .unit(ALFA_ALLERGIMOTTAGNINGEN_DTO)
            .careUnit(ALFA_MEDICINCENTRUM_DTO)
            .careProvider(ALFA_REGIONEN_DTO)
            .question(QUESTION_DTO)
            .build();

    final var actionEvaluation = ActionEvaluation.builder().build();

    doReturn(actionEvaluation)
        .when(actionEvaluationFactory)
        .create(
            AJLA_DOCTOR_DTO,
            ALFA_ALLERGIMOTTAGNINGEN_DTO,
            ALFA_MEDICINCENTRUM_DTO,
            ALFA_REGIONEN_DTO);

    doReturn(FK3226_CERTIFICATE)
        .when(certificateRepository)
        .getById(new CertificateId(CERTIFICATE_ID));

    doReturn(CONTACT_MESSAGE)
        .when(saveMessageDomainService)
        .save(
            FK3226_CERTIFICATE,
            new MessageId(MESSAGE_ID),
            new Content(MESSAGE),
            actionEvaluation,
            MessageType.CONTACT);

    final var expectedQuestion = QuestionDTO.builder().build();
    doReturn(expectedQuestion)
        .when(questionConverter)
        .convert(CONTACT_MESSAGE, CONTACT_MESSAGE.actions(actionEvaluation, FK3226_CERTIFICATE));

    final var expectedResponse = SaveMessageResponse.builder().question(expectedQuestion).build();

    final var actualResponse = saveMessageService.save(request, MESSAGE_ID);
    assertEquals(expectedResponse, actualResponse);
  }
}
