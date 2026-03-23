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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.AG114_CERTIFICATE;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.ag114CertificateBuilder;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.DateRange;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@ExtendWith(MockitoExtension.class)
class AG114SickLeaveProviderTest {

  private final AG114SickLeaveProvider provider = new AG114SickLeaveProvider();

  @Test
  void shouldMapId() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(AG114_CERTIFICATE.id(), sickLeaveCertificate.orElseThrow().id());
  }

  @Test
  void shouldMapCareGiverId() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(
        AG114_CERTIFICATE.certificateMetaData().careProvider().hsaId(),
        sickLeaveCertificate.orElseThrow().careGiverId());
  }

  @Test
  void shouldMapCareUnitId() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(
        AG114_CERTIFICATE.certificateMetaData().issuingUnit().hsaId(),
        sickLeaveCertificate.orElseThrow().issuingUnitId());
  }

  @Test
  void shouldMapCareUnitName() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(
        AG114_CERTIFICATE.certificateMetaData().issuingUnit().name(),
        sickLeaveCertificate.orElseThrow().issuingUnitName());
  }

  @Test
  void shouldMapType() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(
        AG114_CERTIFICATE.certificateModel().type(), sickLeaveCertificate.orElseThrow().type());
  }

  @Test
  void shouldMapCivicRegistrationNumber() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(
        AG114_CERTIFICATE.certificateMetaData().patient().id(),
        sickLeaveCertificate.orElseThrow().civicRegistrationNumber());
  }

  @Test
  void shouldMapSigningDoctorName() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(
        AG114_CERTIFICATE.certificateMetaData().issuer().name(),
        sickLeaveCertificate.orElseThrow().signingDoctorName());
  }

  @Test
  void shouldMapPatientName() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(
        AG114_CERTIFICATE.certificateMetaData().patient().name(),
        sickLeaveCertificate.orElseThrow().patientName());
  }

  @Test
  void shouldMapDiagnoseCode() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals("A013", sickLeaveCertificate.orElseThrow().diagnoseCode().code());
  }

  @Test
  void shouldUseDefaultDiagnosisWhenNoDiagnosisPresent() {
    final var certificate =
        ag114CertificateBuilder()
            .elementData(
                AG114_CERTIFICATE.elementData().stream()
                    .filter(elementData -> !elementData.id().id().equals("4"))
                    .toList())
            .build();

    final var sickLeaveCertificate = provider.build(certificate, false);

    assertEquals("X", sickLeaveCertificate.orElseThrow().diagnoseCode().code());
    assertEquals(
        "Diagnoskod X är okänd och har ingen beskrivning",
        sickLeaveCertificate.orElseThrow().diagnoseCode().description());
  }

  @Test
  void shouldNotMapBiDiagnoseCode1IfMissing() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertNull(sickLeaveCertificate.orElseThrow().biDiagnoseCode1());
  }

  @Test
  void shouldNotMapBiDiagnoseCode2IfMissing() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertNull(sickLeaveCertificate.orElseThrow().biDiagnoseCode2());
  }

  @Test
  void shouldMapSigningDoctorId() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(
        AG114_CERTIFICATE.certificateMetaData().issuer().hsaId(),
        sickLeaveCertificate.orElseThrow().signingDoctorId());
  }

  @Test
  void shouldMapSigningDateTime() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(AG114_CERTIFICATE.signed(), sickLeaveCertificate.orElseThrow().signingDateTime());
  }

  @Test
  void shouldMapDeleted() {
    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(AG114_CERTIFICATE.revoked(), sickLeaveCertificate.orElseThrow().deleted());
  }

  @Test
  void shouldMapWorkCapacities() {
    final var expectedDateRange =
        DateRange.builder()
            .dateRangeId(new FieldId(("70")))
            .from(LocalDate.now())
            .to(LocalDate.now().plusDays(30))
            .build();

    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(List.of(expectedDateRange), sickLeaveCertificate.orElseThrow().workCapacities());
  }

  @Test
  void shouldMapEmployment() {
    final var expectedEmployment =
        List.of(ElementValueCode.builder().code("Nuvarande arbete").build());

    final var sickLeaveCertificate = provider.build(AG114_CERTIFICATE, false);
    assertEquals(expectedEmployment, sickLeaveCertificate.orElseThrow().employment());
  }
}
