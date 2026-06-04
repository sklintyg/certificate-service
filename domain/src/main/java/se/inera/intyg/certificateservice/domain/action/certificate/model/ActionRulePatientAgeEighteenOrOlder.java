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
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ActionRuleContentProvider;

public class ActionRulePatientAgeEighteenOrOlder implements ActionRule {

  private final ActionRuleContentProvider contentProvider;

  public ActionRulePatientAgeEighteenOrOlder(ActionRuleContentProvider contentProvider) {
    this.contentProvider = contentProvider;
  }

  @Override
  public boolean evaluate(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    final var patient = actionEvaluation.map(ActionEvaluation::patient).orElse(null);

    if (patient == null) {
      throw new IllegalArgumentException("Action evaluation is missing required field patient");
    }

    return patient.getAge() >= 18;
  }

  @Override
  public String getReasonForPermissionDenied() {
    return Optional.ofNullable(contentProvider)
        .map(ActionRuleContentProvider::getReasonForPermissionDenied)
        .orElse(
            "Du saknar behörighet för den begärda åtgärden."
                + " För att utföra denna uppgift krävs särskilda rättigheter eller en specifik befattning.");
  }

  @Override
  public Optional<String> message(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    if (evaluate(certificate, actionEvaluation)) {
      return Optional.empty();
    }
    return Optional.of(getReasonForPermissionDenied());
  }
}
