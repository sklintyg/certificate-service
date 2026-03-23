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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;

class AG7804CertificateGeneralPrintProviderTest {

  public static final String LEFT_MARGIN_TEXT = "%s %s %s - Fastställd av %s";
  public static final String DRAFT_ALERT_INFO_TEXT = "arbetsgivaren";

  @Test
  void shouldIncludeGeneralPrintText() {
    final var provider = new AG7804CertificateGeneralPrintProvider();

    final var certificate =
        MedicalCertificate.builder()
            .certificateModel(CertificateModel.builder().generalPrintProvider(provider).build())
            .build();

    assertEquals(LEFT_MARGIN_TEXT, certificate.getGeneralPrintText().get().leftMarginInfoText());
    assertEquals(
        DRAFT_ALERT_INFO_TEXT, certificate.getGeneralPrintText().get().draftAlertInfoText());
  }

  @Test
  void shouldReturnEmptyIfGeneralPrintTextIsNull() {

    final var certificate =
        MedicalCertificate.builder().certificateModel(CertificateModel.builder().build()).build();

    assertFalse(certificate.getGeneralPrintText().isPresent());
  }
}
