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
package se.inera.intyg.certificateservice.integrationtest.common.util;

import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonPatientDTO.ATHENA_REACT_ANDERSSON_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_ALLERGIMOTTAGNINGEN_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_MEDICINCENTRUM_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_REGIONEN_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUserDTO.AJLA_DOCTOR_DTO;

import se.inera.intyg.certificateservice.application.certificate.dto.CreateCertificateRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.PrefillXmlDTO;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.CertificateModelIdDTO;
import se.inera.intyg.certificateservice.application.common.dto.PatientDTO;
import se.inera.intyg.certificateservice.application.common.dto.UnitDTO;
import se.inera.intyg.certificateservice.application.common.dto.UserDTO;

public class CreateCertificateRequestBuilder {

  private static final String VERSION = "1.0";
  private static final String TYPE = "fk7210";

  private UnitDTO unit = ALFA_ALLERGIMOTTAGNINGEN_DTO;
  private UserDTO user = AJLA_DOCTOR_DTO;
  private PatientDTO patient = ATHENA_REACT_ANDERSSON_DTO;
  private PrefillXmlDTO prefillXml;

  private CertificateModelIdDTO certificateModelId =
      CertificateModelIdDTO.builder().version(VERSION).type(TYPE).build();

  public static CreateCertificateRequestBuilder create() {
    return new CreateCertificateRequestBuilder();
  }

  private CreateCertificateRequestBuilder() {}

  public CreateCertificateRequestBuilder user(UserDTO user) {
    this.user = user;
    return this;
  }

  public CreateCertificateRequestBuilder unit(UnitDTO unit) {
    this.unit = unit;
    return this;
  }

  public CreateCertificateRequestBuilder patient(PatientDTO patient) {
    this.patient = patient;
    return this;
  }

  public CreateCertificateRequestBuilder certificateModelId(
      CertificateModelIdDTO certificateModelIdDTO) {
    this.certificateModelId = certificateModelIdDTO;
    return this;
  }

  public CreateCertificateRequestBuilder prefillXml(PrefillXmlDTO prefillXml) {
    this.prefillXml = prefillXml;
    return this;
  }

  public CreateCertificateRequest build() {
    return CreateCertificateRequest.builder()
        .user(user)
        .patient(patient)
        .careProvider(ALFA_REGIONEN_DTO)
        .unit(unit)
        .careUnit(ALFA_MEDICINCENTRUM_DTO)
        .certificateModelId(certificateModelId)
        .prefillXml(prefillXml)
        .build();
  }
}
