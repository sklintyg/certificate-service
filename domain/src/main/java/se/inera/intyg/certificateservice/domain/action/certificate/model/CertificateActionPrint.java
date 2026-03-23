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
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateMetaData;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateActionSpecification;

@Builder
@Getter(AccessLevel.NONE)
public class CertificateActionPrint implements CertificateAction {

  private static final String NAME = "Skriv ut";
  private static final String DESCRIPTION =
      "Öppnar ett fönster där du kan välja att skriva ut eller spara intyget som PDF.";
  private static final String DRAFT_DESCRIPTION =
      "Öppnar ett fönster där du kan välja att skriva ut eller spara intygsutkastet som PDF.";
  private final CertificateActionSpecification certificateActionSpecification;
  private final List<ActionRule> actionRules;
  private static final String PRINT_PROTECTED_PERSON_BODY =
      "<div class='ic-alert ic-alert--status ic-alert--info'>\n"
          + "<i class='ic-alert__icon ic-info-icon'></i><p>Patienten har skyddade personuppgifter. Hantera utskriften varsamt.</p></div>";

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
  public String getName(Optional<Certificate> certificate) {
    return NAME;
  }

  @Override
  public String getDescription(Optional<Certificate> certificate) {
    if (certificate.map(Certificate::isDraft).orElse(false)) {
      return DRAFT_DESCRIPTION;
    }

    return DESCRIPTION;
  }

  @Override
  public String getBody(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    final var patient =
        certificate
            .map(Certificate::certificateMetaData)
            .map(CertificateMetaData::patient)
            .orElse(null);

    if (patient == null) {
      return null;
    }

    return patient.protectedPerson().value() ? PRINT_PROTECTED_PERSON_BODY : null;
  }
}
