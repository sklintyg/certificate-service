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
package se.inera.intyg.certificateservice.application.patient.service.validator;

import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validatePatient;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateUnit;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateUnitExtended;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateUser;

import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.patient.dto.GetPatientCertificatesRequest;

@Component
public class GetPatientCertificatesRequestValidator {

  public void validate(GetPatientCertificatesRequest getPatientCertificatesRequest) {
    validateUser(getPatientCertificatesRequest.getUser());
    validateUnitExtended(getPatientCertificatesRequest.getUnit(), "Unit");
    validateUnit(getPatientCertificatesRequest.getCareUnit(), "CareUnit");
    validateUnit(getPatientCertificatesRequest.getCareProvider(), "CareProvider");
    validatePatient(getPatientCertificatesRequest.getPatient());
  }
}
