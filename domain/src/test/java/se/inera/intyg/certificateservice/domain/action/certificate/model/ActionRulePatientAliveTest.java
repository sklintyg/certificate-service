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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.ATHENA_REACT_ANDERSSON;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.ATLAS_REACT_ABRAHAMSSON;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUser.AJLA_DOKTOR;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateMetaData;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;

class ActionRulePatientAliveTest {

  private ActionRulePatientAlive actionRulePatientAlive;

  @BeforeEach
  void setUp() {
    actionRulePatientAlive = new ActionRulePatientAlive();
  }

  @Nested
  class PatientInActionEvaluation {

    @Test
    void shallReturnTrueIfPatientIsNotDeceased() {
      final var actionEvaluation =
          ActionEvaluation.builder().patient(ATHENA_REACT_ANDERSSON).user(AJLA_DOKTOR).build();

      final var actualResult =
          actionRulePatientAlive.evaluate(Optional.empty(), Optional.of(actionEvaluation));

      assertTrue(actualResult);
    }

    @Test
    void shallReturnFalseIfPatientIsDeceased() {
      final var actionEvaluation =
          ActionEvaluation.builder().patient(ATLAS_REACT_ABRAHAMSSON).user(AJLA_DOKTOR).build();

      final var actualResult =
          actionRulePatientAlive.evaluate(Optional.empty(), Optional.of(actionEvaluation));

      assertFalse(actualResult);
    }
  }

  @Nested
  class PatientInCertificate {

    @Test
    void shallReturnTrueIfPatientIsNotDeceased() {
      final var actionEvaluation = ActionEvaluation.builder().user(AJLA_DOKTOR).build();

      final var certificate =
          MedicalCertificate.builder()
              .certificateMetaData(
                  CertificateMetaData.builder().patient(ATHENA_REACT_ANDERSSON).build())
              .build();
      final var actualResult =
          actionRulePatientAlive.evaluate(Optional.of(certificate), Optional.of(actionEvaluation));

      assertTrue(actualResult);
    }

    @Test
    void shallReturnFalseIfPatientIsNotDeceased() {
      final var actionEvaluation = ActionEvaluation.builder().user(AJLA_DOKTOR).build();

      final var certificate =
          MedicalCertificate.builder()
              .certificateMetaData(
                  CertificateMetaData.builder().patient(ATHENA_REACT_ANDERSSON).build())
              .build();
      final var actualResult =
          actionRulePatientAlive.evaluate(Optional.of(certificate), Optional.of(actionEvaluation));

      assertTrue(actualResult);
    }
  }

  @Test
  void shallReturnTrueIfNoPatientInActionEvaluationOrCertificate() {
    final var actionEvaluation = ActionEvaluation.builder().user(AJLA_DOKTOR).build();

    final var certificate =
        MedicalCertificate.builder()
            .certificateMetaData(CertificateMetaData.builder().build())
            .build();

    final var actualResult =
        actionRulePatientAlive.evaluate(Optional.of(certificate), Optional.of(actionEvaluation));

    assertTrue(actualResult);
  }

  @Test
  void shallReturnErrorMessage() {
    assertEquals(
        "Du kan ej utföra den begärda åtgärden eftersom patienten är avliden.",
        actionRulePatientAlive.getReasonForPermissionDenied());
  }
}
