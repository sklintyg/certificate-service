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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvIntygetGallerFor.TAXI;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvIntygetGallerFor.UTLANDSKT;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionIntygetAvser.QUESTION_INTYGET_AVSER_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionIntygetAvser.questionIntygetAvser;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCodeList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

@ExtendWith(MockitoExtension.class)
class TS8071CertificateSummaryProviderTest {

  @InjectMocks private TS8071CertificateSummaryProvider provider;

  @Test
  void shallIncludeLabel() {
    assertEquals("Intyget avser", provider.summaryOf(MedicalCertificate.builder().build()).label());
  }

  @Nested
  class ValueTests {

    @Test
    void shallReturnEmptyValueIfCertificateSignedIsNull() {
      final var certificate = MedicalCertificate.builder().signed(null).build();

      assertTrue(provider.summaryOf(certificate).value().isEmpty());
    }

    @Test
    void shallReturnEmptyValueIfCertificateCertificateDontContainCodeValue() {
      final var certificate =
          MedicalCertificate.builder()
              .signed(LocalDateTime.now())
              .certificateModel(
                  CertificateModel.builder()
                      .elementSpecifications(List.of(questionIntygetAvser()))
                      .build())
              .elementData(List.of(ElementData.builder().id(new ElementId("notCodeId")).build()))
              .build();

      assertTrue(provider.summaryOf(certificate).value().isEmpty());
    }

    @Test
    void shallThrowIfCertificateCertificateContainCodeValueButWrongType() {
      final var certificate =
          MedicalCertificate.builder()
              .signed(LocalDateTime.now())
              .certificateModel(
                  CertificateModel.builder()
                      .elementSpecifications(List.of(questionIntygetAvser()))
                      .build())
              .elementData(
                  List.of(
                      ElementData.builder()
                          .id(QUESTION_INTYGET_AVSER_ID)
                          .value(ElementValueText.builder().build())
                          .build()))
              .build();

      assertThrows(IllegalStateException.class, () -> provider.summaryOf(certificate));
    }

    @Test
    void shallReturnValueFromCodeIfCertificateContainCodeValue() {
      final var expected = TAXI.displayName();
      final var certificate =
          MedicalCertificate.builder()
              .signed(LocalDateTime.now())
              .certificateModel(
                  CertificateModel.builder()
                      .elementSpecifications(List.of(questionIntygetAvser()))
                      .build())
              .elementData(
                  List.of(
                      ElementData.builder()
                          .id(QUESTION_INTYGET_AVSER_ID)
                          .value(
                              ElementValueCodeList.builder()
                                  .list(
                                      List.of(
                                          ElementValueCode.builder()
                                              .codeId(new FieldId(TAXI.code()))
                                              .code(TAXI.code())
                                              .build()))
                                  .build())
                          .build()))
              .build();

      assertEquals(expected, provider.summaryOf(certificate).value());
    }

    @Test
    void shallReturnValueFromCodesIfCertificateContainSeveralCodeValues() {
      final var expected = String.format("%s, %s", TAXI.displayName(), UTLANDSKT.displayName());
      final var certificate =
          MedicalCertificate.builder()
              .signed(LocalDateTime.now())
              .certificateModel(
                  CertificateModel.builder()
                      .elementSpecifications(List.of(questionIntygetAvser()))
                      .build())
              .elementData(
                  List.of(
                      ElementData.builder()
                          .id(QUESTION_INTYGET_AVSER_ID)
                          .value(
                              ElementValueCodeList.builder()
                                  .list(
                                      List.of(
                                          ElementValueCode.builder()
                                              .codeId(new FieldId(TAXI.code()))
                                              .code(TAXI.code())
                                              .build(),
                                          ElementValueCode.builder()
                                              .codeId(new FieldId(UTLANDSKT.code()))
                                              .code(UTLANDSKT.code())
                                              .build()))
                                  .build())
                          .build()))
              .build();

      assertEquals(expected, provider.summaryOf(certificate).value());
    }
  }
}
