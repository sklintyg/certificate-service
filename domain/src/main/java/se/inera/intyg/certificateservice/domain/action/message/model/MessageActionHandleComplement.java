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
package se.inera.intyg.certificateservice.domain.action.message.model;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateAction;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageActionSpecification;
import se.inera.intyg.certificateservice.domain.message.model.Message;
import se.inera.intyg.certificateservice.domain.message.model.MessageActionLink;
import se.inera.intyg.certificateservice.domain.message.model.MessageActionType;

@Builder
@Getter(AccessLevel.NONE)
public class MessageActionHandleComplement implements MessageAction {

  private final MessageActionSpecification specification;

  @Override
  public MessageActionType type() {
    return specification.messageActionType();
  }

  @Override
  public boolean evaluate(List<CertificateAction> certificateActions, Message message) {
    if (!message.actionAvailable(CertificateActionType.HANDLE_COMPLEMENT, certificateActions)) {
      return false;
    }

    return message.isUnhandledComplement();
  }

  @Override
  public MessageActionLink actionLink() {
    return MessageActionLink.builder()
        .type(MessageActionType.HANDLE_COMPLEMENT)
        .name("Hantera")
        .description("Hantera kompletteringsbegäran.")
        .enabled(true)
        .build();
  }
}
