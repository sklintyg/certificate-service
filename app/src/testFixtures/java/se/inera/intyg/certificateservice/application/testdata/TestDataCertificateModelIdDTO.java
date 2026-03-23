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
package se.inera.intyg.certificateservice.application.testdata;

import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.CertificateModelIdDTO;
import se.inera.intyg.certificateservice.domain.testdata.TestDataCertificateModelConstants;

public class TestDataCertificateModelIdDTO {

  private TestDataCertificateModelIdDTO() {
    throw new IllegalStateException("Utility class");
  }

  public static final CertificateModelIdDTO FK7804_CERTIFICATE_MODEL_ID_DTO =
      fk7804CertificateModelIdDTO().build();

  public static CertificateModelIdDTO.CertificateModelIdDTOBuilder fk7804CertificateModelIdDTO() {
    return CertificateModelIdDTO.builder()
        .type(TestDataCertificateModelConstants.FK7804_TYPE.type())
        .version(TestDataCertificateModelConstants.FK7804_VERSION.version());
  }
}
