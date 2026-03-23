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
package se.inera.intyg.certificateservice.infrastructure.diagnosiscode.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateservice.domain.diagnosiscode.model.Diagnosis;
import se.inera.intyg.certificateservice.domain.diagnosiscode.model.DiagnosisCode;
import se.inera.intyg.certificateservice.domain.diagnosiscode.repository.DiagnosisCodeRepository;
import se.inera.intyg.certificateservice.infrastructure.diagnosiscode.DiagnosisCodeProvider;

@Slf4j
@Repository
@RequiredArgsConstructor
public class InMemoryDiagnosisCodeRepositoy implements DiagnosisCodeRepository {

  private Map<DiagnosisCode, Diagnosis> diagnosesMap;
  private final List<DiagnosisCodeProvider> diagnosisCodeProviders;

  @Override
  public Optional<Diagnosis> findByCode(DiagnosisCode code) {
    if (!getDiagnosesMap().containsKey(code)) {
      return Optional.empty();
    }
    return Optional.of(getDiagnosesMap().get(code));
  }

  @Override
  public Diagnosis getByCode(DiagnosisCode code) {
    if (!getDiagnosesMap().containsKey(code)) {
      throw new IllegalArgumentException("Diagnosis value missing: %s".formatted(code.value()));
    }
    return getDiagnosesMap().get(code);
  }

  private Map<DiagnosisCode, Diagnosis> getDiagnosesMap() {
    if (diagnosesMap == null) {
      log.info("Initiate diagnosis value repository");
      diagnosesMap = new HashMap<>();
      diagnosisCodeProviders.forEach(
          diagnosisCodeProvider ->
              diagnosisCodeProvider
                  .get()
                  .forEach(diagnosis -> diagnosesMap.put(diagnosis.code(), diagnosis)));
      log.info("Loaded '{}' diagnoses to repository", diagnosesMap.size());
    }
    return diagnosesMap;
  }
}
