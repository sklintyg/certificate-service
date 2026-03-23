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
package se.inera.intyg.certificateservice.application.certificate.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateMetadataDTO.CertificateMetadataDTOBuilder;

@JsonDeserialize(builder = CertificateMetadataDTOBuilder.class)
@Value
@Builder
public class CertificateMetadataDTO {

  String id;
  String type;
  String typeVersion;
  String typeName;
  String name;
  String description;
  LocalDateTime created;
  CertificateStatusTypeDTO status;
  boolean testCertificate;
  boolean forwarded;
  boolean sent;
  boolean availableForCitizen;
  String sentTo;
  CertificateRelationsDTO relations;
  @With UnitDTO unit;
  UnitDTO careUnit;
  UnitDTO careProvider;
  PatientDTO patient;
  StaffDTO issuedBy;
  long version;
  boolean latestMajorVersion;
  LocalDateTime readyForSign;
  LocalDateTime signed;
  LocalDateTime modified;
  String responsibleHospName;
  CertificateRecipientDTO recipient;
  CertificateSummaryDTO summary;
  boolean validForSign;
  String externalReference;
  List<CertificateMessageTypeDTO> messageTypes;
  CertificateConfirmationModalDTO confirmationModal;
  StaffDTO createdBy;
  LocalDateTime revokedAt;
  StaffDTO revokedBy;
  boolean isInactiveCertificateType;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CertificateMetadataDTOBuilder {}
}
