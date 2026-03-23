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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateEventDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateEventTypeDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateEvent;

@Component
public class CertificateEventConverter {

  public CertificateEventDTO convert(CertificateEvent event) {
    return CertificateEventDTO.builder()
        .certificateId(event.certificateId().id())
        .relatedCertificateId(
            event.relatedCertificateId() != null ? event.relatedCertificateId().id() : null)
        .relatedCertificateStatus(
            event.relatedCertificateStatus() != null
                ? CertificateStatusTypeDTO.toType(event.relatedCertificateStatus())
                : null)
        .timestamp(event.timestamp())
        .type(CertificateEventTypeDTO.valueOf(event.type().name()))
        .build();
  }
}
