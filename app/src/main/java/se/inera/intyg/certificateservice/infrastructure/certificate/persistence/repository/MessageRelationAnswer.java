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

import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageRelationType.ANSWER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.message.model.Message;
import se.inera.intyg.certificateservice.domain.message.model.MessageStatus;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageRelationEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageRelationTypeEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper.AnswerToMessageEntityMapper;

@Component
@RequiredArgsConstructor
public class MessageRelationAnswer implements MessageRelation {

  private final MessageRelationEntityRepository messageRelationEntityRepository;
  private final MessageEntityRepository messageEntityRepository;
  private final AnswerToMessageEntityMapper answerToMessageEntityMapper;

  @Override
  public void save(Message message, MessageEntity messageEntity) {
    if (message.answer() == null) {
      return;
    }

    if (message.answer().status() == MessageStatus.DELETED_DRAFT) {
      deleteMessageAndRelations(message, messageEntity);
      return;
    }

    final var savedAnswer =
        messageEntityRepository.save(
            answerToMessageEntityMapper.toEntity(
                messageEntity,
                message.answer(),
                messageEntityRepository
                    .findMessageEntityById(message.answer().id().id())
                    .map(MessageEntity::getKey)
                    .orElse(null)));

    final var messageRelations = messageRelationEntityRepository.findByParentMessage(messageEntity);

    if (messageRelations.isEmpty() || isMissingAnswer(messageRelations)) {
      messageRelationEntityRepository.save(
          MessageRelationEntity.builder()
              .childMessage(savedAnswer)
              .parentMessage(messageEntity)
              .messageRelationType(
                  MessageRelationTypeEntity.builder()
                      .key(ANSWER.getKey())
                      .type(ANSWER.name())
                      .build())
              .build());
    }
  }

  private static boolean isMissingAnswer(List<MessageRelationEntity> messageRelations) {
    return messageRelations.stream()
        .noneMatch(
            messageRelationEntity ->
                messageRelationEntity.getMessageRelationType().getType().equals(ANSWER.name()));
  }

  private void deleteMessageAndRelations(Message message, MessageEntity messageEntity) {
    messageRelationEntityRepository.findByParentMessage(messageEntity).stream()
        .filter(entity -> entity.getChildMessage().getId().equals(message.answer().id().id()))
        .findFirst()
        .ifPresent(messageRelationEntityRepository::delete);

    messageEntityRepository
        .findMessageEntityById(message.answer().id().id())
        .ifPresent(messageEntityRepository::delete);
  }
}
