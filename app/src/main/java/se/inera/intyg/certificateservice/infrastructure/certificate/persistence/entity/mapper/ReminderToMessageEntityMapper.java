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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.message.model.Reminder;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageStatusEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageStatusEnum;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageTypeEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageTypeEnum;

@Component
@RequiredArgsConstructor
public class ReminderToMessageEntityMapper {

  public MessageEntity toEntity(MessageEntity messageEntity, Reminder reminder) {
    return MessageEntity.builder()
        .id(UUID.randomUUID().toString())
        .subject(reminder.subject().subject())
        .content(reminder.content().content())
        .author(reminder.author().author())
        .created(
            reminder.created() == null
                ? LocalDateTime.now(ZoneId.systemDefault())
                : reminder.created())
        .modified(
            reminder.created() == null
                ? LocalDateTime.now(ZoneId.systemDefault())
                : reminder.created())
        .sent(reminder.sent())
        .status(
            MessageStatusEntity.builder()
                .key(MessageStatusEnum.SENT.getKey())
                .status(MessageStatusEnum.SENT.name())
                .build())
        .messageType(
            MessageTypeEntity.builder()
                .key(MessageTypeEnum.REMINDER.getKey())
                .type(MessageTypeEnum.REMINDER.name())
                .build())
        .certificate(messageEntity.getCertificate())
        .forwarded(false)
        .build();
  }
}
