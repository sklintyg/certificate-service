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
package se.inera.intyg.certificateservice.integrationtest.common.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO.SIGNED;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUserDTO.ajlaDoktorDtoBuilder;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUserDTO.alvaVardadministratorDtoBuilder;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUserDTO.annaSjukskoterskaDtoBuilder;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUserDTO.bertilBarnmorskaDtoBuilder;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.customReadyForSignCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.customTestabilityCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.ApiRequestUtil.defaultTestablilityCertificateRequest;
import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificateId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.common.dto.AccessScopeTypeDTO;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseIntegrationIT;

public abstract class CertificateReadyForSignIT extends BaseIntegrationIT {

  @Test
  @DisplayName(
      "Vårdadministratör - Om användaren är en vårdadministratör som loggat in djupintegrerat skall utkastet gå att markera som redo för signering")
  void shallAllowIfCareAdminAndOriginIsDjupintegrerad() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion()));

    final var response =
        api()
            .readyForSignCertificate(
                customReadyForSignCertificateRequest()
                    .user(
                        alvaVardadministratorDtoBuilder()
                            .accessScope(AccessScopeTypeDTO.WITHIN_CARE_PROVIDER)
                            .build())
                    .build(),
                certificateId(testCertificates));

    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  @DisplayName(
      "Barnmorska - Om användaren är en barnmorska som loggat in djupintegrerat skall utkastet gå att markera som redo för signering")
  void shallAllowIfMidWifeAndOriginIsDjupintegrerad() {
    if (!midwifeCanMarkReadyForSignCertificate()) {
      return;
    }
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion()));

    final var response =
        api()
            .readyForSignCertificate(
                customReadyForSignCertificateRequest()
                    .user(
                        bertilBarnmorskaDtoBuilder()
                            .accessScope(AccessScopeTypeDTO.WITHIN_CARE_PROVIDER)
                            .build())
                    .build(),
                certificateId(testCertificates));

    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  @DisplayName(
      "Sjuksköterska - Om användaren är en sjuksköterska som loggat in djupintegrerat skall utkastet gå att markera som redo för signering")
  void shallAllowIfNurseAndOriginIsDjupintegrerad() {
    if (!nurseCanMarkReadyForSignCertificate()) {
      return;
    }
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion()));

    final var response =
        api()
            .readyForSignCertificate(
                customReadyForSignCertificateRequest()
                    .user(
                        annaSjukskoterskaDtoBuilder()
                            .accessScope(AccessScopeTypeDTO.WITHIN_CARE_PROVIDER)
                            .build())
                    .build(),
                certificateId(testCertificates));

    assertEquals(200, response.getStatusCode().value());
  }

  @Test
  @DisplayName(
      "Vårdadministratör - Om användaren är en vårdadministratör som loggat in fristående skall utkastet inte gå att markera som redo för signering")
  void shallNotAllowIfCareAdminAndOriginIsNormal() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion()));

    final var response =
        api()
            .readyForSignCertificate(
                customReadyForSignCertificateRequest()
                    .user(
                        alvaVardadministratorDtoBuilder()
                            .accessScope(AccessScopeTypeDTO.WITHIN_CARE_UNIT)
                            .build())
                    .build(),
                certificateId(testCertificates));

    assertEquals(403, response.getStatusCode().value());
  }

  @Test
  @DisplayName(
      "Läkare - Om användaren är en läkare skall utkastet inte gå att markera som redo för signering")
  void shallNotAllowIfDoctor() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(
                customTestabilityCertificateRequest(type(), typeVersion())
                    .user(
                        ajlaDoktorDtoBuilder()
                            .accessScope(AccessScopeTypeDTO.WITHIN_CARE_PROVIDER)
                            .build())
                    .build());

    final var response =
        api()
            .readyForSignCertificate(
                customReadyForSignCertificateRequest()
                    .user(
                        ajlaDoktorDtoBuilder()
                            .accessScope(AccessScopeTypeDTO.WITHIN_CARE_PROVIDER)
                            .build())
                    .build(),
                certificateId(testCertificates));

    assertEquals(403, response.getStatusCode().value());
  }

  @Test
  @DisplayName("Om intyget är signerat skall felkod 403 (FORBIDDEN) returneras")
  void shallReturn403IfCertificateNotSigned() {
    final var testCertificates =
        testabilityApi()
            .addCertificates(defaultTestablilityCertificateRequest(type(), typeVersion(), SIGNED));

    final var response =
        api()
            .readyForSignCertificate(
                customReadyForSignCertificateRequest()
                    .user(
                        alvaVardadministratorDtoBuilder()
                            .accessScope(AccessScopeTypeDTO.WITHIN_CARE_PROVIDER)
                            .build())
                    .build(),
                certificateId(testCertificates));

    assertEquals(403, response.getStatusCode().value());
  }
}
