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
import se.inera.intyg.certificateservice.domain.common.model.AccessScope;

public class ActionRuleWithinAccessScope implements ActionRule {

  private final AccessScope accessScope;

  public ActionRuleWithinAccessScope(AccessScope accessScope) {
    this.accessScope = accessScope;
  }

  @Override
  public boolean evaluate(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    if (actionEvaluation.isEmpty()) {
      return false;
    }

    final var scope = getStrictestAccessScope(actionEvaluation.get());

    switch (scope) {
      case WITHIN_CARE_UNIT -> {
        return certificate
            .filter(value -> value.isWithinCareUnit(actionEvaluation.get()))
            .isPresent();
      }
      case WITHIN_CARE_PROVIDER -> {
        return certificate
            .filter(value -> value.isWithinCareProvider(actionEvaluation.get()))
            .isPresent();
      }
      case ALL_CARE_PROVIDERS -> {
        return true;
      }
    }

    return false;
  }

  private AccessScope getStrictestAccessScope(ActionEvaluation actionEvaluation) {
    final var userAccessScope = actionEvaluation.user().accessScope();

    if (accessScope == null || userAccessScope == null) {
      return AccessScope.WITHIN_CARE_UNIT;
    }

    if (accessScope == AccessScope.WITHIN_CARE_UNIT
        || userAccessScope == AccessScope.WITHIN_CARE_UNIT) {
      return AccessScope.WITHIN_CARE_UNIT;
    }

    if (accessScope == AccessScope.WITHIN_CARE_PROVIDER
        || userAccessScope == AccessScope.WITHIN_CARE_PROVIDER) {
      return AccessScope.WITHIN_CARE_PROVIDER;
    }

    return AccessScope.ALL_CARE_PROVIDERS;
  }
}
