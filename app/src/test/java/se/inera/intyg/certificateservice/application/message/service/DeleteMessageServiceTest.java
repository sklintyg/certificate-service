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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_ALLERGIMOTTAGNINGEN_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_MEDICINCENTRUM_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_REGIONEN_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUserDTO.AJLA_DOCTOR_DTO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.message.dto.DeleteMessageRequest;
import se.inera.intyg.certificateservice.application.message.service.validator.DeleteMessageRequestValidator;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.service.DeleteMessageDomainService;

@ExtendWith(MockitoExtension.class)
class DeleteMessageServiceTest {

  private static final String MESSAGE_ID = "messageId";
  @Mock DeleteMessageRequestValidator deleteMessageRequestValidator;
  @Mock ActionEvaluationFactory actionEvaluationFactory;
  @Mock DeleteMessageDomainService deleteMessageDomainService;
  @InjectMocks DeleteMessageService deleteMessageService;

  @Test
  void shallThrowIfRequestIsInvalid() {
    final var request = DeleteMessageRequest.builder().build();
    doThrow(IllegalStateException.class)
        .when(deleteMessageRequestValidator)
        .validate(request, MESSAGE_ID);

    assertThrows(
        IllegalStateException.class, () -> deleteMessageService.delete(request, MESSAGE_ID));
  }

  @Test
  void shallCallDeleteMessageDomainService() {
    final var request =
        DeleteMessageRequest.builder()
            .user(AJLA_DOCTOR_DTO)
            .unit(ALFA_ALLERGIMOTTAGNINGEN_DTO)
            .careUnit(ALFA_MEDICINCENTRUM_DTO)
            .careProvider(ALFA_REGIONEN_DTO)
            .build();

    final var actionEvaluation = ActionEvaluation.builder().build();

    doReturn(actionEvaluation)
        .when(actionEvaluationFactory)
        .create(
            AJLA_DOCTOR_DTO,
            ALFA_ALLERGIMOTTAGNINGEN_DTO,
            ALFA_MEDICINCENTRUM_DTO,
            ALFA_REGIONEN_DTO);

    deleteMessageService.delete(request, MESSAGE_ID);

    verify(deleteMessageDomainService).delete(new MessageId(MESSAGE_ID), actionEvaluation);
  }
}
