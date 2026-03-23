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

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateEntity;

public class CertificateEntitySpecification {

  private CertificateEntitySpecification() {}

  public static Specification<CertificateEntity> modifiedEqualsAndGreaterThan(LocalDateTime from) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get("modified"), from);
  }

  public static Specification<CertificateEntity> modifiedEqualsAndLesserThan(LocalDateTime to) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.lessThanOrEqualTo(root.get("modified"), to);
  }

  public static Specification<CertificateEntity> createdEqualsAndGreaterThan(LocalDateTime from) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get("created"), from);
  }

  public static Specification<CertificateEntity> createdEqualsAndLesserThan(LocalDateTime to) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.lessThanOrEqualTo(root.get("created"), to);
  }

  public static Specification<CertificateEntity> notPlacerholderCertificate() {
    return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("placeholder"));
  }

  public static Specification<CertificateEntity> signedEqualsAndGreaterThan(LocalDate from) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.greaterThanOrEqualTo(root.get("signed"), from.atStartOfDay());
  }

  public static Specification<CertificateEntity> signedEqualsAndLesserThan(LocalDate to) {
    return (root, query, criteriaBuilder) ->
        criteriaBuilder.lessThanOrEqualTo(root.get("signed"), to.atTime(23, 59, 59));
  }
}
