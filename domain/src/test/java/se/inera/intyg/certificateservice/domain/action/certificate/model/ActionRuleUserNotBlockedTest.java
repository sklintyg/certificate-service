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
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.ATHENA_REACT_ANDERSSON;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUser.AJLA_DOKTOR;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUser.ajlaDoctorBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUserConstants.BLOCKED_TRUE;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ActionRuleUserNotBlockedTest {

  private ActionRuleUserNotBlocked actionRuleUserNotBlocked;

  @BeforeEach
  void setUp() {
    actionRuleUserNotBlocked = new ActionRuleUserNotBlocked();
  }

  @Test
  void shallReturnFalseIfUserIsBlocked() {
    final var actionEvaluation =
        ActionEvaluation.builder()
            .patient(ATHENA_REACT_ANDERSSON)
            .user(ajlaDoctorBuilder().blocked(BLOCKED_TRUE).build())
            .build();

    final var actualResult =
        actionRuleUserNotBlocked.evaluate(Optional.empty(), Optional.of(actionEvaluation));

    assertFalse(actualResult);
  }

  @Test
  void shallReturnTrueIfUserIsNotBlocked() {
    final var actionEvaluation =
        ActionEvaluation.builder().patient(ATHENA_REACT_ANDERSSON).user(AJLA_DOKTOR).build();

    final var actualResult =
        actionRuleUserNotBlocked.evaluate(Optional.empty(), Optional.of(actionEvaluation));

    assertTrue(actualResult);
  }
}
