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
package se.inera.intyg.certificateservice.domain.certificate.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.exception.CertificateActionForbidden;

@RequiredArgsConstructor
public class ForwardCertificateDomainService {

  private final CertificateRepository certificateRepository;

  public Certificate forward(Certificate certificate, ActionEvaluation actionEvaluation) {
    if (!certificate.allowTo(
            CertificateActionType.FORWARD_CERTIFICATE, Optional.of(actionEvaluation))
        && !certificate.allowTo(
            CertificateActionType.FORWARD_CERTIFICATE_FROM_LIST, Optional.of(actionEvaluation))) {
      throw new CertificateActionForbidden(
          "Not allowed to forward certificate %s".formatted(certificate.id()),
          certificate.reasonNotAllowed(
              CertificateActionType.FORWARD_CERTIFICATE, Optional.of(actionEvaluation)));
    }

    certificate.forward();

    return certificateRepository.save(certificate);
  }
}
