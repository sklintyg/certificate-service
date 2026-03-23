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
package se.inera.intyg.certificateservice.application.common.converter;

import java.util.Optional;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.common.dto.ResourceLinkDTO;
import se.inera.intyg.certificateservice.application.common.dto.ResourceLinkTypeDTO;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateAction;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;

@Component
public class ResourceLinkConverter {

  public ResourceLinkDTO convert(
      CertificateAction certificateAction,
      Optional<Certificate> certificate,
      ActionEvaluation actionEvaluation) {
    return ResourceLinkDTO.builder()
        .type(ResourceLinkTypeDTO.toResourceLinkType(certificateAction.getType()))
        .name(certificateAction.getName(certificate))
        .description(certificateAction.getDescription(certificate))
        .body(certificateAction.getBody(certificate, Optional.of(actionEvaluation)))
        .enabled(certificateAction.isEnabled(certificate, Optional.of(actionEvaluation)))
        .build();
  }
}
