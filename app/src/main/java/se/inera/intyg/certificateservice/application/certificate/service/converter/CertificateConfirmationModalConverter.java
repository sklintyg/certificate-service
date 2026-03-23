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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.AlertDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateConfirmationModalDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateModalActionTypeDTO;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateModalActionType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateConfirmationModal;

@Component
public class CertificateConfirmationModalConverter {

  public CertificateConfirmationModalDTO convert(CertificateConfirmationModal modal) {
    if (modal == null) {
      return null;
    }

    return CertificateConfirmationModalDTO.builder()
        .title(modal.title())
        .alert(AlertDTO.builder().type(modal.alert().type()).text(modal.alert().text()).build())
        .checkboxText(modal.checkboxText())
        .primaryAction(convertAction(modal.primaryAction()))
        .secondaryAction(convertAction(modal.secondaryAction()))
        .text(modal.text())
        .build();
  }

  private CertificateModalActionTypeDTO convertAction(CertificateModalActionType type) {
    switch (type) {
      case DELETE -> {
        return CertificateModalActionTypeDTO.DELETE;
      }
      case READ -> {
        return CertificateModalActionTypeDTO.READ;
      }
      case CANCEL -> {
        return CertificateModalActionTypeDTO.CANCEL;
      }
    }

    throw new IllegalStateException(String.format("Not allowed modal action type '%s'", type));
  }
}
