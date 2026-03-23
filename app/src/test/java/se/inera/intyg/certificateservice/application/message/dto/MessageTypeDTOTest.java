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
package se.inera.intyg.certificateservice.application.message.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.application.message.dto.MessageTypeDTO.AVSTMN;
import static se.inera.intyg.certificateservice.application.message.dto.MessageTypeDTO.KOMPLT;
import static se.inera.intyg.certificateservice.application.message.dto.MessageTypeDTO.KONTKT;
import static se.inera.intyg.certificateservice.application.message.dto.MessageTypeDTO.OVRIGT;
import static se.inera.intyg.certificateservice.application.message.dto.MessageTypeDTO.PAMINN;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.message.model.MessageType;

class MessageTypeDTOTest {

  @Test
  void shallConvertToContact() {
    assertEquals(MessageType.CONTACT, KONTKT.toMessageType());
  }

  @Test
  void shallConvertToComplement() {
    assertEquals(MessageType.COMPLEMENT, KOMPLT.toMessageType());
  }

  @Test
  void shallConvertToReminder() {
    assertEquals(MessageType.REMINDER, PAMINN.toMessageType());
  }

  @Test
  void shallConvertToOther() {
    assertEquals(MessageType.OTHER, OVRIGT.toMessageType());
  }

  @Test
  void shouldConvertToMeeting() {
    assertEquals(MessageType.COORDINATION, AVSTMN.toMessageType());
  }
}
