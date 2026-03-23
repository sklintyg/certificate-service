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
package se.inera.intyg.certificateservice.domain.certificate.model;

import lombok.Builder;
import lombok.Value;
import lombok.With;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;
import se.inera.intyg.certificateservice.domain.staff.model.Staff;
import se.inera.intyg.certificateservice.domain.unit.model.CareProvider;
import se.inera.intyg.certificateservice.domain.unit.model.CareUnit;
import se.inera.intyg.certificateservice.domain.unit.model.IssuingUnit;
import se.inera.intyg.certificateservice.domain.user.model.ResponsibleIssuer;

@Value
@Builder
public class CertificateMetaData {

  @With Patient patient;
  Staff issuer;
  IssuingUnit issuingUnit;
  CareUnit careUnit;
  CareProvider careProvider;
  ResponsibleIssuer responsibleIssuer;
  Staff creator;

  public CertificateMetaData updated(ActionEvaluation actionEvaluation) {
    return CertificateMetaData.builder()
        .patient(actionEvaluation.patient() == null ? patient : actionEvaluation.patient())
        .issuer(Staff.create(actionEvaluation.user()))
        .careUnit(
            careUnit.hsaId().equals(actionEvaluation.careUnit().hsaId())
                ? actionEvaluation.careUnit()
                : careUnit)
        .careProvider(
            careProvider.hsaId().equals(actionEvaluation.careProvider().hsaId())
                ? actionEvaluation.careProvider()
                : careProvider)
        .issuingUnit(
            issuingUnit.hsaId().equals(actionEvaluation.subUnit().hsaId())
                ? actionEvaluation.subUnit()
                : issuingUnit)
        .responsibleIssuer(actionEvaluation.user().responsibleIssuer())
        .build();
  }
}
