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
package se.inera.intyg.certificateservice.infrastructure.certificateaction;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModelId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateType;
import se.inera.intyg.certificateservice.domain.certificatemodel.repository.CertificateActionConfigurationRepository;
import se.inera.intyg.certificateservice.domain.configuration.limitedcertificatefunctionality.dto.LimitedCertificateFunctionalityConfiguration;
import se.inera.intyg.certificateservice.domain.configuration.unitaccess.dto.CertificateAccessConfiguration;
import se.inera.intyg.certificateservice.infrastructure.configuration.GetLimitedCertificateFunctionalityConfiguration;
import se.inera.intyg.certificateservice.infrastructure.configuration.UnitAccessConfiguration;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryCertificateActionConfigurationRepository
    implements CertificateActionConfigurationRepository {

  private final UnitAccessConfiguration unitAccessConfiguration;
  private final GetLimitedCertificateFunctionalityConfiguration
      getLimitedCertificateFunctionalityConfiguration;

  @Override
  public List<CertificateAccessConfiguration> findAccessConfiguration(
      CertificateType certificateType) {
    final var certificateAccessConfigurations = unitAccessConfiguration.get();

    if (certificateAccessConfigurations.isEmpty()) {
      return Collections.emptyList();
    }

    return certificateAccessConfigurations.stream()
        .filter(configuration -> configuration.certificateType().equals(certificateType.type()))
        .toList();
  }

  @Override
  public LimitedCertificateFunctionalityConfiguration
      findLimitedCertificateFunctionalityConfiguration(CertificateModelId certificateModelId) {
    final var limitedCertificateFunctionalityConfigurations =
        getLimitedCertificateFunctionalityConfiguration.get();
    if (limitedCertificateFunctionalityConfigurations.isEmpty()) {
      return null;
    }

    return limitedCertificateFunctionalityConfigurations.stream()
        .filter(config -> certificateModelId.matches(config.certificateType(), config.version()))
        .findFirst()
        .orElse(null);
  }
}
