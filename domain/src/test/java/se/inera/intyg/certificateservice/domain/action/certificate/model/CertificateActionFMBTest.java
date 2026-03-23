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
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProvider.ALFA_REGIONEN;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareUnit.ALFA_MEDICINCENTRUM;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataPatient.ATHENA_REACT_ANDERSSON;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataSubUnit.ALFA_ALLERGIMOTTAGNINGEN;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataUser.AJLA_DOKTOR;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation.ActionEvaluationBuilder;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateActionSpecification;

@ExtendWith(MockitoExtension.class)
class CertificateActionFMBTest {

  private CertificateActionFMB certificateActionFMB;

  private static final CertificateActionSpecification CERTIFICATE_ACTION_SPECIFICATION =
      CertificateActionSpecification.builder()
          .certificateActionType(CertificateActionType.FMB)
          .build();
  private MedicalCertificate.MedicalCertificateBuilder certificateBuilder;
  private ActionEvaluationBuilder actionEvaluationBuilder;

  @InjectMocks CertificateActionFactory certificateActionFactory;

  @BeforeEach
  void setUp() {
    certificateActionFMB =
        (CertificateActionFMB) certificateActionFactory.create(CERTIFICATE_ACTION_SPECIFICATION);

    actionEvaluationBuilder =
        ActionEvaluation.builder()
            .user(AJLA_DOKTOR)
            .subUnit(ALFA_ALLERGIMOTTAGNINGEN)
            .patient(ATHENA_REACT_ANDERSSON)
            .careProvider(ALFA_REGIONEN)
            .careUnit(ALFA_MEDICINCENTRUM);

    certificateBuilder = MedicalCertificate.builder().status(Status.DRAFT);
  }

  @Test
  void shallReturnName() {
    assertEquals("FMB", certificateActionFMB.getName(Optional.empty()));
  }

  @Test
  void shallReturnType() {
    assertEquals(CertificateActionType.FMB, certificateActionFMB.getType());
  }

  @Test
  void shallReturnDescription() {
    assertEquals(
        "Läs FMB - ett stöd för ifyllnad och bedömning.",
        certificateActionFMB.getDescription(Optional.empty()));
  }

  @Test
  void shallReturnReasonNotAllowed() {
    assertEquals(
        "För att genomföra den begärda åtgärden behöver intygets status vara [DRAFT]",
        certificateActionFMB
            .reasonNotAllowed(
                Optional.of(certificateBuilder.status(Status.SIGNED).build()),
                Optional.of(actionEvaluationBuilder.build()))
            .getFirst());
  }

  @Test
  void shallEvaluateFalseIfCertificateIsNotDraft() {
    assertFalse(
        certificateActionFMB.evaluate(
            Optional.of(certificateBuilder.status(Status.SIGNED).build()),
            Optional.of(actionEvaluationBuilder.build())));
  }

  @Test
  void shallEvaluateTrueIfCertificateIsDraft() {
    assertTrue(
        certificateActionFMB.evaluate(
            Optional.of(certificateBuilder.build()), Optional.of(actionEvaluationBuilder.build())));
  }
}
