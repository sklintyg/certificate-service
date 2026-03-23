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

import java.util.List;
import java.util.Optional;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.common.model.Role;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;

public class ActionRuleProtectedPerson implements ActionRule {

  private final List<Role> allowedRoles;

  public ActionRuleProtectedPerson(List<Role> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

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
                        value ->
                            ifPatientIsProtectedUserMustHaveAllowedRoleAndBeWithinCareUnit(
                                evaluation.user().role(),
                                value.certificateMetaData().patient(),
                                value,
                                evaluation))
                    .isPresent())
        .isPresent();
  }

  @Override
  public String getReasonForPermissionDenied() {
    return "Du saknar behörighet för den begärda åtgärden."
        + " Det krävs särskilda rättigheter eller en specifik befattning"
        + " för att hantera patienter med skyddade personuppgifter.";
  }

  private boolean evaluate(Optional<ActionEvaluation> actionEvaluation) {
    return actionEvaluation
        .filter(
            evaluation ->
                ifPatientIsProtectedUserMustHaveAllowedRole(
                    evaluation.user().role(), evaluation.patient()))
        .isPresent();
  }

  private boolean ifPatientIsProtectedUserMustHaveAllowedRole(Role role, Patient patient) {
    return !patient.protectedPerson().value() || allowedRoles.contains(role);
  }

  private boolean ifPatientIsProtectedUserMustHaveAllowedRoleAndBeWithinCareUnit(
      Role role, Patient patient, Certificate certificate, ActionEvaluation actionEvaluation) {
    if (!patient.protectedPerson().value()) {
      return true;
    }

    return allowedRoles.contains(role) && certificate.isWithinCareUnit(actionEvaluation);
  }
}
