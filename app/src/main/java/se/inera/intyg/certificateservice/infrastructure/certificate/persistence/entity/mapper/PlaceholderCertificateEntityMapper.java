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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateMetaData;
import se.inera.intyg.certificateservice.domain.certificate.model.PlaceholderCertificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Status;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.UnitRepository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateStatus;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateStatusEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.CertificateEntityRepository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.CertificateModelEntityRepository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.PatientEntityRepository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.StaffEntityRepository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository.UnitEntityRepository;

@Component
@RequiredArgsConstructor
public class PlaceholderCertificateEntityMapper {

  private static final String PLACEHOLDER = "PLACEHOLDER";
  private final CertificateEntityRepository certificateEntityRepository;
  private final CertificateModelEntityRepository certificateModelEntityRepository;
  private final PatientEntityRepository patientEntityRepository;
  private final StaffEntityRepository staffEntityRepository;
  private final UnitEntityRepository unitEntityRepository;
  private final UnitRepository unitRepository;

  public CertificateEntity toEntity(Certificate certificate) {
    final var certificateEntity =
        certificateEntityRepository
            .findPlaceholderByCertificateId(certificate.id().id())
            .orElse(toCertificateEntity(certificate));

    final var certificateStatus = CertificateStatus.valueOf(certificate.status().name());
    certificateEntity.setStatus(
        CertificateStatusEntity.builder()
            .key(certificateStatus.getKey())
            .status(certificateStatus.name())
            .build());

    certificateEntity.setPatient(patientEntityRepository.findById(PLACEHOLDER).orElseThrow());
    certificateEntity.setCareProvider(unitEntityRepository.findByHsaId(PLACEHOLDER).orElseThrow());

    certificateEntity.setCareUnit(unitEntityRepository.findByHsaId(PLACEHOLDER).orElseThrow());

    certificateEntity.setIssuedOnUnit(
        unitRepository.issuingUnit(certificate.certificateMetaData().issuingUnit()));
    certificateEntity.setIssuedBy(staffEntityRepository.findByHsaId(PLACEHOLDER).orElseThrow());

    certificateEntity.setCreatedBy(certificateEntity.getIssuedBy());

    certificateEntity.setCertificateModel(
        certificateModelEntityRepository
            .findByTypeAndVersion(PLACEHOLDER, PLACEHOLDER)
            .orElseThrow());

    certificateEntity.setPlaceholder(true);

    return certificateEntity;
  }

  public PlaceholderCertificate toDomain(CertificateEntity certificateEntity) {
    return PlaceholderCertificate.builder()
        .id(new CertificateId(certificateEntity.getCertificateId()))
        .status(Status.valueOf(certificateEntity.getStatus().getStatus()))
        .created(certificateEntity.getCreated())
        .certificateMetaData(
            CertificateMetaData.builder()
                .issuingUnit(
                    UnitEntityMapper.toIssuingUnitDomain(certificateEntity.getIssuedOnUnit()))
                .build())
        .build();
  }

  private CertificateEntity toCertificateEntity(Certificate certificate) {
    return CertificateEntity.builder()
        .certificateId(certificate.id().id())
        .created(LocalDateTime.now())
        .modified(LocalDateTime.now())
        .revision(0L)
        .build();
  }
}
