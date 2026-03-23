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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.PatientVersionEntity;

@Repository
public interface PatientVersionEntityRepository
    extends JpaRepository<PatientVersionEntity, Integer> {

  @Query(
      "SELECT pv FROM PatientVersionEntity pv "
          + "JOIN pv.patient p "
          + "WHERE p.id = :id "
          + "ORDER BY pv.validFrom DESC "
          + "LIMIT 1")
  Optional<PatientVersionEntity> findFirstByIdOrderByValidFromDesc(@Param("id") String id);

  @Query(
      "SELECT pv FROM PatientVersionEntity pv "
          + "JOIN pv.patient p "
          + "WHERE p.id = :id "
          + "AND (pv.validFrom IS NULL OR pv.validFrom <= :ts) "
          + "AND pv.validTo >= :ts "
          + "ORDER BY CASE WHEN pv.validFrom IS NULL THEN 1 ELSE 0 END, pv.validFrom DESC")
  Optional<PatientVersionEntity> findFirstCoveringTimestampOrderByMostRecent(
      @Param("id") String id, @Param("ts") LocalDateTime ts);

  @Query("SELECT pv FROM PatientVersionEntity pv " + "JOIN pv.patient p " + "WHERE p.id IN :ids")
  List<PatientVersionEntity> findAllByIdIn(@Param("ids") List<String> ids);
}
