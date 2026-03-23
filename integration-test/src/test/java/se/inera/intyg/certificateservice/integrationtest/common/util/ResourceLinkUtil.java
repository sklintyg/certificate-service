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
package se.inera.intyg.certificateservice.integrationtest.common.util;

import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.CertificateTypeInfoDTO;
import se.inera.intyg.certificateservice.application.common.dto.ResourceLinkDTO;
import se.inera.intyg.certificateservice.application.common.dto.ResourceLinkTypeDTO;

public class ResourceLinkUtil {

  public static ResourceLinkDTO resourceLink(
      CertificateTypeInfoDTO certificateTypeInfoDTOS, ResourceLinkTypeDTO type) {
    if (certificateTypeInfoDTOS == null) {
      return null;
    }

    return certificateTypeInfoDTOS.getLinks().stream()
        .filter(resourceLinkDTO -> type.equals(resourceLinkDTO.getType()))
        .limit(1)
        .findAny()
        .orElse(null);
  }
}
