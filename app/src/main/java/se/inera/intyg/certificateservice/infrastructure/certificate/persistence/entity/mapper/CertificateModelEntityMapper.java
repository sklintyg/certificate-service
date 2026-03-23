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

import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModelId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateVersion;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateModelEntity;

public class CertificateModelEntityMapper {

  private CertificateModelEntityMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static CertificateModelEntity toEntity(CertificateModel model) {
    return CertificateModelEntity.builder()
        .type(model.id().type().type())
        .name(model.name())
        .version(model.id().version().version())
        .build();
  }

  public static CertificateModel toDomain(CertificateModelEntity model) {
    return CertificateModel.builder()
        .id(
            CertificateModelId.builder()
                .version(new CertificateVersion(model.getVersion()))
                .type(new CertificateType(model.getType()))
                .build())
        .name(model.getName())
        .build();
  }
}
