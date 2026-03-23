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
package se.inera.intyg.certificateservice.application.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_ALLERGIMOTTAGNINGEN_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_MEDICINCENTRUM_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_REGIONEN_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUserDTO.AJLA_DOCTOR_DTO;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataStaff.AJLA_DOKTOR;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.application.testdata.TestDataCommonStaffDTO;
import se.inera.intyg.certificateservice.application.unit.dto.GetUnitCertificatesInfoRequest;
import se.inera.intyg.certificateservice.application.unit.dto.GetUnitCertificatesInfoResponse;
import se.inera.intyg.certificateservice.application.unit.service.validator.GetUnitCertificatesInfoRequestValidator;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.common.model.CertificatesRequest;
import se.inera.intyg.certificateservice.domain.unit.service.GetUnitCertificatesInfoDomainService;

@ExtendWith(MockitoExtension.class)
class GetUnitCertificatesInfoServiceTest {

  @Mock private GetUnitCertificatesInfoRequestValidator getUnitCertificatesInfoRequestValidator;
  @Mock private ActionEvaluationFactory actionEvaluationFactory;
  @Mock private GetUnitCertificatesInfoDomainService getUnitCertificatesInfoDomainService;
  @InjectMocks private GetUnitCertificatesInfoService getUnitCertificatesInfoService;

  @Test
  void shallThrowIfInvalidRequest() {
    final var request = GetUnitCertificatesInfoRequest.builder().build();
    doThrow(IllegalArgumentException.class)
        .when(getUnitCertificatesInfoRequestValidator)
        .validate(request);

    assertThrows(IllegalArgumentException.class, () -> getUnitCertificatesInfoService.get(request));
  }

  @Test
  void shallReturnGetUnitCertificatesResponse() {
    final var expectedResponse =
        GetUnitCertificatesInfoResponse.builder()
            .staffs(List.of(TestDataCommonStaffDTO.AJLA_DOKTOR))
            .build();

    final var actionEvaluation = ActionEvaluation.builder().build();
    doReturn(actionEvaluation)
        .when(actionEvaluationFactory)
        .create(
            AJLA_DOCTOR_DTO,
            ALFA_ALLERGIMOTTAGNINGEN_DTO,
            ALFA_MEDICINCENTRUM_DTO,
            ALFA_REGIONEN_DTO);

    doReturn(List.of(AJLA_DOKTOR))
        .when(getUnitCertificatesInfoDomainService)
        .get(any(CertificatesRequest.class), eq(actionEvaluation));

    final var actualResult =
        getUnitCertificatesInfoService.get(
            GetUnitCertificatesInfoRequest.builder()
                .user(AJLA_DOCTOR_DTO)
                .unit(ALFA_ALLERGIMOTTAGNINGEN_DTO)
                .careUnit(ALFA_MEDICINCENTRUM_DTO)
                .careProvider(ALFA_REGIONEN_DTO)
                .build());

    assertEquals(expectedResponse, actualResult);
  }
}
