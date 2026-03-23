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
package se.inera.intyg.certificateservice.domain.staff.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.common.model.AllowCopy;
import se.inera.intyg.certificateservice.domain.common.model.Blocked;
import se.inera.intyg.certificateservice.domain.common.model.HealthCareProfessionalLicence;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;
import se.inera.intyg.certificateservice.domain.common.model.PaTitle;
import se.inera.intyg.certificateservice.domain.common.model.Role;
import se.inera.intyg.certificateservice.domain.common.model.Speciality;
import se.inera.intyg.certificateservice.domain.patient.model.Name;
import se.inera.intyg.certificateservice.domain.user.model.User;

@Value
@Builder
public class Staff {

  HsaId hsaId;
  Name name;
  Role role;
  List<PaTitle> paTitles;
  List<Speciality> specialities;
  Blocked blocked;
  AllowCopy allowCopy;
  List<HealthCareProfessionalLicence> healthCareProfessionalLicence;

  public static Staff create(User user) {
    return Staff.builder()
        .hsaId(user.hsaId())
        .name(user.name())
        .role(user.role())
        .paTitles(user.paTitles().stream().toList())
        .specialities(user.specialities().stream().toList())
        .blocked(user.blocked())
        .allowCopy(user.allowCopy())
        .healthCareProfessionalLicence(user.healthCareProfessionalLicence().stream().toList())
        .build();
  }
}
