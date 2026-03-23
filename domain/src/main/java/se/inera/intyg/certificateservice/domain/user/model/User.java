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
package se.inera.intyg.certificateservice.domain.user.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import se.inera.intyg.certificateservice.domain.common.model.AccessScope;
import se.inera.intyg.certificateservice.domain.common.model.Agreement;
import se.inera.intyg.certificateservice.domain.common.model.AllowCopy;
import se.inera.intyg.certificateservice.domain.common.model.Blocked;
import se.inera.intyg.certificateservice.domain.common.model.HealthCareProfessionalLicence;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;
import se.inera.intyg.certificateservice.domain.common.model.PaTitle;
import se.inera.intyg.certificateservice.domain.common.model.Role;
import se.inera.intyg.certificateservice.domain.common.model.Speciality;
import se.inera.intyg.certificateservice.domain.patient.model.Name;

@Value
@Builder
public class User {

  HsaId hsaId;
  Name name;
  Role role;
  List<PaTitle> paTitles;
  List<Speciality> specialities;
  Blocked blocked;
  Agreement agreement;
  AllowCopy allowCopy;
  AccessScope accessScope;
  List<HealthCareProfessionalLicence> healthCareProfessionalLicence;
  ResponsibleIssuer responsibleIssuer;
  SrsActive srsActive;
}
