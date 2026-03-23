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
package se.inera.intyg.certificateservice.application.message.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateRelationTypeDTO.COMPLEMENTED;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Relation;
import se.inera.intyg.certificateservice.domain.certificate.model.RelationType;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;

class CertificateRelationConverterTest {

  private static final CertificateId CHILD_CERTIFICATE_ID = new CertificateId("childCertificateId");
  private CertificateRelationConverter certificateRelationConverter;
  private Optional<Relation> relation;

  @BeforeEach
  void setUp() {
    certificateRelationConverter = new CertificateRelationConverter();
    relation =
        Optional.of(
            Relation.builder()
                .type(RelationType.COMPLEMENT)
                .certificate(
                    MedicalCertificate.builder()
                        .id(CHILD_CERTIFICATE_ID)
                        .status(Status.SIGNED)
                        .build())
                .created(LocalDateTime.now())
                .build());
  }

  @Test
  void shallIncludeCertificateId() {
    assertEquals(
        CHILD_CERTIFICATE_ID.id(),
        certificateRelationConverter.convert(relation).getCertificateId());
  }

  @Test
  void shallIncludeStatus() {
    assertEquals(
        CertificateStatusTypeDTO.SIGNED,
        certificateRelationConverter.convert(relation).getStatus());
  }

  @Test
  void shallIncludeCreated() {
    assertNotNull(certificateRelationConverter.convert(relation).getCreated());
  }

  @Test
  void shallIncludeType() {
    assertEquals(COMPLEMENTED, certificateRelationConverter.convert(relation).getType());
  }

  @Test
  void shallReturnNullIfEmpty() {
    assertNull(certificateRelationConverter.convert(Optional.empty()));
  }
}
