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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import static se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType.CHECKBOX_MULTIPLE_CODE;

import java.util.ArrayList;
import java.util.Map;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDataElement;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueCode;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueCodeList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementVisibilityConfiguration;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementVisibilityConfigurationsCheckboxMultipleCode;

@Component
public class ComplementElementVisibilityCheckboxMultipleCode
    implements ComplementElementVisibility {

  @Override
  public boolean supports(ElementVisibilityConfiguration elementVisibilityConfiguration) {
    return elementVisibilityConfiguration.type().equals(CHECKBOX_MULTIPLE_CODE);
  }

  @Override
  public void handle(
      Map<String, CertificateDataElement> dataElementMap,
      ElementVisibilityConfiguration visibilityConfiguration) {
    if (!(visibilityConfiguration
        instanceof
        ElementVisibilityConfigurationsCheckboxMultipleCode visibilityConfigurationsCodeList)) {
      throw new IllegalStateException(
          "visibilityConfiguration is not of type ElementVisibilityConfigurationsCodeList");
    }

    final var certificateDataElement =
        dataElementMap.get(visibilityConfigurationsCodeList.elementId().id());
    final var value = (CertificateDataValueCodeList) certificateDataElement.getValue();
    final var certificateDataValueCodes = new ArrayList<>(value.getList());
    final var certificateDataValueCode = certificateDataValueCode(visibilityConfigurationsCodeList);

    if (certificateDataValueCodes.contains(certificateDataValueCode)) {
      return;
    }

    certificateDataValueCodes.add(certificateDataValueCode);

    final var certificateDataValueCodeList = value.withList(certificateDataValueCodes);
    final var certificateDataElementWithUpdatedValue =
        certificateDataElement.withValue(certificateDataValueCodeList);

    dataElementMap.put(
        visibilityConfigurationsCodeList.elementId().id(), certificateDataElementWithUpdatedValue);
  }

  private static CertificateDataValueCode certificateDataValueCode(
      ElementVisibilityConfigurationsCheckboxMultipleCode visibilityConfigurationsCodeList) {
    return CertificateDataValueCode.builder()
        .id(visibilityConfigurationsCodeList.fieldId().value())
        .code(visibilityConfigurationsCodeList.fieldId().value())
        .build();
  }
}
