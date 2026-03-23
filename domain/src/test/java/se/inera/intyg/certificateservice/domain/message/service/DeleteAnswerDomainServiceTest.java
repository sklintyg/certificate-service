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
package se.inera.intyg.certificateservice.domain.message.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataAction.ACTION_EVALUATION;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.common.exception.CertificateActionForbidden;
import se.inera.intyg.certificateservice.domain.message.model.Answer;
import se.inera.intyg.certificateservice.domain.message.model.Message;
import se.inera.intyg.certificateservice.domain.message.model.MessageStatus;
import se.inera.intyg.certificateservice.domain.message.repository.MessageRepository;

@ExtendWith(MockitoExtension.class)
class DeleteAnswerDomainServiceTest {

  private static final CertificateId CERTIFICATE_ID = new CertificateId("certificateId");
  @Mock MessageRepository messageRepository;
  @InjectMocks DeleteAnswerDomainService deleteAnswerDomainService;

  @Test
  void shallThrowIfNotAllowedToDeleteAnswer() {
    final var certificate = mock(MedicalCertificate.class);
    final var message = mock(Message.class);

    doReturn(CERTIFICATE_ID).when(certificate).id();
    doReturn(false)
        .when(certificate)
        .allowTo(CertificateActionType.DELETE_ANSWER, Optional.of(ACTION_EVALUATION));

    assertThrows(
        CertificateActionForbidden.class,
        () -> deleteAnswerDomainService.delete(message, certificate, ACTION_EVALUATION));
  }

  @Test
  void shallPersistMessage() {
    final var expectedMessage =
        Message.builder().answer(Answer.builder().status(MessageStatus.DRAFT).build()).build();
    final var certificate = mock(MedicalCertificate.class);

    doReturn(true)
        .when(certificate)
        .allowTo(CertificateActionType.DELETE_ANSWER, Optional.of(ACTION_EVALUATION));

    deleteAnswerDomainService.delete(expectedMessage, certificate, ACTION_EVALUATION);

    verify(messageRepository).save(expectedMessage);
  }

  @Test
  void shallReturnMessageWithDeletedAnswer() {
    final var expectedMessage =
        Message.builder().answer(Answer.builder().status(MessageStatus.DRAFT).build()).build();
    final var certificate = mock(MedicalCertificate.class);
    doReturn(true)
        .when(certificate)
        .allowTo(CertificateActionType.DELETE_ANSWER, Optional.of(ACTION_EVALUATION));
    doReturn(expectedMessage).when(messageRepository).save(expectedMessage);

    final var actualMessage =
        deleteAnswerDomainService.delete(expectedMessage, certificate, ACTION_EVALUATION);

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void shallCallDeleteAnswerOnMessage() {
    final var message = mock(Message.class);
    final var certificate = mock(MedicalCertificate.class);
    doReturn(true)
        .when(certificate)
        .allowTo(CertificateActionType.DELETE_ANSWER, Optional.of(ACTION_EVALUATION));
    doReturn(message).when(messageRepository).save(message);
    doNothing().when(message).deleteAnswer();

    deleteAnswerDomainService.delete(message, certificate, ACTION_EVALUATION);

    verify(message).deleteAnswer();
  }
}
