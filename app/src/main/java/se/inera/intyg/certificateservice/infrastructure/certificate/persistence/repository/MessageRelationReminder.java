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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository;

import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageRelationType.REMINDER;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.message.model.Message;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageRelationEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageRelationTypeEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper.ReminderToMessageEntityMapper;

@Component
@RequiredArgsConstructor
public class MessageRelationReminder implements MessageRelation {

  private final MessageRelationEntityRepository messageRelationEntityRepository;
  private final MessageEntityRepository messageEntityRepository;
  private final ReminderToMessageEntityMapper reminderToMessageEntityMapper;

  @Override
  public void save(Message message, MessageEntity messageEntity) {
    if (message.reminders().isEmpty()) {
      return;
    }

    message
        .reminders()
        .forEach(
            reminder -> {
              final var entity = messageEntityRepository.findMessageEntityById(reminder.id().id());
              if (entity.isEmpty()) {
                final var savedReminder =
                    messageEntityRepository.save(
                        reminderToMessageEntityMapper.toEntity(messageEntity, reminder));

                messageRelationEntityRepository.save(
                    MessageRelationEntity.builder()
                        .childMessage(savedReminder)
                        .parentMessage(messageEntity)
                        .messageRelationType(
                            MessageRelationTypeEntity.builder()
                                .key(REMINDER.getKey())
                                .type(REMINDER.name())
                                .build())
                        .build());
              }
            });
  }
}
