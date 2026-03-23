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
import org.springframework.data.jpa.domain.Specification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateType;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateEntity;

public class CertificateModelEntitySpecification {

  private CertificateModelEntitySpecification() {}

  public static Specification<CertificateEntity> containsTypes(List<CertificateType> types) {
    final var typeNames = types.stream().map(CertificateType::type).toList();

    return (root, query, criteriaBuilder) -> {
      final var type = root.join("certificateModel");
      return criteriaBuilder.upper(type.get("type")).in(typeNames);
    };
  }
}
