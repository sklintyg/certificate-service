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
package se.inera.intyg.certificateservice.application.common;

import java.util.Collections;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.unit.dto.MessagesQueryCriteriaDTO;
import se.inera.intyg.certificateservice.application.unit.dto.QuestionSenderTypeDTO;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;
import se.inera.intyg.certificateservice.domain.common.model.MessagesRequest;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.common.model.PersonIdType;
import se.inera.intyg.certificateservice.domain.message.model.Author;
import se.inera.intyg.certificateservice.domain.message.model.Forwarded;

@Component
public class MessagesRequestFactory {

  public MessagesRequest create() {
    return MessagesRequest.builder().build();
  }

  public MessagesRequest create(MessagesQueryCriteriaDTO queryCriteria) {
    return MessagesRequest.builder()
        .sentDateFrom(queryCriteria.getSentDateFrom())
        .sentDateTo(queryCriteria.getSentDateTo())
        .personId(
            queryCriteria.getPatientId() != null
                ? PersonId.builder()
                    .id(queryCriteria.getPatientId().getId())
                    .type(PersonIdType.valueOf(queryCriteria.getPatientId().getType()))
                    .build()
                : null)
        .author(
            queryCriteria.getSenderType() != null
                    && queryCriteria.getSenderType() != QuestionSenderTypeDTO.SHOW_ALL
                ? new Author(queryCriteria.getSenderType().name())
                : null)
        .issuedOnUnitIds(
            queryCriteria.getIssuedOnUnitIds() != null
                ? queryCriteria.getIssuedOnUnitIds().stream().map(HsaId::new).toList()
                : Collections.emptyList())
        .forwarded(
            queryCriteria.getForwarded() != null
                ? new Forwarded(queryCriteria.getForwarded())
                : null)
        .issuedByStaffId(
            queryCriteria.getIssuedByStaffId() != null
                ? new HsaId(queryCriteria.getIssuedByStaffId())
                : null)
        .build();
  }
}
