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

import lombok.Builder;
import lombok.Value;
import lombok.With;
import se.inera.intyg.certificateservice.domain.patient.model.Patient;
import se.inera.intyg.certificateservice.domain.unit.model.CareProvider;
import se.inera.intyg.certificateservice.domain.unit.model.CareUnit;
import se.inera.intyg.certificateservice.domain.unit.model.SubUnit;
import se.inera.intyg.certificateservice.domain.user.model.User;

@Value
@Builder
public class ActionEvaluation {

  @With Patient patient;
  User user;
  SubUnit subUnit;
  CareUnit careUnit;
  CareProvider careProvider;

  public boolean isIssuingUnitCareUnit() {
    return subUnit.hsaId().equals(careUnit.hsaId());
  }

  public boolean isIssuingUnitSubUnit() {
    return !isIssuingUnitCareUnit();
  }

  public boolean hasPatient() {
    return patient != null;
  }

  public boolean hasUser() {
    return user != null;
  }
}
