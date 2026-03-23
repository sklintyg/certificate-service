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
package se.inera.intyg.certificateservice.domain.common.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateType;

@Value
@Builder
public class CertificatesRequest {

  LocalDateTime modifiedFrom;
  LocalDateTime modifiedTo;
  LocalDateTime createdFrom;
  LocalDateTime createdTo;
  List<Status> statuses;
  List<CertificateType> types;
  List<HsaId> issuedByStaffIds;
  @With List<HsaId> issuedUnitIds;
  @With HsaId careUnitId;
  PersonId personId;
  Boolean validCertificates;
  LocalDate signedFrom;
  LocalDate signedTo;

  public CertificatesRequest apply(ActionEvaluation actionEvaluation) {
    if (issuedUnitIds() == null && actionEvaluation.isIssuingUnitSubUnit()) {
      return this.withIssuedUnitIds(List.of(actionEvaluation.subUnit().hsaId()));
    }

    if (issuedUnitIds() == null && actionEvaluation.isIssuingUnitCareUnit()) {
      return this.withCareUnitId(actionEvaluation.careUnit().hsaId());
    }

    return this;
  }
}
