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
package se.inera.intyg.certificateservice.domain.message.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import se.inera.intyg.certificateservice.domain.staff.model.Staff;

@Getter
@Builder
@EqualsAndHashCode
public class Answer {

  private MessageId id;
  private SenderReference reference;
  private MessageType type;
  private Subject subject;
  private Content content;
  private Author author;
  private LocalDateTime created;
  private LocalDateTime modified;
  private LocalDateTime sent;
  private MessageStatus status;
  private MessageContactInfo contactInfo;
  private Staff authoredStaff;

  public void send(Staff staff, Content content) {
    this.status = MessageStatus.HANDLED;
    this.content = content;
    this.authoredStaff = staff;
    this.sent = LocalDateTime.now();
    this.author = new Author(staff.name().fullName());
  }

  public void delete() {
    this.status = MessageStatus.DELETED_DRAFT;
  }

  public void save(Staff staff, Content content) {
    this.content = content;
    this.authoredStaff = staff;
    this.author = new Author(staff.name().fullName());
    this.modified = LocalDateTime.now();
  }

  public boolean isDraft() {
    return MessageStatus.DRAFT.equals(this.status);
  }
}
