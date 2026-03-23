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
package se.inera.intyg.certificateservice.domain.action.certificate.model;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.repository.CertificateActionConfigurationRepository;

@RequiredArgsConstructor
public class ActionRuleLimitedCertificateFunctionality implements ActionRule {

  private final CertificateActionConfigurationRepository certificateActionConfigurationRepository;
  private final CertificateActionType certificateActionType;

  @Override
  public boolean evaluate(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    if (certificate.isEmpty()) {
      return false;
    }

    final var evaluatedCertificate = certificate.get();
    if (evaluatedCertificate.certificateModel().isLastestActiveVersion()) {
      return true;
    }

    final var limitedCertificateFunctionalityConfiguration =
        certificateActionConfigurationRepository.findLimitedCertificateFunctionalityConfiguration(
            evaluatedCertificate.certificateModel().id());

    if (limitedCertificateFunctionalityConfiguration == null) {
      return true;
    }

    return limitedCertificateFunctionalityConfiguration.configuration().actions().stream()
        .filter(
            limitedActionConfiguration ->
                limitedActionConfiguration.type().equals(certificateActionType.name()))
        .map(config -> LocalDateTime.now().isBefore(config.untilDateTime()))
        .findFirst()
        .orElse(false);
  }
}
