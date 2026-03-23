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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateEntity;

@Repository
public interface CertificateEntityRepository
    extends CrudRepository<CertificateEntity, Long>, JpaSpecificationExecutor<CertificateEntity> {

  @Query(
      "SELECT c FROM CertificateEntity c WHERE c.certificateId = :certificateId AND c.placeholder != true")
  Optional<CertificateEntity> findByCertificateId(String certificateId);

  List<CertificateEntity> findCertificateEntitiesByCertificateIdIn(List<String> certificateId);

  void deleteAllByCertificateIdIn(List<String> certificateIds);

  @Query(
      "SELECT c FROM CertificateEntity c WHERE c.careProvider.hsaId = :careProviderHsaId AND c.signed IS NOT null")
  Page<CertificateEntity> findSignedCertificateEntitiesByCareProviderHsaId(
      @Param("careProviderHsaId") String careProviderHsaId, Pageable pageable);

  @Query("SELECT c FROM CertificateEntity c WHERE c.careProvider.hsaId = :careProviderHsaId")
  Page<CertificateEntity> findCertificateEntitiesByCareProviderHsaId(
      @Param("careProviderHsaId") String careProviderHsaId, Pageable pageable);

  @Query(
      "SELECT count(c.certificateId) FROM CertificateEntity c WHERE c.careProvider.hsaId = :careProviderHsaId AND c.revoked IS NOT null")
  long findRevokedCertificateEntitiesByCareProviderHsaId(
      @Param("careProviderHsaId") String careProviderHsaId);

  @Query(
      "SELECT c FROM CertificateEntity c WHERE c.certificateId = :certificateId AND c.placeholder = true")
  Optional<CertificateEntity> findPlaceholderByCertificateId(String certificateId);

  @Query(
      """
        SELECT c.certificateId
        FROM CertificateEntity c
        WHERE c.certificateId IN :certificateIds
          AND c.revoked IS NULL
          AND NOT EXISTS (
            SELECT rel
            FROM CertificateRelationEntity rel
            WHERE rel.parentCertificate = c
              AND rel.childCertificate.revoked IS NULL
              AND rel.childCertificate.signed IS NOT NULL
              AND rel.certificateRelationType.type IN ('COMPLEMENT', 'REPLACE')
          )
      """)
  List<String> findValidSickLeaveCertificatesByIds(List<String> certificateIds);

  @Query(
      "SELECT c.certificateId FROM CertificateEntity c WHERE c.created <= :createdTo AND c.status.key IN :statuses")
  List<String> findCertificateIdsByCreatedBeforeAndStatusIn(
      @Param("createdTo") LocalDateTime createdTo, @Param("statuses") List<Integer> statuses);

  Optional<List<CertificateEntity>> findCertificateEntitiesByPatient_Key(Integer patientKey);
}
