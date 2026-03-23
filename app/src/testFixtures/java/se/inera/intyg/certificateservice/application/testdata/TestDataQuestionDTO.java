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
package se.inera.intyg.certificateservice.application.testdata;

import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateConstants.CERTIFICATE_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessageConstants.CONTENT;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataMessageConstants.MESSAGE_ID;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import se.inera.intyg.certificateservice.application.common.dto.ResourceLinkDTO;
import se.inera.intyg.certificateservice.application.common.dto.ResourceLinkTypeDTO;
import se.inera.intyg.certificateservice.application.message.dto.QuestionDTO;
import se.inera.intyg.certificateservice.application.message.dto.QuestionDTO.QuestionDTOBuilder;
import se.inera.intyg.certificateservice.application.message.dto.QuestionTypeDTO;

public class TestDataQuestionDTO {

  private TestDataQuestionDTO() {
    throw new IllegalStateException("Utility class");
  }

  public static final QuestionDTO QUESTION_DTO_BUILDER = questionDTOBuilder().build();

  public static QuestionDTOBuilder questionDTOBuilder() {
    return QuestionDTO.builder()
        .id(MESSAGE_ID)
        .type(QuestionTypeDTO.CONTACT)
        .subject(QuestionTypeDTO.CONTACT.name())
        .message(CONTENT)
        .author(TestDataStaffEntity.ajlaDoctorEntityBuilder().build().getFirstName())
        .sent(null)
        .complements(Collections.emptyList())
        .isHandled(false)
        .isForwarded(false)
        .answer(null)
        .answeredByCertificate(null)
        .reminders(Collections.emptyList())
        .lastUpdate(LocalDateTime.now(ZoneId.systemDefault()))
        .lastDateToReply(null)
        .contactInfo(Collections.emptyList())
        .certificateId(CERTIFICATE_ID)
        .links(
            List.of(
                ResourceLinkDTO.builder()
                    .type(ResourceLinkTypeDTO.HANDLE_QUESTION)
                    .name("Hantera")
                    .description("Hantera fråga")
                    .body(null)
                    .enabled(true)
                    .title(null)
                    .build()));
  }
}
