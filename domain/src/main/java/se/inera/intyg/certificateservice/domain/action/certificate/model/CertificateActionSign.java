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
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateActionSpecification;

@Builder
@Getter(AccessLevel.NONE)
public class CertificateActionSign implements CertificateAction {

  private static final String SIGN_NAME = "Signera intyget";
  private static final String SEND_AFTER_SIGN_NAME = "Signera och skicka";
  private static final String SEND_AFTER_SIGN_DESCRIPTION = "Intyget skickas direkt till %s.";
  private static final String SIGN_DESCRIPTION = "Intyget signeras.";
  private final CertificateActionSpecification certificateActionSpecification;
  private final List<ActionRule> actionRules;

  @Override
  public List<String> reasonNotAllowed(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    return actionRules.stream()
        .filter(value -> !value.evaluate(certificate, actionEvaluation))
        .map(ActionRule::getReasonForPermissionDenied)
        .toList();
  }

  @Override
  public CertificateActionType getType() {
    return certificateActionSpecification.certificateActionType();
  }

  @Override
  public boolean evaluate(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    return actionRules.stream()
            .filter(value -> value.evaluate(certificate, actionEvaluation))
            .count()
        == actionRules.size();
  }

  @Override
  public String getName(Optional<Certificate> optionalCertificate) {
    return optionalCertificate
        .map(
            certificate ->
                displaySendAfterSignAction(certificate) ? SEND_AFTER_SIGN_NAME : SIGN_NAME)
        .orElse(SIGN_NAME);
  }

  @Override
  public String getDescription(Optional<Certificate> optionalCertificate) {
    return optionalCertificate
        .filter(CertificateActionSign::displaySendAfterSignAction)
        .map(
            certificate ->
                SEND_AFTER_SIGN_DESCRIPTION.formatted(
                    certificate.certificateModel().recipient().name()))
        .orElse(SIGN_DESCRIPTION);
  }

  private static boolean displaySendAfterSignAction(Certificate certificate) {
    return certificate
            .certificateModel()
            .certificateActionExists(CertificateActionType.SEND_AFTER_SIGN)
        || hasComplementRelationAndSendAfterComplementActionAction(certificate);
  }

  private static boolean hasComplementRelationAndSendAfterComplementActionAction(
      Certificate certificate) {
    return certificate.parent() != null
        && RelationType.COMPLEMENT.equals(certificate.parent().type())
        && certificate
            .certificateModel()
            .certificateActionExists(CertificateActionType.SEND_AFTER_COMPLEMENT);
  }
}
