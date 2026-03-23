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
package se.inera.intyg.certificateservice.certificate.converter;

import static se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationUnitContactInformation.UNIT_CONTACT_INFORMATION;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueList;

@Component
public class PrintCertificateUnitInformationConverter {

  public List<String> convert(Certificate certificate) {
    final var elementData = certificate.getElementDataById(UNIT_CONTACT_INFORMATION);

    if (elementData.isEmpty()) {
      return Collections.emptyList();
    }

    final var simplifiedValue =
        certificate
            .certificateModel()
            .elementSpecification(UNIT_CONTACT_INFORMATION)
            .configuration()
            .simplified(elementData.get().value());

    if (simplifiedValue.isEmpty()) {
      return Collections.emptyList();
    }

    if (!(simplifiedValue.get() instanceof ElementSimplifiedValueList elementSimplifiedValueList)) {
      throw new IllegalStateException(
          "Wrong format of simplified value for unit info expecting List");
    }

    return elementSimplifiedValueList.list();
  }
}
