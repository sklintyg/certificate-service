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

import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonPatientDTO.ATHENA_REACT_ANDERSSON_PERSON_ID_DTO;
import static se.inera.intyg.certificateservice.application.testdata.TestDataCommonUnitDTO.ALFA_MEDICINCENTRUM_DTO;

import java.time.LocalDate;
import java.util.List;
import se.inera.intyg.certificateservice.application.certificate.dto.GetSickLeaveCertificatesInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.PersonIdDTO;

public class GetSickLeaveCertificatesInternalRequestBuilder {

  private PersonIdDTO personId =
      PersonIdDTO.builder()
          .id(ATHENA_REACT_ANDERSSON_PERSON_ID_DTO.getId())
          .type(ATHENA_REACT_ANDERSSON_PERSON_ID_DTO.getType().name())
          .build();
  private List<String> certificateTypes = List.of("ag7804", "ag114");
  private LocalDate signedFrom = LocalDate.now().minusYears(1);
  private LocalDate signedTo = LocalDate.now().plusYears(1);
  private List<String> issuedByUnitIds = List.of(ALFA_MEDICINCENTRUM_DTO.getId());

  private GetSickLeaveCertificatesInternalRequestBuilder() {}

  public static GetSickLeaveCertificatesInternalRequestBuilder create() {
    return new GetSickLeaveCertificatesInternalRequestBuilder();
  }

  public GetSickLeaveCertificatesInternalRequestBuilder personId(PersonIdDTO personId) {
    this.personId = personId;
    return this;
  }

  public GetSickLeaveCertificatesInternalRequestBuilder certificateTypes(
      List<String> certificateTypes) {
    this.certificateTypes = certificateTypes;
    return this;
  }

  public GetSickLeaveCertificatesInternalRequestBuilder signedFrom(LocalDate signedFrom) {
    this.signedFrom = signedFrom;
    return this;
  }

  public GetSickLeaveCertificatesInternalRequestBuilder signedTo(LocalDate signedTo) {
    this.signedTo = signedTo;
    return this;
  }

  public GetSickLeaveCertificatesInternalRequestBuilder issuedByUnitIds(
      List<String> issuedByUnitIds) {
    this.issuedByUnitIds = issuedByUnitIds;
    return this;
  }

  public GetSickLeaveCertificatesInternalRequest build() {
    return GetSickLeaveCertificatesInternalRequest.builder()
        .personId(personId)
        .certificateTypes(certificateTypes)
        .signedFrom(signedFrom)
        .signedTo(signedTo)
        .issuedByUnitIds(issuedByUnitIds)
        .build();
  }
}
