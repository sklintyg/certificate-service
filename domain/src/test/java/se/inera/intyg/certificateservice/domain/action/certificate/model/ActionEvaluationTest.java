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
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProvider.ALFA_REGIONEN;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareUnit.ALFA_MEDICINCENTRUM;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.ATHENA_REACT_ANDERSSON;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataSubUnit.ALFA_ALLERGIMOTTAGNINGEN;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataSubUnit.ALFA_MEDICINSKT_CENTRUM;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUser.AJLA_DOKTOR;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActionEvaluationTest {

  private ActionEvaluation.ActionEvaluationBuilder actionEvaluationBuilder;

  @BeforeEach
  void setUp() {
    actionEvaluationBuilder =
        ActionEvaluation.builder()
            .patient(ATHENA_REACT_ANDERSSON)
            .user(AJLA_DOKTOR)
            .subUnit(ALFA_ALLERGIMOTTAGNINGEN)
            .careUnit(ALFA_MEDICINCENTRUM)
            .careProvider(ALFA_REGIONEN);
  }

  @Test
  void shallReturnTrueIfSubUnitIdMatchesCareUnitId() {
    final var actionEvaluation = actionEvaluationBuilder.subUnit(ALFA_MEDICINSKT_CENTRUM).build();

    assertTrue(actionEvaluation.isIssuingUnitCareUnit());
  }

  @Test
  void shallReturnFalseIfSubUnitIdNotMatchingCareUnitId() {
    final var actionEvaluation = actionEvaluationBuilder.build();

    assertFalse(actionEvaluation.isIssuingUnitCareUnit());
  }

  @Test
  void shallReturnTrueIfPatientIsPresent() {
    assertTrue(actionEvaluationBuilder.build().hasPatient());
  }

  @Test
  void shallReturnTrueIfUserIsPresent() {
    assertTrue(actionEvaluationBuilder.build().hasUser());
  }

  @Test
  void shallReturnFalseIfPatientIsMissing() {
    final var actionEvaluation = actionEvaluationBuilder.patient(null).build();
    assertFalse(actionEvaluation.hasPatient());
  }

  @Test
  void shallReturnTrueIfUserIsMissing() {
    final var actionEvaluation = actionEvaluationBuilder.user(null).build();
    assertFalse(actionEvaluation.hasUser());
  }
}
