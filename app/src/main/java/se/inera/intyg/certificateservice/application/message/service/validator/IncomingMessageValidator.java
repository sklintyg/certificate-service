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
package se.inera.intyg.certificateservice.application.message.service.validator;

import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.message.dto.IncomingComplementDTO;
import se.inera.intyg.certificateservice.application.message.dto.IncomingMessageRequest;
import se.inera.intyg.certificateservice.application.message.dto.MessageTypeDTO;

@Component
public class IncomingMessageValidator {

  public void validate(IncomingMessageRequest incomingMessageRequest) {
    if (isNullOrBlank(incomingMessageRequest.getId())) {
      throw new IllegalArgumentException("Message.id is missing");
    }
    if (isNullOrBlank(incomingMessageRequest.getCertificateId())) {
      throw new IllegalArgumentException("Message.certificateId is missing");
    }
    if (isNullOrBlank(incomingMessageRequest.getContent())) {
      throw new IllegalArgumentException("Message.content is missing");
    }
    if (incomingMessageRequest.getSent() == null) {
      throw new IllegalArgumentException("Message.sent is missing");
    }
    if (incomingMessageRequest.getSentBy() == null) {
      throw new IllegalArgumentException("Message.sentBy is missing");
    }
    if (incomingMessageRequest.getContactInfo() == null) {
      throw new IllegalArgumentException("Message.contactInfo is missing");
    }
    if (incomingMessageRequest.getPersonId() == null) {
      throw new IllegalArgumentException("Required parameter missing: Message.personId");
    }
    if (isNullOrBlank(incomingMessageRequest.getPersonId().getId())) {
      throw new IllegalArgumentException("Required parameter missing: Message.personId.id");
    }
    if (incomingMessageRequest.getPersonId().getType() == null) {
      throw new IllegalArgumentException("Required parameter missing: Message.personId.type");
    }
    validateComplement(incomingMessageRequest);
    validateReminder(incomingMessageRequest);
  }

  private void validateReminder(IncomingMessageRequest incomingMessageRequest) {
    if (MessageTypeDTO.PAMINN.equals(incomingMessageRequest.getType())
        && (incomingMessageRequest.getReminderMessageId() == null
            || incomingMessageRequest.getReminderMessageId().isBlank())) {
      throw new IllegalArgumentException("Required parameter missing: Message.reminderMessageId");
    }
  }

  private void validateComplement(IncomingMessageRequest incomingMessageRequest) {
    if (MessageTypeDTO.KOMPLT.equals(incomingMessageRequest.getType())) {
      if (incomingMessageRequest.getComplements() == null) {
        throw new IllegalArgumentException("Required parameter missing: Message.complement");
      }
      if (complementMissingQuestionId(incomingMessageRequest.getComplements())) {
        throw new IllegalArgumentException(
            "Required parameter missing: Message.complement.questionId");
      }
      if (complementMissingText(incomingMessageRequest.getComplements())) {
        throw new IllegalArgumentException(
            "Required parameter missing: Message.complement.content");
      }
    }
  }

  private boolean complementMissingText(List<IncomingComplementDTO> complements) {
    return complements.stream().anyMatch(complement -> isNullOrBlank(complement.getContent()));
  }

  private boolean complementMissingQuestionId(List<IncomingComplementDTO> complements) {
    return complements.stream().anyMatch(complement -> isNullOrBlank(complement.getQuestionId()));
  }

  private static boolean isNullOrBlank(String incomingMessageRequest) {
    return incomingMessageRequest == null || incomingMessageRequest.isBlank();
  }
}
