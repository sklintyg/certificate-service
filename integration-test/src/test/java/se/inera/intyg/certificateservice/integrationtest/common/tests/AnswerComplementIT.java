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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO.SIGNED;
import static se.inera.intyg.certificateservice.application.testdata.TestDataIncomingMessage.incomingComplementDTOBuilder;
import static se.inera.intyg.certificateservice.application.testdata.TestDataIncomingMessage.incomingComplementMessageBuilder;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultAnswerComplementRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultComplementCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultGetCertificateMessageRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultSendCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultSignCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultTestablilityCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificateId;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.questions;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.common.dto.ResourceLinkTypeDTO;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseIntegrationIT;

public abstract class AnswerComplementIT extends BaseIntegrationIT {

  @Test
  @DisplayName("Skall markera komplettering som hanterad om besvarad med meddelande")
  void shallSetComplementedQuestionsToHandledWitAnswers() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion(), SIGNED));

    api().sendCertificate(defaultSendCertificateRequest(), certificateId(testCertificates));

    api()
        .receiveMessage(
            incomingComplementMessageBuilder()
                .certificateId(certificateId(testCertificates))
                .complements(
                    List.of(incomingComplementDTOBuilder().questionId(questionId()).build()))
                .build());

    api().answerComplement(defaultAnswerComplementRequest(), certificateId(testCertificates));

    final var messagesForCertificate =
        api()
            .getMessagesForCertificate(
                defaultGetCertificateMessageRequest(), certificateId(testCertificates));

    assertTrue(
        questions(messagesForCertificate.getBody()).get(0).isHandled(),
        "Should return true when answer complement is handled!");
  }

  @Test
  @DisplayName("Skall skicka det nya intyget om kompletteringen besvaras med ett nytt intyg")
  void shallSendComplementingCertificateAfterSign() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion(), SIGNED));

    api().sendCertificate(defaultSendCertificateRequest(), certificateId(testCertificates));

    api()
        .receiveMessage(
            incomingComplementMessageBuilder()
                .certificateId(certificateId(testCertificates))
                .complements(
                    List.of(incomingComplementDTOBuilder().questionId(questionId()).build()))
                .build());

    final var complementingCertificate =
        api()
            .complementCertificate(
                defaultComplementCertificateRequest(), certificateId(testCertificates))
            .getBody()
            .getCertificate();

    final var signedComplement =
        api()
            .signCertificate(
                defaultSignCertificateRequest(),
                complementingCertificate.getMetadata().getId(),
                complementingCertificate.getMetadata().getVersion())
            .getBody()
            .getCertificate();

    assertTrue(
        signedComplement.getLinks().stream()
            .anyMatch(link -> link.getType() == ResourceLinkTypeDTO.SEND_AFTER_SIGN_CERTIFICATE),
        "Should send certificate after sign when complementing certificate!");
  }
}
