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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.ATHENA_REACT_ANDERSSON;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.athenaReactAnderssonBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.coordinationNumberBuilder;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.personalIdentityNumberBuilder;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ActionRuleContentProvider;

@ExtendWith(MockitoExtension.class)
class ActionRulePatientAgeEighteenOrOlderTest {

  @Mock ActionRuleContentProvider actionRuleContentProvider;

  @Nested
  class EvaluateTests {

    @Test
    void shouldThrowIfPatientIsMissing() {
      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      final var illegalStateException =
          assertThrows(
              IllegalArgumentException.class,
              () -> rulePatientAgeEighteenOrOlder.evaluate(Optional.empty(), Optional.empty()));

      assertEquals(
          "Action evaluation is missing required field patient",
          illegalStateException.getMessage());
    }

    @Test
    void shouldReturnTrueIfPatientIsOlderThanEighteen() {
      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      final var actionEvaluation =
          Optional.of(ActionEvaluation.builder().patient(ATHENA_REACT_ANDERSSON).build());

      final var result = rulePatientAgeEighteenOrOlder.evaluate(Optional.empty(), actionEvaluation);
      assertTrue(result);
    }

    @Test
    void shouldReturnTrueIfPatientIsEighteen() {
      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      final var actionEvaluation =
          Optional.of(
              ActionEvaluation.builder()
                  .patient(
                      athenaReactAnderssonBuilder()
                          .id(personalIdentityNumberBuilder(LocalDate.now().minusYears(18)).build())
                          .build())
                  .build());

      final var result = rulePatientAgeEighteenOrOlder.evaluate(Optional.empty(), actionEvaluation);
      assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfPatientIsUnderEighteen() {
      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      final var actionEvaluation =
          Optional.of(
              ActionEvaluation.builder()
                  .patient(
                      athenaReactAnderssonBuilder()
                          .id(
                              personalIdentityNumberBuilder(
                                      LocalDate.now().minusYears(18).plusDays(1))
                                  .build())
                          .build())
                  .build());

      final var result = rulePatientAgeEighteenOrOlder.evaluate(Optional.empty(), actionEvaluation);
      assertFalse(result);
    }

    @Test
    void shouldReturnTrueIfPatientIsEighteenWithCoordinationNumber() {
      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      final var actionEvaluation =
          Optional.of(
              ActionEvaluation.builder()
                  .patient(
                      athenaReactAnderssonBuilder()
                          .id(coordinationNumberBuilder(LocalDate.now().minusYears(18)).build())
                          .build())
                  .build());

      final var result = rulePatientAgeEighteenOrOlder.evaluate(Optional.empty(), actionEvaluation);
      assertTrue(result);
    }

    @Test
    void shouldReturnFalseIfPatientIsUnderEighteenWithCoordinationNumber() {
      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      final var actionEvaluation =
          Optional.of(
              ActionEvaluation.builder()
                  .patient(
                      athenaReactAnderssonBuilder()
                          .id(
                              coordinationNumberBuilder(LocalDate.now().minusYears(18).plusDays(1))
                                  .build())
                          .build())
                  .build());

      final var result = rulePatientAgeEighteenOrOlder.evaluate(Optional.empty(), actionEvaluation);
      assertFalse(result);
    }
  }

  @Nested
  class GetReasonForPermissionDeniedTests {

    @Test
    void shouldReturnReasonFromContentProvider() {
      final var expectedMessage = "expectedMessage";

      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      when(actionRuleContentProvider.getReasonForPermissionDenied()).thenReturn(expectedMessage);

      final var actualMessage = rulePatientAgeEighteenOrOlder.getReasonForPermissionDenied();
      assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldReturnDefaultReasonIfContentProviderNotPresent() {
      final var expectedMessage =
          "Du saknar behörighet för den begärda åtgärden."
              + " För att utföra denna uppgift krävs särskilda rättigheter eller en specifik befattning.";

      final var rulePatientAgeEighteenOrOlder = new ActionRulePatientAgeEighteenOrOlder(null);

      final var actualMessage = rulePatientAgeEighteenOrOlder.getReasonForPermissionDenied();
      assertEquals(expectedMessage, actualMessage);
    }
  }

  @Nested
  class MessageTests {

    @Test
    void shouldReturnMessageWhenPatientIsUnderEighteen() {
      final var expectedMessage = "expectedMessage";
      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      when(actionRuleContentProvider.getReasonForPermissionDenied()).thenReturn(expectedMessage);

      final var actionEvaluation =
          Optional.of(
              ActionEvaluation.builder()
                  .patient(
                      athenaReactAnderssonBuilder()
                          .id(
                              personalIdentityNumberBuilder(
                                      LocalDate.now().minusYears(18).plusDays(1))
                                  .build())
                          .build())
                  .build());

      final var result = rulePatientAgeEighteenOrOlder.message(Optional.empty(), actionEvaluation);
      assertEquals(Optional.of(expectedMessage), result);
    }

    @Test
    void shouldReturnEmptyWhenPatientIsEighteen() {
      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      final var actionEvaluation =
          Optional.of(
              ActionEvaluation.builder()
                  .patient(
                      athenaReactAnderssonBuilder()
                          .id(personalIdentityNumberBuilder(LocalDate.now().minusYears(18)).build())
                          .build())
                  .build());

      final var result = rulePatientAgeEighteenOrOlder.message(Optional.empty(), actionEvaluation);
      assertEquals(Optional.empty(), result);
    }

    @Test
    void shouldReturnEmptyWhenPatientIsOlderThanEighteen() {
      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      final var actionEvaluation =
          Optional.of(ActionEvaluation.builder().patient(ATHENA_REACT_ANDERSSON).build());

      final var result = rulePatientAgeEighteenOrOlder.message(Optional.empty(), actionEvaluation);
      assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnMessageWhenPatientIsUnderEighteenWithCoordinationNumber() {
      final var expectedMessage = "expectedMessage";
      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      when(actionRuleContentProvider.getReasonForPermissionDenied()).thenReturn(expectedMessage);

      final var actionEvaluation =
          Optional.of(
              ActionEvaluation.builder()
                  .patient(
                      athenaReactAnderssonBuilder()
                          .id(
                              coordinationNumberBuilder(LocalDate.now().minusYears(18).plusDays(1))
                                  .build())
                          .build())
                  .build());

      final var result = rulePatientAgeEighteenOrOlder.message(Optional.empty(), actionEvaluation);
      assertEquals(Optional.of(expectedMessage), result);
    }

    @Test
    void shouldReturnEmptyWhenPatientIsEighteenWithCoordinationNumber() {
      final var rulePatientAgeEighteenOrOlder =
          new ActionRulePatientAgeEighteenOrOlder(actionRuleContentProvider);

      final var actionEvaluation =
          Optional.of(
              ActionEvaluation.builder()
                  .patient(
                      athenaReactAnderssonBuilder()
                          .id(coordinationNumberBuilder(LocalDate.now().minusYears(18)).build())
                          .build())
                  .build());

      final var result = rulePatientAgeEighteenOrOlder.message(Optional.empty(), actionEvaluation);
      assertEquals(Optional.empty(), result);
    }
  }
}
