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

import static org.springframework.data.jpa.domain.Specification.unrestricted;
import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.MessageEntitySpecification.equalsAuthor;
import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.MessageEntitySpecification.equalsForwarded;
import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.MessageEntitySpecification.sentEqualsAndGreaterThan;
import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.MessageEntitySpecification.sentEqualsAndLesserThan;
import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.PatientEntitySpecification.equalsPatientForMessage;
import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.StaffEntitySpecification.equalsIssuedByStaffForMessage;
import static se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.UnitEntitySpecification.inIssuedOnUnitIds;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.common.model.MessagesRequest;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageEntity;

@Component
public class MessageEntitySpecificationFactory {

  public Specification<MessageEntity> create(MessagesRequest request) {
    Specification<MessageEntity> specification = unrestricted();
    if (request.sentDateFrom() != null) {
      specification = specification.and(sentEqualsAndGreaterThan(request.sentDateFrom()));
    }

    if (request.sentDateTo() != null) {
      specification = specification.and(sentEqualsAndLesserThan(request.sentDateTo()));
    }

    if (request.issuedByStaffId() != null) {
      specification = specification.and(equalsIssuedByStaffForMessage(request.issuedByStaffId()));
    }

    if (request.issuedOnUnitIds() != null && !request.issuedOnUnitIds().isEmpty()) {
      specification = specification.and(inIssuedOnUnitIds(request.issuedOnUnitIds()));
    }

    if (request.personId() != null) {
      specification = specification.and(equalsPatientForMessage(request.personId()));
    }

    if (request.forwarded() != null) {
      specification = specification.and(equalsForwarded(request.forwarded()));
    }

    if (request.author() != null) {
      specification = specification.and(equalsAuthor(request.author()));
    }

    return specification;
  }
}
