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
package se.inera.intyg.certificateservice.application.unit.service;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.unit.dto.UnitStatisticsDTO;
import se.inera.intyg.certificateservice.application.unit.dto.UnitStatisticsRequest;
import se.inera.intyg.certificateservice.application.unit.dto.UnitStatisticsResponse;
import se.inera.intyg.certificateservice.application.unit.service.validator.UnitStatisticsRequestValidator;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;
import se.inera.intyg.certificateservice.domain.unit.service.GetUnitStatisticsDomainService;

@Service
@RequiredArgsConstructor
public class GetUnitStatisticsService {

  private final UnitStatisticsRequestValidator unitStatisticsRequestValidator;
  private final GetUnitStatisticsDomainService getUnitStatisticsDomainService;

  public UnitStatisticsResponse get(UnitStatisticsRequest request) {
    unitStatisticsRequestValidator.validate(request);

    final var statisticsMap =
        getUnitStatisticsDomainService.get(
            request.getUser().getRole().toRole(),
            request.getIssuedByUnitIds().stream().map(HsaId::new).toList());

    return UnitStatisticsResponse.builder()
        .unitStatistics(
            statisticsMap.entrySet().stream()
                .collect(
                    Collectors.toMap(
                        entry -> entry.getKey().id(),
                        entry ->
                            UnitStatisticsDTO.builder()
                                .draftCount(entry.getValue().certificateCount())
                                .unhandledMessageCount(entry.getValue().messageCount())
                                .build())))
        .build();
  }
}
