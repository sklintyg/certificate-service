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

import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;

@Builder
@Getter(AccessLevel.NONE)
public class ActionRuleBlockTestIndicatedPerson implements ActionRule {

  @Override
  public boolean evaluate(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    if (certificate.isEmpty()) {
      return evaluate(actionEvaluation);
    }

    return actionEvaluation
        .filter(
            evaluation ->
                certificate
                    .filter(
                        value -> isPatientNotTestIndicated(value.certificateMetaData().patient()))
                    .isPresent())
        .isPresent();
  }

  @Override
  public String getReasonForPermissionDenied() {
    return "Åtgärden kan inte utföras för testpersoner.";
  }

  private static boolean evaluate(Optional<ActionEvaluation> actionEvaluation) {
    return actionEvaluation
        .filter(evaluation -> isPatientNotTestIndicated(evaluation.patient()))
        .isPresent();
  }

  private static boolean isPatientNotTestIndicated(Patient patient) {
    return !patient.testIndicated().value();
  }
}
