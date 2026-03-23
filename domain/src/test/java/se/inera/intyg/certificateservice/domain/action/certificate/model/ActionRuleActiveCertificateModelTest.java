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
package se.inera.intyg.certificateservice.domain.action.certificate.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificate.fk7804CertificateBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModel.fk7804certificateModelBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ActionRuleActiveCertificateModelTest {

  @Test
  void shouldReturnTrueIfCertificateIsActive() {
    final var activeCertificate = fk7804CertificateBuilder().build();

    final var result =
        new ActionRuleActiveCertificateModel()
            .evaluate(Optional.of(activeCertificate), Optional.empty());

    assertTrue(result);
  }

  @Test
  void shouldReturnFalseIfCertificateIsInactive() {
    final var inactiveCertificate =
        fk7804CertificateBuilder()
            .certificateModel(
                fk7804certificateModelBuilder().activeFrom(LocalDateTime.now().plusDays(1)).build())
            .build();

    final var result =
        new ActionRuleActiveCertificateModel()
            .evaluate(Optional.of(inactiveCertificate), Optional.empty());

    assertFalse(result);
  }

  @Test
  void shouldReturnFalseIfCertificateIsMissing() {
    final var result =
        new ActionRuleActiveCertificateModel().evaluate(Optional.empty(), Optional.empty());

    assertFalse(result);
  }
}
