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
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateMessageTypeDTO;
import se.inera.intyg.certificateservice.application.message.dto.QuestionTypeDTO;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateMessageType;

@Component
public class CertificateMessageTypeConverter {

  public CertificateMessageTypeDTO convert(CertificateMessageType certificateMessageType) {
    return CertificateMessageTypeDTO.builder()
        .type(QuestionTypeDTO.valueOf(certificateMessageType.type().name()))
        .subject(certificateMessageType.subject().subject())
        .build();
  }
}
