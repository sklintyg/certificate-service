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

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.exception.CertificateActionForbidden;
import se.inera.intyg.certificateservice.domain.message.model.Answer;
import se.inera.intyg.certificateservice.domain.message.model.Message;
import se.inera.intyg.certificateservice.domain.message.model.MessageId;
import se.inera.intyg.certificateservice.domain.message.repository.MessageRepository;

@RequiredArgsConstructor
public class ReceiveAnswerMessageDomainService {

  private final CertificateRepository certificateRepository;
  private final MessageRepository messageRepository;

  public Message receive(MessageId messageId, Answer answer) {
    final var message = messageRepository.getById(messageId);
    final var certificate = certificateRepository.getById(message.certificateId());
    if (!certificate.allowTo(CertificateActionType.RECEIVE_ANSWER, Optional.empty())) {
      throw new CertificateActionForbidden(
          "Not allowed to receive answer on certificate for %s".formatted(certificate.id().id()),
          certificate.reasonNotAllowed(CertificateActionType.RECEIVE_ANSWER, Optional.empty()));
    }

    if (!certificate.isCertificateIssuedOnPatient(message.personId())) {
      throw new CertificateActionForbidden(
          "Not allowed to receive answer on certificate for %s, because patient is not matching"
              .formatted(certificate.id().id()),
          List.of("Different patient on certificate and incoming message!"));
    }

    message.answer(answer);

    return messageRepository.save(message);
  }
}
