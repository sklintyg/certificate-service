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
import se.inera.intyg.certificateservice.domain.message.model.Answer;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.StaffRepository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageContactInfoEmbeddable;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageStatusEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageStatusEnum;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageTypeEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageTypeEnum;

@Component
@RequiredArgsConstructor
public class AnswerToMessageEntityMapper {

  private final StaffRepository staffEntityRepository;

  public MessageEntity toEntity(MessageEntity messageEntity, Answer answer, Integer key) {
    return MessageEntity.builder()
        .key(key)
        .id(answer.id() != null ? answer.id().id() : UUID.randomUUID().toString())
        .reference(answer.reference() != null ? answer.reference().reference() : null)
        .subject(answer.subject().subject())
        .content(answer.content().content())
        .author(answer.author().author())
        .created(
            answer.created() == null ? LocalDateTime.now(ZoneId.systemDefault()) : answer.created())
        .modified(
            answer.modified() == null
                ? LocalDateTime.now(ZoneId.systemDefault())
                : answer.modified())
        .sent(answer.sent())
        .status(
            MessageStatusEntity.builder()
                .key(MessageStatusEnum.valueOf(answer.status().name()).getKey())
                .status(MessageStatusEnum.valueOf(answer.status().name()).name())
                .build())
        .messageType(
            MessageTypeEntity.builder()
                .key(MessageTypeEnum.ANSWER.getKey())
                .type(MessageTypeEnum.ANSWER.name())
                .build())
        .authoredByStaff(
            answer.authoredStaff() != null
                ? staffEntityRepository.staff(answer.authoredStaff())
                : null)
        .contactInfo(
            answer.contactInfo() != null
                ? answer.contactInfo().lines().stream()
                    .map(info -> MessageContactInfoEmbeddable.builder().info(info).build())
                    .toList()
                : null)
        .certificate(messageEntity.getCertificate())
        .forwarded(messageEntity.isForwarded())
        .build();
  }
}
