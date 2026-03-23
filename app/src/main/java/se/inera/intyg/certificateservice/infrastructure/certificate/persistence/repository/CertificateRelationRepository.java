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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateRelationEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateRelationType;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateRelationTypeEntity;

@Repository
@RequiredArgsConstructor
public class CertificateRelationRepository {

  private final CertificateEntityRepository certificateEntityRepository;
  private final CertificateRelationEntityRepository relationEntityRepository;

  public void save(Certificate certificate, CertificateEntity certificateEntity) {
    if (certificate.parent() == null) {
      return;
    }

    final var certificateRelations =
        relationEntityRepository.findByChildCertificate(certificateEntity);

    if (certificateRelations.isEmpty()) {
      final var parentEntity = getParentEntity(certificate);
      final var relationType = CertificateRelationType.valueOf(certificate.parent().type().name());

      relationEntityRepository.save(
          CertificateRelationEntity.builder()
              .parentCertificate(parentEntity)
              .childCertificate(certificateEntity)
              .created(certificate.parent().created())
              .certificateRelationType(
                  CertificateRelationTypeEntity.builder()
                      .key(relationType.getKey())
                      .type(relationType.name())
                      .build())
              .build());
    }
  }

  public List<CertificateRelationEntity> relations(CertificateEntity certificateEntity) {
    return relationEntityRepository.findByParentCertificateOrChildCertificate(
        certificateEntity, certificateEntity);
  }

  public void deleteRelations(CertificateEntity certificateEntity) {
    relationEntityRepository.deleteAll(relations(certificateEntity));
  }

  private CertificateEntity getParentEntity(Certificate certificate) {
    if (certificate.parent().certificate().isPlaceholder()) {
      return getCertificate(
          certificateEntityRepository.findPlaceholderByCertificateId(
              certificate.parent().certificate().id().id()),
          certificate);
    }

    return getCertificate(
        certificateEntityRepository.findByCertificateId(
            certificate.parent().certificate().id().id()),
        certificate);
  }

  private CertificateEntity getCertificate(
      Optional<CertificateEntity> certificateEntityRepository, Certificate certificate) {
    return certificateEntityRepository.orElseThrow(
        () ->
            new IllegalStateException(
                "Parent certificate with id '%s' not found"
                    .formatted(certificate.parent().certificate().id().id())));
  }
}
