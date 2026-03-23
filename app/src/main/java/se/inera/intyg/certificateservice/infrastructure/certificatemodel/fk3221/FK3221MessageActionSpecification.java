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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3221;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageActionSpecification;
import se.inera.intyg.certificateservice.domain.message.model.MessageActionType;

public class FK3221MessageActionSpecification {

  private FK3221MessageActionSpecification() {
    throw new IllegalStateException("Utility class");
  }

  public static List<MessageActionSpecification> create() {
    return List.of(
        MessageActionSpecification.builder().messageActionType(MessageActionType.ANSWER).build(),
        MessageActionSpecification.builder()
            .messageActionType(MessageActionType.HANDLE_COMPLEMENT)
            .build(),
        MessageActionSpecification.builder()
            .messageActionType(MessageActionType.COMPLEMENT)
            .build(),
        MessageActionSpecification.builder()
            .messageActionType(MessageActionType.CANNOT_COMPLEMENT)
            .build(),
        MessageActionSpecification.builder().messageActionType(MessageActionType.FORWARD).build(),
        MessageActionSpecification.builder()
            .messageActionType(MessageActionType.HANDLE_MESSAGE)
            .build());
  }
}
