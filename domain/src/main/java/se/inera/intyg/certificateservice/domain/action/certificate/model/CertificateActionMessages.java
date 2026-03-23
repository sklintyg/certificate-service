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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.RelationType;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateActionSpecification;

@Builder
@Getter(AccessLevel.NONE)
public class CertificateActionMessages implements CertificateAction {

  private static final String NAME = "Ärendekommunikation";
  private static final String DESCRIPTION = "Hantera kompletteringsbegäran, frågor och svar";
  private final CertificateActionSpecification certificateActionSpecification;
  private final List<ActionRule> actionRules;

  @Override
  public CertificateActionType getType() {
    return certificateActionSpecification.certificateActionType();
  }

  @Override
  public boolean evaluate(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    if (isDraftButIsComplementing(certificate)) {
      return true;
    }

    return actionRules.stream()
            .filter(value -> value.evaluate(certificate, actionEvaluation))
            .count()
        == actionRules.size();
  }

  @Override
  public List<String> reasonNotAllowed(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    return actionRules.stream()
        .filter(value -> !value.evaluate(certificate, actionEvaluation))
        .map(ActionRule::getReasonForPermissionDenied)
        .toList();
  }

  @Override
  public String getName(Optional<Certificate> optionalCertificate) {
    return NAME;
  }

  @Override
  public String getDescription(Optional<Certificate> optionalCertificate) {
    return DESCRIPTION;
  }

  private static boolean isDraftButIsComplementing(Optional<Certificate> certificate) {
    return certificate.stream()
        .filter(value -> Status.DRAFT.equals(value.status()))
        .filter(value -> value.parent() != null)
        .anyMatch(value -> RelationType.COMPLEMENT.equals(value.parent().type()));
  }
}
