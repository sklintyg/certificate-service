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
package se.inera.intyg.certificateservice.domain.unit.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.model.MessagesRequest;
import se.inera.intyg.certificateservice.domain.common.model.MessagesResponse;
import se.inera.intyg.certificateservice.domain.message.model.Message;
import se.inera.intyg.certificateservice.domain.message.repository.MessageRepository;

@RequiredArgsConstructor
public class GetUnitMessagesDomainService {

  private final MessageRepository messageRepository;
  private final CertificateRepository certificateRepository;

  public MessagesResponse get(MessagesRequest request, ActionEvaluation actionEvaluation) {
    final var messages = messageRepository.findByMessagesRequest(request).stream().toList();

    if (messages.isEmpty()) {
      return MessagesResponse.builder().build();
    }

    final var certificates =
        certificateRepository
            .getByIds(messages.stream().map(Message::certificateId).distinct().toList())
            .stream()
            .filter(
                certificate ->
                    certificate.allowTo(CertificateActionType.READ, Optional.of(actionEvaluation)))
            .toList();

    final var filteredMessagesBasedOnActionEvaluation =
        messages.stream()
            .filter(
                message ->
                    certificates.stream()
                        .anyMatch(
                            certificate ->
                                certificate.id().id().equals(message.certificateId().id())))
            .toList();

    return MessagesResponse.builder()
        .messages(filteredMessagesBasedOnActionEvaluation)
        .certificates(certificates)
        .build();
  }
}
