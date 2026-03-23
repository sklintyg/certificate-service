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

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateEventDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateEventTypeDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateEvent;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateEventType;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;

class CertificateEventConverterTest {

  @Test
  void shouldConvertCertificateEvent() {
    final var now = LocalDateTime.now();
    final var expected =
        CertificateEventDTO.builder()
            .certificateId("C_ID")
            .relatedCertificateId("R_ID")
            .relatedCertificateStatus(CertificateStatusTypeDTO.REVOKED)
            .timestamp(now)
            .type(CertificateEventTypeDTO.COMPLEMENTED)
            .build();

    assertEquals(
        expected,
        new CertificateEventConverter()
            .convert(
                CertificateEvent.builder()
                    .certificateId(new CertificateId("C_ID"))
                    .relatedCertificateId(new CertificateId("R_ID"))
                    .type(CertificateEventType.COMPLEMENTED)
                    .timestamp(now)
                    .relatedCertificateStatus(Status.REVOKED)
                    .build()));
  }

  @Test
  void shouldConvertCertificateWithoutRelatedCertificate() {
    final var now = LocalDateTime.now();
    final var expected =
        CertificateEventDTO.builder()
            .certificateId("C_ID")
            .timestamp(now)
            .type(CertificateEventTypeDTO.COMPLEMENTED)
            .build();

    assertEquals(
        expected,
        new CertificateEventConverter()
            .convert(
                CertificateEvent.builder()
                    .certificateId(new CertificateId("C_ID"))
                    .type(CertificateEventType.COMPLEMENTED)
                    .timestamp(now)
                    .build()));
  }
}
