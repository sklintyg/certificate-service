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
package se.inera.intyg.certificateservice.application.common;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.GetSickLeaveCertificatesInternalRequest;
import se.inera.intyg.certificateservice.application.unit.dto.CertificatesQueryCriteriaDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateType;
import se.inera.intyg.certificateservice.domain.common.model.CertificatesRequest;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;
import se.inera.intyg.certificateservice.domain.common.model.PersonIdType;

public class CertificatesRequestFactory {

  public static CertificatesRequest create() {
    return CertificatesRequest.builder().statuses(Status.unsigned()).build();
  }

  public static CertificatesRequest create(CertificatesQueryCriteriaDTO queryCriteria) {
    return CertificatesRequest.builder()
        .modifiedFrom(queryCriteria.getFrom())
        .modifiedTo(queryCriteria.getTo())
        .issuedByStaffIds(
            queryCriteria.getIssuedByStaffId() != null
                ? List.of(new HsaId(queryCriteria.getIssuedByStaffId()))
                : null)
        .personId(
            queryCriteria.getPersonId() != null
                ? PersonId.builder()
                    .id(queryCriteria.getPersonId().getId())
                    .type(PersonIdType.valueOf(queryCriteria.getPersonId().getType()))
                    .build()
                : null)
        .statuses(
            queryCriteria.getStatuses() == null
                ? Collections.emptyList()
                : queryCriteria.getStatuses().stream()
                    .map(CertificatesRequestFactory::convertStatus)
                    .filter(Objects::nonNull)
                    .toList())
        .validCertificates(queryCriteria.getValidForSign())
        .build();
  }

  public static CertificatesRequest convert(GetSickLeaveCertificatesInternalRequest request) {
    return CertificatesRequest.builder()
        .personId(
            PersonId.builder()
                .id(request.getPersonId().getId())
                .type(PersonIdType.valueOf(request.getPersonId().getType()))
                .build())
        .signedFrom(request.getSignedFrom())
        .signedTo(request.getSignedTo())
        .types(request.getCertificateTypes().stream().map(CertificateType::new).toList())
        .issuedUnitIds(request.getIssuedByUnitIds().stream().map(HsaId::new).toList())
        .issuedByStaffIds(request.getIssuedByStaffIds().stream().map(HsaId::new).toList())
        .build();
  }

  private static Status convertStatus(CertificateStatusTypeDTO status) {
    return switch (status) {
      case UNSIGNED -> Status.DRAFT;
      case SIGNED -> Status.SIGNED;
      case LOCKED -> Status.LOCKED_DRAFT;
      case LOCKED_REVOKED, REVOKED -> null;
    };
  }
}
