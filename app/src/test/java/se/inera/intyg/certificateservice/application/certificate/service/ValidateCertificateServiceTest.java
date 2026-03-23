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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonPatientDTO.ATHENA_REACT_ANDERSSON_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_ALLERGIMOTTAGNINGEN_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_MEDICINCENTRUM_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_REGIONEN_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUserDTO.AJLA_DOCTOR_DTO;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.ValidateCertificateResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.ValidationErrorDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.config.ValidateCertificateRequest;
import se.inera.intyg.certificateservice.application.certificate.service.converter.ElementCertificateConverter;
import se.inera.intyg.certificateservice.application.certificate.service.validation.ValidateCertificateRequestValidator;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.service.ValidateCertificateDomainService;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ErrorMessage;
import se.inera.intyg.certificateservice.domain.validation.model.ValidationError;
import se.inera.intyg.certificateservice.domain.validation.model.ValidationResult;

@ExtendWith(MockitoExtension.class)
class ValidateCertificateServiceTest {

  private static final String CERTIFICATE_ID = "certificateId";
  private static final String ELEMENT_ID = "elementId";
  private static final String CATEGORY_ID = "categoryId";
  private static final String FIELD_ID = "code";
  private static final String MESSAGE = "text";
  @Mock private ActionEvaluationFactory actionEvaluationFactory;
  @Mock private ElementCertificateConverter elementCertificateConverter;
  @Mock private ValidateCertificateRequestValidator validateCertificateRequestValidator;
  @Mock private ValidateCertificateDomainService validateCertificateDomainService;

  @InjectMocks private ValidateCertificateService validateCertificateService;

  @Test
  void shallThrowIfRequestIsInvalid() {
    final var request = ValidateCertificateRequest.builder().build();
    doThrow(IllegalArgumentException.class)
        .when(validateCertificateRequestValidator)
        .validate(request, CERTIFICATE_ID);
    assertThrows(
        IllegalArgumentException.class,
        () -> validateCertificateService.validate(request, CERTIFICATE_ID));
  }

  @Test
  void shallReturnValidateCertificateResponse() {
    final var expectedValidationError =
        ValidationErrorDTO.builder()
            .id(ELEMENT_ID)
            .category(CATEGORY_ID)
            .field(FIELD_ID)
            .text(MESSAGE)
            .build();
    final var expectedValidiationErrors = List.of(expectedValidationError);
    final var expectedResult =
        ValidateCertificateResponse.builder().validationErrors(expectedValidiationErrors).build();

    final var validationErrors =
        ValidationResult.builder()
            .errors(
                List.of(
                    ValidationError.builder()
                        .elementId(new ElementId(ELEMENT_ID))
                        .fieldId(new FieldId(FIELD_ID))
                        .categoryId(new ElementId(CATEGORY_ID))
                        .message(new ErrorMessage(MESSAGE))
                        .build()))
            .build();

    final var certificateDTO = CertificateDTO.builder().build();

    final var actionEvaluation = ActionEvaluation.builder().build();
    doReturn(actionEvaluation)
        .when(actionEvaluationFactory)
        .create(
            ATHENA_REACT_ANDERSSON_DTO,
            AJLA_DOCTOR_DTO,
            ALFA_ALLERGIMOTTAGNINGEN_DTO,
            ALFA_MEDICINCENTRUM_DTO,
            ALFA_REGIONEN_DTO);
    final var elementData = ElementData.builder().build();
    final var elementDataList = List.of(elementData, elementData);

    doReturn(elementDataList).when(elementCertificateConverter).convert(certificateDTO);

    doReturn(validationErrors)
        .when(validateCertificateDomainService)
        .validate(new CertificateId(CERTIFICATE_ID), elementDataList, actionEvaluation);

    final var actualResult =
        validateCertificateService.validate(
            ValidateCertificateRequest.builder()
                .user(AJLA_DOCTOR_DTO)
                .patient(ATHENA_REACT_ANDERSSON_DTO)
                .unit(ALFA_ALLERGIMOTTAGNINGEN_DTO)
                .careUnit(ALFA_MEDICINCENTRUM_DTO)
                .careProvider(ALFA_REGIONEN_DTO)
                .certificate(certificateDTO)
                .build(),
            CERTIFICATE_ID);

    assertEquals(expectedResult, actualResult);
  }
}
