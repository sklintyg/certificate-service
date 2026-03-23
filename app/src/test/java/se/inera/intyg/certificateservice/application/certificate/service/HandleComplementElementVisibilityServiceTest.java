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
package se.inera.intyg.certificateservice.application.certificate.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDataElement;
import se.inera.intyg.certificateservice.application.certificate.service.converter.ComplementElementVisibilityCheckboxMultipleCode;
import se.inera.intyg.certificateservice.application.certificate.service.converter.HandleComplementElementVisibilityService;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.MedicalCertificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementVisibilityConfigurationsCheckboxMultipleCode;

@ExtendWith(MockitoExtension.class)
class HandleComplementElementVisibilityServiceTest {

  private static final ElementId COMPLEMENT_ID = new ElementId("complementId");
  private static final Map<String, CertificateDataElement> DATA_ELEMENT_MAP =
      Collections.emptyMap();
  private static final ElementVisibilityConfigurationsCheckboxMultipleCode
      VISIBILITY_CONFIGURATION =
          ElementVisibilityConfigurationsCheckboxMultipleCode.builder().build();
  private static final ElementId MISSING_COMPLEMENT_ID = new ElementId("missingComplementId");

  @Mock
  ComplementElementVisibilityCheckboxMultipleCode complementElementVisibilityCheckboxMultipleCode;

  HandleComplementElementVisibilityService handleComplementElementVisibilityService;

  @BeforeEach
  void setUp() {
    handleComplementElementVisibilityService =
        new HandleComplementElementVisibilityService(
            List.of(complementElementVisibilityCheckboxMultipleCode));
  }

  @Test
  void shouldNotProceedIfShouldValidateIsNull() {
    final var certificate =
        MedicalCertificate.builder()
            .certificateModel(
                CertificateModel.builder()
                    .elementSpecifications(
                        List.of(ElementSpecification.builder().id(COMPLEMENT_ID).build()))
                    .build())
            .build();

    handleComplementElementVisibilityService.handle(
        COMPLEMENT_ID, DATA_ELEMENT_MAP, certificate, VISIBILITY_CONFIGURATION);

    verifyNoInteractions(complementElementVisibilityCheckboxMultipleCode);
  }

  @Test
  void shouldNotProceedIfShouldValidateReturnsTrue() {
    final var certificate =
        MedicalCertificate.builder()
            .elementData(List.of(ElementData.builder().id(COMPLEMENT_ID).build()))
            .certificateModel(
                CertificateModel.builder()
                    .elementSpecifications(
                        List.of(
                            ElementSpecification.builder()
                                .id(COMPLEMENT_ID)
                                .shouldValidate(
                                    elementData ->
                                        elementData.getFirst().id().equals(COMPLEMENT_ID))
                                .build()))
                    .build())
            .build();

    handleComplementElementVisibilityService.handle(
        COMPLEMENT_ID, DATA_ELEMENT_MAP, certificate, VISIBILITY_CONFIGURATION);

    verifyNoInteractions(complementElementVisibilityCheckboxMultipleCode);
  }

  @Test
  void shouldThrowIfNoVisibilityServiceIsFoundForType() {
    final var certificate =
        MedicalCertificate.builder()
            .elementData(List.of(ElementData.builder().id(MISSING_COMPLEMENT_ID).build()))
            .certificateModel(
                CertificateModel.builder()
                    .elementSpecifications(
                        List.of(
                            ElementSpecification.builder()
                                .id(COMPLEMENT_ID)
                                .shouldValidate(
                                    elementData ->
                                        elementData.getFirst().id().equals(COMPLEMENT_ID))
                                .build()))
                    .build())
            .build();

    when(complementElementVisibilityCheckboxMultipleCode.supports(VISIBILITY_CONFIGURATION))
        .thenReturn(false);

    assertThrows(
        IllegalStateException.class,
        () ->
            handleComplementElementVisibilityService.handle(
                COMPLEMENT_ID, DATA_ELEMENT_MAP, certificate, VISIBILITY_CONFIGURATION));
  }

  @Test
  void shouldHandleVisibilityIfVisibilityServiceIsFoundForType() {
    final var certificate =
        MedicalCertificate.builder()
            .elementData(List.of(ElementData.builder().id(MISSING_COMPLEMENT_ID).build()))
            .certificateModel(
                CertificateModel.builder()
                    .elementSpecifications(
                        List.of(
                            ElementSpecification.builder()
                                .id(COMPLEMENT_ID)
                                .shouldValidate(
                                    elementData ->
                                        elementData.getFirst().id().equals(COMPLEMENT_ID))
                                .build()))
                    .build())
            .build();

    when(complementElementVisibilityCheckboxMultipleCode.supports(VISIBILITY_CONFIGURATION))
        .thenReturn(true);

    handleComplementElementVisibilityService.handle(
        COMPLEMENT_ID, DATA_ELEMENT_MAP, certificate, VISIBILITY_CONFIGURATION);

    verify(complementElementVisibilityCheckboxMultipleCode)
        .handle(DATA_ELEMENT_MAP, VISIBILITY_CONFIGURATION);
  }
}
