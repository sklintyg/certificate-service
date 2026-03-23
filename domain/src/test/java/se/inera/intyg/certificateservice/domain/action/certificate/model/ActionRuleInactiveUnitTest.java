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
import se.inera.intyg.certificateservice.domain.unit.model.Inactive;
import se.inera.intyg.certificateservice.domain.unit.model.SubUnit;

class ActionRuleInactiveUnitTest {

  private ActionRuleInactiveUnit actionRuleInactiveUnit;

  @BeforeEach
  void setUp() {
    actionRuleInactiveUnit = new ActionRuleInactiveUnit();
  }

  @Test
  void shallReturnFalseIfUnitIsInactive() {
    final var actionEvaluation =
        ActionEvaluation.builder()
            .subUnit(SubUnit.builder().inactive(new Inactive(true)).build())
            .build();

    final var actualResult =
        actionRuleInactiveUnit.evaluate(Optional.empty(), Optional.of(actionEvaluation));

    assertFalse(actualResult);
  }

  @Test
  void shallReturnTrueIfUnitIsNotInactive() {
    final var actionEvaluation =
        ActionEvaluation.builder()
            .subUnit(SubUnit.builder().inactive(new Inactive(false)).build())
            .build();

    final var actualResult =
        actionRuleInactiveUnit.evaluate(Optional.empty(), Optional.of(actionEvaluation));

    assertTrue(actualResult);
  }
}
