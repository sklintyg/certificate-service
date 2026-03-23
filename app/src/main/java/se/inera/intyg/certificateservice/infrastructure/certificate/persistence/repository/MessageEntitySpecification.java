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

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import se.inera.intyg.certificateservice.domain.message.model.Author;
import se.inera.intyg.certificateservice.domain.message.model.Forwarded;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageEntity;

public class MessageEntitySpecification {

  private MessageEntitySpecification() {}

  public static Specification<MessageEntity> sentEqualsAndGreaterThan(LocalDateTime from) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get("sent"), from.toLocalDate().atStartOfDay());
  }

  public static Specification<MessageEntity> sentEqualsAndLesserThan(LocalDateTime to) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.lessThanOrEqualTo(
            root.get("sent"), to.toLocalDate().plusDays(1).atStartOfDay());
  }

  public static Specification<MessageEntity> equalsForwarded(Forwarded forwarded) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("forwarded"), forwarded.value());
  }

  public static Specification<MessageEntity> equalsAuthor(Author author) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("author"), author.author());
  }
}
