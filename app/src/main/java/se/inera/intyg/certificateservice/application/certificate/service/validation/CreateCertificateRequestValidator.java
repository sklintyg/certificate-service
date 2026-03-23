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
package se.inera.intyg.certificateservice.application.certificate.service.validation;

import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateCertificateModelId;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validatePatient;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateUnit;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateUnitExtended;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateUser;

import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.CreateCertificateRequest;

@Component
public class CreateCertificateRequestValidator {

  public void validate(CreateCertificateRequest createCertificateRequest) {
    validateUser(createCertificateRequest.getUser());
    validateUnitExtended(createCertificateRequest.getUnit(), "Unit");
    validateUnit(createCertificateRequest.getCareUnit(), "CareUnit");
    validateUnit(createCertificateRequest.getCareProvider(), "CareProvider");
    validatePatient(createCertificateRequest.getPatient());
    validateCertificateModelId(createCertificateRequest.getCertificateModelId());
  }
}
