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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateActionSpecification;
import se.inera.intyg.certificateservice.domain.common.model.Role;

class TS8071CertificateActionSpecificationTest {

  @Test
  void shallIncludeCertificateActionCreate() {
    final var expectedType = CertificateActionType.CREATE;

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.certificateActionType())),
        "Expected type: %s".formatted(expectedType));
  }

  @Test
  void shallIncludeCertificateActionRead() {
    final var expectedType = CertificateActionType.READ;

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.certificateActionType())),
        "Expected type: %s".formatted(expectedType));
  }

  @Test
  void shallIncludeCertificateActionUpdate() {
    final var expectedType = CertificateActionType.UPDATE;

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.certificateActionType())),
        "Expected type: %s".formatted(expectedType));
  }

  @Test
  void shallIncludeCertificateActionDelete() {
    final var expectedType = CertificateActionType.DELETE;

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.certificateActionType())),
        "Expected type: %s".formatted(expectedType));
  }

  @Test
  void shallIncludeCertificateActionSign() {
    final var expectedSpecification =
        CertificateActionSpecification.builder()
            .certificateActionType(CertificateActionType.SIGN)
            .allowedRoles(List.of(Role.DOCTOR, Role.PRIVATE_DOCTOR))
            .build();

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream().anyMatch(expectedSpecification::equals),
        "Expected type: %s".formatted(expectedSpecification));
  }

  @Test
  void shallIncludeCertificateActionSend() {
    final var expectedSpecification =
        CertificateActionSpecification.builder()
            .certificateActionType(CertificateActionType.SEND)
            .allowedRoles(
                List.of(
                    Role.DOCTOR, Role.PRIVATE_DOCTOR, Role.CARE_ADMIN, Role.MIDWIFE, Role.NURSE))
            .build();

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream().anyMatch(expectedSpecification::equals),
        "Expected type: %s".formatted(expectedSpecification));
  }

  @Test
  void shallIncludeCertificateActionRevoke() {
    final var expectedSpecification =
        CertificateActionSpecification.builder()
            .certificateActionType(CertificateActionType.REVOKE)
            .allowedRoles(List.of(Role.DOCTOR, Role.PRIVATE_DOCTOR))
            .build();

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream().anyMatch(expectedSpecification::equals),
        "Expected type: %s".formatted(expectedSpecification));
  }

  @Test
  void shallIncludeCertificateActionReplace() {
    final var expectedType = CertificateActionType.REPLACE;

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.certificateActionType())),
        "Expected type: %s".formatted(expectedType));
  }

  @Test
  void shallIncludeCertificateActionReplaceContinue() {
    final var expectedType = CertificateActionType.REPLACE_CONTINUE;

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream()
            .anyMatch(
                actionSpecification ->
                    expectedType.equals(actionSpecification.certificateActionType())),
        "Expected type: %s".formatted(expectedType));
  }

  @Test
  void shallIncludeCertificateActionForwardCertificate() {
    final var expectedSpecification =
        CertificateActionSpecification.builder()
            .certificateActionType(CertificateActionType.FORWARD_CERTIFICATE)
            .allowedRoles(List.of(Role.CARE_ADMIN, Role.MIDWIFE, Role.NURSE))
            .build();

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream().anyMatch(expectedSpecification::equals),
        "Expected type: %s".formatted(expectedSpecification));
  }

  @Test
  void shallIncludeCertificateActionReadyForSign() {
    final var expectedSpecification =
        CertificateActionSpecification.builder()
            .certificateActionType(CertificateActionType.READY_FOR_SIGN)
            .allowedRoles(List.of(Role.CARE_ADMIN, Role.MIDWIFE, Role.NURSE))
            .build();

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream().anyMatch(expectedSpecification::equals),
        "Expected type: %s".formatted(expectedSpecification));
  }

  @Test
  void shallIncludeCertificateActionAccessForRoles() {
    final var expectedSpecification =
        CertificateActionSpecification.builder()
            .certificateActionType(CertificateActionType.LIST_CERTIFICATE_TYPE)
            .allowedRoles(
                List.of(
                    Role.DOCTOR, Role.PRIVATE_DOCTOR, Role.CARE_ADMIN, Role.MIDWIFE, Role.NURSE))
            .build();

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream().anyMatch(expectedSpecification::equals),
        "Expected type: %s".formatted(expectedSpecification));
  }

  @Test
  void shallIncludeCertificateActionForwardCertificateFromList() {
    final var expectedSpecification =
        CertificateActionSpecification.builder()
            .certificateActionType(CertificateActionType.FORWARD_CERTIFICATE_FROM_LIST)
            .build();

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream().anyMatch(expectedSpecification::equals),
        "Expected type: %s".formatted(expectedSpecification));
  }

  @Test
  void shallIncludeCertificateActionPrint() {
    final var expectedSpecification =
        CertificateActionSpecification.builder()
            .certificateActionType(CertificateActionType.PRINT)
            .build();

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream().anyMatch(expectedSpecification::equals),
        "Expected type: %s".formatted(expectedSpecification));
  }

  @Test
  void shallIncludeCertificateActionInactiveCertificateModel() {
    final var expectedSpecification =
        CertificateActionSpecification.builder()
            .certificateActionType(CertificateActionType.INACTIVE_CERTIFICATE_MODEL)
            .build();

    final var actionSpecifications = TS8071CertificateActionSpecification.create();

    assertTrue(
        actionSpecifications.stream().anyMatch(expectedSpecification::equals),
        "Expected type: %s".formatted(expectedSpecification));
  }
}
