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
package se.inera.intyg.certificateservice.integrationtest.common.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO.SIGNED;
import static se.inera.intyg.certificateservice.application.testdata.TestDataIncomingMessage.incomingComplementDTOBuilder;
import static se.inera.intyg.certificateservice.application.testdata.TestDataIncomingMessage.incomingComplementMessageBuilder;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultGetCertificateFromMessageRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultGetCertificateMessageRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultTestablilityCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificateId;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.messageId;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.questions;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseIntegrationIT;

public abstract class CertificateFromMessageIT extends BaseIntegrationIT {

  @Test
  @DisplayName("Ska returnera intyget om det finns")
  void shallReturnCertificate() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion(), SIGNED));

    api()
        .receiveMessage(
            incomingComplementMessageBuilder()
                .certificateId(certificateId(testCertificates))
                .complements(
                    List.of(incomingComplementDTOBuilder().questionId(questionId()).build()))
                .build());

    final var messagesForCertificate =
        api()
            .getMessagesForCertificate(
                defaultGetCertificateMessageRequest(), certificateId(testCertificates));

    final var questions = questions(messagesForCertificate.getBody());

    final var response =
        api()
            .certificateFromMessage(
                defaultGetCertificateFromMessageRequest(), messageId(questions.get(0)));

    assertEquals(certificateId(testCertificates), certificateId(response.getBody()));
  }

  @Test
  @DisplayName("Om intyget inte finns i tjänsten skall felkod 400 (BAD_REQUEST) returneras")
  void shallNotReturnCertificateIfMissing() {
    final var response =
        api()
            .certificateFromMessage(
                defaultGetCertificateFromMessageRequest(), "message-not-exists");

    assertEquals(400, response.getStatusCode().value());
  }
}
