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

import static org.junit.jupiter.api.Assertions.assertThrows;
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
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.exception.CertificateActionForbidden;
import se.inera.intyg.certificateservice.domain.message.model.Message;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.repository.MessageRepository;

@ExtendWith(MockitoExtension.class)
class DeleteMessageDomainServiceTest {

  private static final CertificateId CERTIFICATE_ID = new CertificateId("certificateId");
  private static final MessageId MESSAGE_ID = new MessageId("messageId");
  @Mock MessageRepository messageRepository;
  @Mock CertificateRepository certificateRepository;
  @InjectMocks DeleteMessageDomainService deleteMessageDomainService;

  @Test
  void shallThrowIfNotAllowedToDeleteMessage() {
    final var certificate = mock(MedicalCertificate.class);
    final var message = mock(Message.class);
    doReturn(message).when(messageRepository).getById(MESSAGE_ID);
    doReturn(CERTIFICATE_ID).when(message).certificateId();
    doReturn(CERTIFICATE_ID).when(certificate).id();
    doReturn(certificate).when(certificateRepository).getById(CERTIFICATE_ID);
    doReturn(false)
        .when(certificate)
        .allowTo(CertificateActionType.DELETE_MESSAGE, Optional.of(ACTION_EVALUATION));

    assertThrows(
        CertificateActionForbidden.class,
        () -> deleteMessageDomainService.delete(MESSAGE_ID, ACTION_EVALUATION));
  }

  @Test
  void shallDeleteMessage() {
    final var certificate = mock(MedicalCertificate.class);
    final var message = mock(Message.class);
    doReturn(message).when(messageRepository).getById(MESSAGE_ID);
    doReturn(CERTIFICATE_ID).when(message).certificateId();
    doReturn(certificate).when(certificateRepository).getById(CERTIFICATE_ID);
    doReturn(true)
        .when(certificate)
        .allowTo(CertificateActionType.DELETE_MESSAGE, Optional.of(ACTION_EVALUATION));

    deleteMessageDomainService.delete(MESSAGE_ID, ACTION_EVALUATION);

    verify(message).delete();
  }

  @Test
  void shallPersistDeletedMessage() {
    final var certificate = mock(MedicalCertificate.class);
    final var message = mock(Message.class);
    doReturn(message).when(messageRepository).getById(MESSAGE_ID);
    doReturn(CERTIFICATE_ID).when(message).certificateId();
    doReturn(certificate).when(certificateRepository).getById(CERTIFICATE_ID);
    doReturn(true)
        .when(certificate)
        .allowTo(CertificateActionType.DELETE_MESSAGE, Optional.of(ACTION_EVALUATION));

    deleteMessageDomainService.delete(MESSAGE_ID, ACTION_EVALUATION);

    verify(messageRepository).save(message);
  }
}
