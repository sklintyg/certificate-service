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

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.common.model.Agreement;
import se.inera.intyg.certificateservice.domain.user.model.User;

class ActionRuleUserAgreementTest {

  private ActionRuleUserAgreement actionRuleUserAgreement;

  @BeforeEach
  void setUp() {
    actionRuleUserAgreement = new ActionRuleUserAgreement();
  }

  @Test
  void shallReturnFalseIfAgreementIsFalse() {
    final var actionEvaluation =
        ActionEvaluation.builder()
            .user(User.builder().agreement(new Agreement(false)).build())
            .build();

    final var actualResult =
        actionRuleUserAgreement.evaluate(Optional.empty(), Optional.of(actionEvaluation));

    assertFalse(actualResult);
  }

  @Test
  void shallReturnTrueIfAgreementIsTrue() {
    final var actionEvaluation =
        ActionEvaluation.builder()
            .user(User.builder().agreement(new Agreement(true)).build())
            .build();

    final var actualResult =
        actionRuleUserAgreement.evaluate(Optional.empty(), Optional.of(actionEvaluation));

    assertTrue(actualResult);
  }
}
