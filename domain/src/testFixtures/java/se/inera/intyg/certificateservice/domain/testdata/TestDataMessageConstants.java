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
package se.inera.intyg.certificateservice.domain.testdata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class TestDataMessageConstants {

  public static final String MESSAGE_ID = "MESSAGE_ID";
  public static final String REFERENCE_ID = "REFERENCE_ID";

  public static final String ANSWER_ID = "ANSWER_ID";
  public static final String ANSWER_REFERENCE_ID = "ANSWER_REFERENCE_ID";

  public static final String REMINDER_MESSAGE_ID = "REMINDER_MESSAGE_ID";
  public static final String ANSWER_MESSAGE_ID = "ANSWER_MESSAGE_ID";
  public static final String REMINDER_REFERENCE_ID = "REMINDER_REFERENCE_ID";

  public static final String SUBJECT = "This is the subject";
  public static final String CONTENT = "This is the content of the message";
  public static final String REMINDER_CONTENT = "This is a reminder";
  public static final LocalDateTime SENT = LocalDateTime.now(ZoneId.systemDefault());
  public static final LocalDateTime CREATED_AFTER_SENT = SENT.plusDays(1);
  public static final List<String> CONTACT_INFO =
      List.of("Contact info 1", "Contact info 2", "Contact info 3");
  public static final LocalDate LAST_DATE_TO_REPLY =
      LocalDate.now(ZoneId.systemDefault()).plusDays(10);
  public static final String COMPLEMENT_QUESTION_ID_ONE = "55";
  public static final String COMPLEMENT_FIELD_ID_ONE = "55.1";
  public static final Integer INSTANCE_ONE = 0;
  public static final String COMPLEMENT_TEXT_ONE = "This is text for the first question";
  public static final String AUTHOR_INCOMING_MESSAGE = "FK";
  public static final String AUTHOR_INCOMING_MESSAGE_NAME = "Försäkringskassan";
}
