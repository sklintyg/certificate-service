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
import se.inera.intyg.certificateservice.domain.certificate.model.RelationType;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;

public class ActionRuleChildRelationNoMatch implements ActionRule {

  private final List<RelationType> relationTypes;
  private final List<Status> allowedStatuses;

  public ActionRuleChildRelationNoMatch(
      List<RelationType> relationTypes, List<Status> allowedStatuses) {
    this.relationTypes = relationTypes;
    this.allowedStatuses = allowedStatuses;
  }

  @Override
  public boolean evaluate(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    return certificate.filter(this::doesntMatchRelationType).isPresent();
  }

  private boolean doesntMatchRelationType(Certificate certificate) {
    return certificate.children().stream()
        .noneMatch(
            relation ->
                relationTypes.contains(relation.type())
                    && !allowedStatuses.contains(relation.certificate().status()));
  }

  @Override
  public String getReasonForPermissionDenied() {
    return "Du saknar behörighet för den begärda åtgärden eftersom intyget redan har relation med typ: %s"
        .formatted(relationTypes);
  }
}
