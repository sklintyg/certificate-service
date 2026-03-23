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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.AlertDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateConfirmationModalDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateModalActionTypeDTO;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateModalActionType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.Alert;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateConfirmationModal;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageLevel;

class CertificateConfirmationModalConverterTest {

  @Test
  void shouldConvertNullToNull() {
    assertNull(new CertificateConfirmationModalConverter().convert(null));
  }

  @Test
  void shouldConvertConfirmationModal() {
    final var expected =
        CertificateConfirmationModalDTO.builder()
            .text("TEXT")
            .title("TITLE")
            .alert(AlertDTO.builder().text("ALERT").type(MessageLevel.INFO).build())
            .checkboxText("CHECKBOX")
            .primaryAction(CertificateModalActionTypeDTO.READ)
            .secondaryAction(CertificateModalActionTypeDTO.CANCEL)
            .build();

    final var result =
        new CertificateConfirmationModalConverter()
            .convert(
                CertificateConfirmationModal.builder()
                    .text("TEXT")
                    .title("TITLE")
                    .alert(Alert.builder().text("ALERT").type(MessageLevel.INFO).build())
                    .checkboxText("CHECKBOX")
                    .primaryAction(CertificateModalActionType.READ)
                    .secondaryAction(CertificateModalActionType.CANCEL)
                    .build());

    assertEquals(expected, result);
  }
}
