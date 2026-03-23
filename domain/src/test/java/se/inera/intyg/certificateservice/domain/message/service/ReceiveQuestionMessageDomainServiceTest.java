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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.CERTIFICATE_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModel.FK7810_CERTIFICATE_MODEL;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModel.fk7810certificateModelBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessage.CONTACT_MESSAGE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessage.contactMessageBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.ATHENA_REACT_ANDERSSON;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.exception.CertificateActionForbidden;
import se.inera.intyg.certificateservice.domain.message.repository.MessageRepository;

@ExtendWith(MockitoExtension.class)
class ReceiveQuestionMessageDomainServiceTest {

  @Mock private Certificate certificate;

  @Mock private CertificateRepository certificateRepository;

  @Mock private MessageRepository messageRepository;

  @InjectMocks private ReceiveQuestionMessageDomainService receiveQuestionMessageDomainService;

  @BeforeEach
  void setUp() {
    doReturn(certificate).when(certificateRepository).getById(CERTIFICATE_ID);
  }

  @Test
  void shallThrowExceptionIfNotAllowedOnCertificate() {
    doReturn(false)
        .when(certificate)
        .allowTo(CertificateActionType.RECEIVE_QUESTION, Optional.empty());
    doReturn(CERTIFICATE_ID).when(certificate).id();

    assertThrows(
        CertificateActionForbidden.class,
        () -> receiveQuestionMessageDomainService.receive(CONTACT_MESSAGE));
  }

  @Test
  void shallThrowExceptionIfNotSamePatientAsCertificate() {
    doReturn(true)
        .when(certificate)
        .allowTo(CertificateActionType.RECEIVE_QUESTION, Optional.empty());
    doReturn(CERTIFICATE_ID).when(certificate).id();
    doReturn(false).when(certificate).isCertificateIssuedOnPatient(ATHENA_REACT_ANDERSSON.id());

    assertThrows(
        CertificateActionForbidden.class,
        () -> receiveQuestionMessageDomainService.receive(CONTACT_MESSAGE));
  }

  @Test
  void shallThrowExceptionIfMessageTypeNotAllowed() {
    doReturn(true)
        .when(certificate)
        .allowTo(CertificateActionType.RECEIVE_QUESTION, Optional.empty());
    doReturn(true).when(certificate).isCertificateIssuedOnPatient(ATHENA_REACT_ANDERSSON.id());
    doReturn(fk7810certificateModelBuilder().messageTypes(List.of()).build())
        .when(certificate)
        .certificateModel();
    doReturn(CERTIFICATE_ID).when(certificate).id();

    assertThrows(
        CertificateActionForbidden.class,
        () -> receiveQuestionMessageDomainService.receive(CONTACT_MESSAGE));
  }

  @Nested
  class ContactMessageAllowed {

    @BeforeEach
    void setUp() {
      doReturn(true)
          .when(certificate)
          .allowTo(CertificateActionType.RECEIVE_QUESTION, Optional.empty());
      doReturn(true).when(certificate).isCertificateIssuedOnPatient(ATHENA_REACT_ANDERSSON.id());
      doReturn(FK7810_CERTIFICATE_MODEL).when(certificate).certificateModel();
    }

    @Test
    void shallStoreReceivedMessage() {
      final var expectedMessage = contactMessageBuilder().build();
      receiveQuestionMessageDomainService.receive(expectedMessage);

      verify(messageRepository).save(expectedMessage);
    }

    @Test
    void shallReturnSavedMessage() {
      final var expectedMessage = contactMessageBuilder().build();

      doReturn(expectedMessage).when(messageRepository).save(CONTACT_MESSAGE);

      final var actualMessage = receiveQuestionMessageDomainService.receive(CONTACT_MESSAGE);

      assertEquals(expectedMessage, actualMessage);
    }
  }
}
