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
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUser.AJLA_DOKTOR;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUser.ALVA_VARDADMINISTRATOR;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateActionSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.repository.CertificateActionConfigurationRepository;
import se.inera.intyg.certificateservice.domain.common.model.Role;

@ExtendWith(MockitoExtension.class)
class CertificateActionListCertificateTypeTest {

  private CertificateActionListCertificateType certificateActionListCertificateType;
  private ActionEvaluation.ActionEvaluationBuilder actionEvaluationBuilder;
  private static final CertificateActionSpecification CERTIFICATE_ACTION_SPECIFICATION =
      CertificateActionSpecification.builder()
          .certificateActionType(CertificateActionType.LIST_CERTIFICATE_TYPE)
          .allowedRoles(List.of(Role.DOCTOR))
          .build();

  @Mock CertificateActionConfigurationRepository certificateActionConfigurationRepository;
  @InjectMocks CertificateActionFactory certificateActionFactory;

  @BeforeEach
  void setUp() {
    certificateActionListCertificateType =
        (CertificateActionListCertificateType)
            certificateActionFactory.create(CERTIFICATE_ACTION_SPECIFICATION);

    actionEvaluationBuilder = ActionEvaluation.builder().user(AJLA_DOKTOR);
  }

  @Test
  void shallReturnFalseIfRoleIsNotPresentInAllowedRoles() {
    final var actionEvaluation = actionEvaluationBuilder.user(ALVA_VARDADMINISTRATOR).build();

    assertFalse(
        certificateActionListCertificateType.evaluate(
            Optional.empty(), Optional.of(actionEvaluation)));
  }

  @Test
  void shallReturnTrueIfRoleIsPresentInAllowedRoles() {
    final var actionEvaluation = actionEvaluationBuilder.build();

    assertTrue(
        certificateActionListCertificateType.evaluate(
            Optional.empty(), Optional.of(actionEvaluation)));
  }

  @Test
  void shallReturnFalseForInclude() {
    assertFalse(certificateActionListCertificateType.include(Optional.empty(), Optional.empty()));
  }
}
