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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionDiagnos.QUESTION_DIAGNOS_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionFormedlaInfoOmDiagnosTillAG.QUESTION_FORMEDLA_DIAGNOS_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionSmittbararpenning.QUESTION_SMITTBARARPENNING_ID;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CitizenAvailableFunction;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CitizenAvailableFunctionInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CitizenAvailableFunctionInformationType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CitizenAvailableFunctionType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.DefaultCitizenAvailableFunctionsProvider;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;

@ExtendWith(MockitoExtension.class)
class AG7804CertificateCitizenAvailableFunctionsProviderTest {

  private AG7804CitizenAvailableFunctionsProvider provider;

  @Mock private Certificate certificate;
  @Mock private CertificateModel certificateModel;

  @BeforeEach
  void setUp() {
    provider = new AG7804CitizenAvailableFunctionsProvider();
    when(certificate.certificateModel()).thenReturn(certificateModel);
  }

  @Nested
  class SmittbararpenningFunction {

    @Test
    void shouldIncludeAttentionFunctionWhenSmittbararpenningIsTrue() {
      when(certificate.getElementDataById(QUESTION_SMITTBARARPENNING_ID))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(new ElementId(QUESTION_SMITTBARARPENNING_ID.id()))
                      .value(ElementValueBoolean.builder().value(true).build())
                      .build()));
      when(certificate.getElementDataById(QUESTION_FORMEDLA_DIAGNOS_ID))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(new ElementId(QUESTION_FORMEDLA_DIAGNOS_ID.id()))
                      .value(ElementValueBoolean.builder().value(false).build())
                      .build()));
      when(certificateModel.fileName()).thenReturn("ag7804-certificate.pdf");

      final var expectedAttentionFunction =
          CitizenAvailableFunction.builder()
              .type(CitizenAvailableFunctionType.ATTENTION)
              .name("Presentera informationsruta")
              .title("Avstängning enligt smittskyddslagen")
              .body(
                  "I intyg som gäller avstängning enligt smittskyddslagen kan"
                      + " du inte dölja din diagnos. När du klickar på \"Skriv ut intyg\" hämtas hela intyget.")
              .enabled(true)
              .information(List.of())
              .build();

      final var result = provider.of(certificate);

      assertEquals(2, result.size());
      assertEquals(expectedAttentionFunction, result.getFirst());
      assertEquals(CitizenAvailableFunctionType.PRINT_CERTIFICATE, result.get(1).type());
    }

    @Test
    void shouldNotIncludeAttentionFunctionAndNormalPrintWhenSmittbararpenningIsFalse() {
      when(certificate.getElementDataById(QUESTION_SMITTBARARPENNING_ID))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(new ElementId(QUESTION_SMITTBARARPENNING_ID.id()))
                      .value(ElementValueBoolean.builder().value(false).build())
                      .build()));
      when(certificate.getElementDataById(QUESTION_FORMEDLA_DIAGNOS_ID))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(new ElementId(QUESTION_FORMEDLA_DIAGNOS_ID.id()))
                      .value(ElementValueBoolean.builder().value(false).build())
                      .build()));
      when(certificateModel.fileName()).thenReturn("ag7804-certificate.pdf");

      final var result = provider.of(certificate);

      assertEquals(1, result.size());
      assertEquals(CitizenAvailableFunctionType.PRINT_CERTIFICATE, result.getFirst().type());
    }

    @Test
    void shouldNotIncludeAttentionFunctionAndNormalPrintWhenSmittbararpenningElementMissing() {
      when(certificate.getElementDataById(QUESTION_SMITTBARARPENNING_ID))
          .thenReturn(Optional.empty());
      when(certificate.getElementDataById(QUESTION_FORMEDLA_DIAGNOS_ID))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(new ElementId(QUESTION_FORMEDLA_DIAGNOS_ID.id()))
                      .value(ElementValueBoolean.builder().value(false).build())
                      .build()));
      when(certificateModel.fileName()).thenReturn("ag7804-certificate.pdf");

      final var result = provider.of(certificate);

      assertEquals(1, result.size());
      assertEquals(CitizenAvailableFunctionType.PRINT_CERTIFICATE, result.getFirst().type());
    }
  }

  @Nested
  class DiagnosisCustomizationFunction {

    @Test
    void shouldIncludeCustomizePrintFunctionWhenDiagnosisIsIncludedAndSmittbararFalse() {
      when(certificate.getElementDataById(QUESTION_SMITTBARARPENNING_ID))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(new ElementId(QUESTION_SMITTBARARPENNING_ID.id()))
                      .value(ElementValueBoolean.builder().value(false).build())
                      .build()));
      when(certificate.getElementDataById(QUESTION_FORMEDLA_DIAGNOS_ID))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(new ElementId(QUESTION_FORMEDLA_DIAGNOS_ID.id()))
                      .value(ElementValueBoolean.builder().value(true).build())
                      .build()));
      when(certificateModel.fileName()).thenReturn("ag7804-certificate.pdf");

      final var expectedCustomizeFunction =
          CitizenAvailableFunction.builder()
              .type(CitizenAvailableFunctionType.CUSTOMIZE_PRINT_CERTIFICATE)
              .name("Anpassa intyget för utskrift")
              .title("Vill du visa eller dölja diagnos?")
              .body(
                  "När du skriver ut ett läkarintyg du ska lämna till din arbetsgivare kan du "
                      + "välja om du vill att din diagnos ska visas eller döljas. Ingen annan information kan döljas. ")
              .description(
                  "Information om diagnos kan vara viktig för din arbetsgivare."
                      + " Det kan underlätta anpassning av din arbetssituation. Det kan också göra att du snabbare kommer tillbaka till arbetet.")
              .enabled(true)
              .information(
                  List.of(
                      CitizenAvailableFunctionInformation.builder()
                          .type(CitizenAvailableFunctionInformationType.FILENAME)
                          .text("ag7804-certificate.pdf")
                          .build(),
                      CitizenAvailableFunctionInformation.builder()
                          .type(CitizenAvailableFunctionInformationType.OPTIONS)
                          .text("Visa diagnos")
                          .build(),
                      CitizenAvailableFunctionInformation.builder()
                          .id(QUESTION_DIAGNOS_ID)
                          .type(CitizenAvailableFunctionInformationType.OPTIONS)
                          .text("Dölj diagnos")
                          .build()))
              .build();

      final var result = provider.of(certificate);

      assertEquals(1, result.size());
      assertEquals(expectedCustomizeFunction, result.getFirst());
    }

    @Test
    void shouldIncludeNormalPrintFunctionWhenDiagnosisNotIncluded() {
      when(certificate.getElementDataById(QUESTION_SMITTBARARPENNING_ID))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(new ElementId(QUESTION_SMITTBARARPENNING_ID.id()))
                      .value(ElementValueBoolean.builder().value(false).build())
                      .build()));
      when(certificate.getElementDataById(QUESTION_FORMEDLA_DIAGNOS_ID))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(new ElementId(QUESTION_FORMEDLA_DIAGNOS_ID.id()))
                      .value(ElementValueBoolean.builder().value(false).build())
                      .build()));
      when(certificateModel.fileName()).thenReturn("ag7804-certificate.pdf");

      final var result = provider.of(certificate);

      assertEquals(1, result.size());
      assertEquals(CitizenAvailableFunctionType.PRINT_CERTIFICATE, result.getFirst().type());
    }

    @Test
    void shouldIncludeNormalPrintFunctionWhenDiagnosisElementMissing() {
      when(certificate.getElementDataById(QUESTION_SMITTBARARPENNING_ID))
          .thenReturn(
              Optional.of(
                  ElementData.builder()
                      .id(new ElementId(QUESTION_SMITTBARARPENNING_ID.id()))
                      .value(ElementValueBoolean.builder().value(false).build())
                      .build()));
      when(certificate.getElementDataById(QUESTION_FORMEDLA_DIAGNOS_ID))
          .thenReturn(Optional.empty());
      when(certificateModel.fileName()).thenReturn("ag7804-certificate.pdf");

      final var result = provider.of(certificate);

      assertEquals(1, result.size());
      assertEquals(CitizenAvailableFunctionType.PRINT_CERTIFICATE, result.getFirst().type());
    }
  }

  @Nested
  class ReplacedCerificateFunction {

    @Test
    void shouldNotReturnSendAndPrintFunctionIfCertificateIsReplaced() {
      when(certificate.isReplaced()).thenReturn(true);

      final var actual = new DefaultCitizenAvailableFunctionsProvider().of(certificate);

      assertAll(
          () ->
              assertTrue(
                  actual.stream()
                      .noneMatch(
                          function ->
                              function.type() == CitizenAvailableFunctionType.SEND_CERTIFICATE)),
          () ->
              assertTrue(
                  actual.stream()
                      .noneMatch(
                          function ->
                              function.type() == CitizenAvailableFunctionType.PRINT_CERTIFICATE)));
    }

    @Test
    void shouldReturnSendAndPrintFunctionIfCertificateIsNotReplaced() {
      when(certificate.isReplaced()).thenReturn(false);
      when(certificateModel.fileName()).thenReturn("fileName.pdf");
      when(certificate.isSendActiveForCitizen()).thenReturn(true);

      final var actual = new DefaultCitizenAvailableFunctionsProvider().of(certificate);

      assertAll(
          () ->
              assertTrue(
                  actual.stream()
                      .anyMatch(
                          function ->
                              function.type() == CitizenAvailableFunctionType.SEND_CERTIFICATE)),
          () ->
              assertTrue(
                  actual.stream()
                      .anyMatch(
                          function ->
                              function.type() == CitizenAvailableFunctionType.PRINT_CERTIFICATE)));
    }
  }

  @Nested
  class ComplementedFunction {

    @Test
    void shouldNotReturnSendAndPrintFunctionIfCertificateIsComplemented() {
      when(certificate.isComplemented()).thenReturn(true);

      final var actual = new DefaultCitizenAvailableFunctionsProvider().of(certificate);

      assertAll(
          () ->
              assertTrue(
                  actual.stream()
                      .noneMatch(
                          function ->
                              function.type() == CitizenAvailableFunctionType.SEND_CERTIFICATE)),
          () ->
              assertTrue(
                  actual.stream()
                      .noneMatch(
                          function ->
                              function.type() == CitizenAvailableFunctionType.PRINT_CERTIFICATE)));
    }

    @Test
    void shouldReturnSendAndPrintFunctionIfCertificateIsNotComplemented() {
      when(certificate.isComplemented()).thenReturn(false);
      when(certificateModel.fileName()).thenReturn("fileName.pdf");
      when(certificate.isSendActiveForCitizen()).thenReturn(true);

      final var actual = new DefaultCitizenAvailableFunctionsProvider().of(certificate);

      assertAll(
          () ->
              assertTrue(
                  actual.stream()
                      .anyMatch(
                          function ->
                              function.type() == CitizenAvailableFunctionType.SEND_CERTIFICATE)),
          () ->
              assertTrue(
                  actual.stream()
                      .anyMatch(
                          function ->
                              function.type() == CitizenAvailableFunctionType.PRINT_CERTIFICATE)));
    }
  }
}
