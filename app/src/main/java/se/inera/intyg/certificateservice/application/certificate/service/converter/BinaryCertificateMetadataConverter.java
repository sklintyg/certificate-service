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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import static se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationUnitContactInformation.UNIT_CONTACT_INFORMATION;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.BinaryCertificateMetadataDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.BinaryStaffDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.BinaryUnitDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateRelationDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateRelationTypeDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateRelationsDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateStatusTypeDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateTypeDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.PatientDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.PersonIdDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Relation;
import se.inera.intyg.certificateservice.domain.common.model.HealthCareProfessionalLicence;
import se.inera.intyg.certificateservice.domain.common.model.PaTitle;
import se.inera.intyg.certificateservice.domain.common.model.Speciality;
import se.inera.intyg.certificateservice.domain.staff.model.Staff;
import se.inera.intyg.certificateservice.domain.unit.model.CareProvider;
import se.inera.intyg.certificateservice.domain.unit.model.IssuingUnit;

/**
 * Converts a {@link Certificate} aggregate (and its rendered PDF) into a {@link
 * BinaryCertificateMetadataDTO} for the internal binary certificate API. Reuses the existing CS
 * DTOs (StaffDTO, UnitDTO, PatientDTO, CertificateRelationsDTO, CertificateStatusTypeDTO) instead
 * of introducing a TKB-literal DTO tree — mapping to the formal TKB `BinartIntyg` XML shape is
 * Webcert's responsibility.
 */
@Component
@RequiredArgsConstructor
public class BinaryCertificateMetadataConverter {

  private final BinaryCertificateUnitConverter binaryCertificateUnitConverter;

  public BinaryCertificateMetadataDTO convert(Certificate certificate) {
    return BinaryCertificateMetadataDTO.builder()
        .certificateId(certificate.id().id())
        .type(toCertificateTypeDTO(certificate))
        .version(certificate.certificateModel().id().version().version())
        .signedAt(certificate.signed())
        .revokedAt(certificate.revoked().revokedAt())
        .sentAt(certificate.sent().sentAt())
        .patient(toPatientDTO(certificate))
        .issuedBy(toBinaryStaffDTO(certificate))
        .relations(toRelations(certificate.parent(), certificate.children()))
        .build();
  }

  private CertificateTypeDTO toCertificateTypeDTO(Certificate certificate) {
    final var type = certificate.certificateModel().type();
    return CertificateTypeDTO.builder()
        .code(type.code())
        .codeSystem(type.codeSystem())
        .displayName(type.displayName())
        .build();
  }

  private PatientDTO toPatientDTO(Certificate certificate) {
    final var patient = certificate.certificateMetaData().patient();
    return PatientDTO.builder()
        .personId(
            PersonIdDTO.builder()
                .id(patient.id().idWithDash())
                .type(patient.id().type().name())
                .build())
        .firstName(patient.name().firstName())
        .middleName(patient.name().middleName())
        .lastName(patient.name().lastName())
        .street(patient.address() != null ? patient.address().street() : "")
        .city(patient.address() != null ? patient.address().city() : "")
        .zipCode(patient.address() != null ? patient.address().zipCode() : "")
        .build();
  }

  private BinaryStaffDTO toBinaryStaffDTO(Certificate certificate) {
    final var staff = certificate.certificateMetaData().issuer();
    return BinaryStaffDTO.builder()
        .personId(staff.hsaId().id())
        .firstName(staff.name().firstName())
        .middleName(staff.name().middleName())
        .lastName(staff.name().lastName())
        .fullName(staff.name().fullName())
        .titles(toTitles(staff))
        .specialities(toSpecialities(staff))
        .licences(toLicences(staff))
        .unit(toUnitDTO(certificate))
        .build();
  }

  private List<CertificateTypeDTO> toTitles(Staff staff) {
    return staff.paTitles().stream()
        .map(
            paTitle ->
                CertificateTypeDTO.builder()
                    .code(paTitle.code())
                    .codeSystem(PaTitle.OID)
                    .displayName(paTitle.description())
                    .build())
        .toList();
  }

  private List<String> toSpecialities(Staff staff) {
    return staff.specialities().stream().map(Speciality::value).toList();
  }

  private List<CertificateTypeDTO> toLicences(Staff staff) {
    return staff.healthCareProfessionalLicence().stream()
        .map(HealthCareProfessionalLicence::code)
        .map(
            code ->
                CertificateTypeDTO.builder()
                    .code(code.code())
                    .codeSystem(code.codeSystem())
                    .displayName(code.displayName())
                    .build())
        .toList();
  }

  private BinaryUnitDTO toUnitDTO(Certificate certificate) {
    final var issuingUnit = certificate.certificateMetaData().issuingUnit();
    final var baseUnit =
        binaryCertificateUnitConverter.convert(
            issuingUnit,
            certificate.elementData().stream()
                .filter(data -> data.id().equals(UNIT_CONTACT_INFORMATION))
                .findFirst());

    return baseUnit
        .withWorkplaceCode(workplaceCode(issuingUnit))
        .withCareProvider(toCareProviderDTO(certificate.certificateMetaData().careProvider()));
  }

  private String workplaceCode(IssuingUnit issuingUnit) {
    return issuingUnit.workplaceCode() != null ? issuingUnit.workplaceCode().code() : null;
  }

  private BinaryUnitDTO toCareProviderDTO(CareProvider careProvider) {
    return BinaryUnitDTO.builder()
        .unitId(careProvider.hsaId().id())
        .unitName(careProvider.name().name())
        .build();
  }

  private CertificateRelationsDTO toRelations(Relation parent, List<Relation> children) {
    return CertificateRelationsDTO.builder()
        .parent(toRelation(parent))
        .children(children.stream().filter(Objects::nonNull).map(this::toRelation).toList())
        .build();
  }

  private CertificateRelationDTO toRelation(Relation relation) {
    if (relation == null) {
      return null;
    }

    return CertificateRelationDTO.builder()
        .certificateId(relation.certificate().id().id())
        .type(CertificateRelationTypeDTO.toType(relation.type()))
        .status(CertificateStatusTypeDTO.toType(relation.certificate().status()))
        .created(relation.created())
        .build();
  }
}
