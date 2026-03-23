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

import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateAdditonalInfoText;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateCertificateId;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validatePatient;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateUnit;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateUnitExtended;
import static se.inera.intyg.certificateservice.application.common.validator.ValidationUtil.validateUser;

import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificatePdfRequest;

@Component
public class GetCertificatePdfRequestValidator {

  public void validate(GetCertificatePdfRequest getCertificatePdfRequest, String certificateId) {
    validateUser(getCertificatePdfRequest.getUser());
    validateUnitExtended(getCertificatePdfRequest.getUnit(), "Unit");
    validateUnit(getCertificatePdfRequest.getCareUnit(), "CareUnit");
    validateUnit(getCertificatePdfRequest.getCareProvider(), "CareProvider");
    validateCertificateId(certificateId);
    validatePatient(getCertificatePdfRequest.getPatient());
    validateAdditonalInfoText(getCertificatePdfRequest.getAdditionalInfoText());
  }
}
