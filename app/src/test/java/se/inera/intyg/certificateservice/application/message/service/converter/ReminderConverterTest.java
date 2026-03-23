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
package se.inera.intyg.certificateservice.application.message.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessage.REMINDER;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessageConstants.AUTHOR_INCOMING_MESSAGE_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessageConstants.REMINDER_CONTENT;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessageConstants.REMINDER_MESSAGE_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessageConstants.SENT;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReminderConverterTest {

  @InjectMocks private ReminderConverter reminderConverter;

  @Test
  void shallIncludeId() {
    assertEquals(REMINDER_MESSAGE_ID, reminderConverter.convert(REMINDER).getId());
  }

  @Test
  void shallIncludeAuthor() {
    assertEquals(AUTHOR_INCOMING_MESSAGE_NAME, reminderConverter.convert(REMINDER).getAuthor());
  }

  @Test
  void shallIncludeSent() {
    assertEquals(SENT, reminderConverter.convert(REMINDER).getSent());
  }

  @Test
  void shallIncludeMessage() {
    assertEquals(REMINDER_CONTENT, reminderConverter.convert(REMINDER).getMessage());
  }
}
