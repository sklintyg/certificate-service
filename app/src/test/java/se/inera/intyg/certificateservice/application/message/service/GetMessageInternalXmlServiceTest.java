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
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.FK7472_CERTIFICATE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessage.ANSWER;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessage.complementMessageBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessageConstants.MESSAGE_ID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.message.dto.GetMessageInternalXmlResponse;
import se.inera.intyg.certificateservice.domain.certificate.model.Xml;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.repository.MessageRepository;
import se.inera.intyg.certificateservice.domain.message.service.XmlGeneratorMessage;

@ExtendWith(MockitoExtension.class)
class GetMessageInternalXmlServiceTest {

  @Mock private MessageRepository messageRepository;
  @Mock private CertificateRepository certificateRepository;
  @Mock private XmlGeneratorMessage xmlGeneratorMessage;
  @InjectMocks private GetMessageInternalXmlService getMessageInternalXmlService;

  private static final String XML = "<message>test</message>";

  @Test
  void shallReturnXmlWithAnswer() {
    final var expected = GetMessageInternalXmlResponse.builder().xml(new Xml(XML).base64()).build();

    final var complementMessageWithAnswer = complementMessageBuilder().answer(ANSWER).build();

    doReturn(complementMessageWithAnswer)
        .when(messageRepository)
        .findById(new MessageId(MESSAGE_ID));
    doReturn(FK7472_CERTIFICATE).when(certificateRepository).getById(FK7472_CERTIFICATE.id());
    doReturn(new Xml(XML))
        .when(xmlGeneratorMessage)
        .generateAnswer(ANSWER, complementMessageWithAnswer, FK7472_CERTIFICATE);

    assertEquals(expected, getMessageInternalXmlService.get(MESSAGE_ID));
  }

  @Test
  void shallReturnXmlWithoutAnswer() {
    final var expected = GetMessageInternalXmlResponse.builder().xml(new Xml(XML).base64()).build();

    final var complementMessageWithAnswer = complementMessageBuilder().build();

    doReturn(complementMessageWithAnswer)
        .when(messageRepository)
        .findById(new MessageId(MESSAGE_ID));
    doReturn(FK7472_CERTIFICATE).when(certificateRepository).getById(FK7472_CERTIFICATE.id());
    doReturn(new Xml(XML))
        .when(xmlGeneratorMessage)
        .generate(complementMessageWithAnswer, FK7472_CERTIFICATE);

    assertEquals(expected, getMessageInternalXmlService.get(MESSAGE_ID));
  }
}
