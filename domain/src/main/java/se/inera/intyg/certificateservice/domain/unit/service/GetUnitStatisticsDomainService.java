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
package se.inera.intyg.certificateservice.domain.unit.service;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificate.repository.StatisticsRepository;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.repository.CertificateModelRepository;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;
import se.inera.intyg.certificateservice.domain.common.model.Role;
import se.inera.intyg.certificateservice.domain.unit.model.UnitStatistics;

@RequiredArgsConstructor
public class GetUnitStatisticsDomainService {

  private final StatisticsRepository statisticsRepository;
  private final CertificateModelRepository certificateModelRepository;

  public Map<HsaId, UnitStatistics> get(Role userRole, List<HsaId> issuedByUnitIds) {
    final var typesToViewProtectedPersons =
        certificateModelRepository.findAllActive().stream()
            .filter(
                certificateModel ->
                    certificateModel
                        .certificateAction(CertificateActionType.READ)
                        .map(
                            actionSpecification ->
                                actionSpecification
                                    .allowedRolesForProtectedPersons()
                                    .contains(userRole))
                        .orElse(false))
            .map(CertificateModel::id)
            .toList();

    return statisticsRepository.getStatisticsForUnits(issuedByUnitIds, typesToViewProtectedPersons);
  }
}
