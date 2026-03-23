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

import jakarta.persistence.criteria.Join;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.MessageEntity;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.UnitEntity;

public class UnitEntitySpecification {

  private static final String HSA_ID = "hsaId";
  private static final String ISSUED_ON_UNIT = "issuedOnUnit";

  private UnitEntitySpecification() {}

  public static Specification<MessageEntity> inIssuedOnUnitIds(List<HsaId> unitIds) {
    return (root, query, criteriaBuilder) -> {
      Join<CertificateEntity, MessageEntity> certificate = root.join("certificate");
      Join<UnitEntity, CertificateEntity> certificateIssuedOn = certificate.join(ISSUED_ON_UNIT);
      return certificateIssuedOn.get(HSA_ID).in(unitIds.stream().map(HsaId::id).toList());
    };
  }

  public static Specification<CertificateEntity> issuedOnUnitIdIn(List<HsaId> unitIds) {
    return (root, query, criteriaBuilder) -> {
      Join<UnitEntity, CertificateEntity> certificateIssuedOn = root.join(ISSUED_ON_UNIT);
      return certificateIssuedOn.get(HSA_ID).in(unitIds.stream().map(HsaId::id).toList());
    };
  }

  public static Specification<CertificateEntity> equalsCareUnit(HsaId careUnit) {
    return (root, query, criteriaBuilder) -> {
      Join<UnitEntity, CertificateEntity> certificateCareUnit = root.join("careUnit");
      return criteriaBuilder.equal(certificateCareUnit.get(HSA_ID), careUnit.id());
    };
  }
}
