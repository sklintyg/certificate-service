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
package se.inera.intyg.certificateservice.application.message.service.converter;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateRelationDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateRelationTypeDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Relation;

@Component
@RequiredArgsConstructor
public class CertificateRelationConverter {

  public CertificateRelationDTO convert(Optional<Relation> optionalRelation) {
    return optionalRelation
        .map(
            relation ->
                CertificateRelationDTO.builder()
                    .certificateId(relation.certificate().id().id())
                    .status(CertificateStatusTypeDTO.toType(relation.certificate().status()))
                    .created(relation.created())
                    .type(CertificateRelationTypeDTO.toType(relation.type()))
                    .build())
        .orElse(null);
  }
}
