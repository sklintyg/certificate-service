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
package se.inera.intyg.certificateservice.certificate.custom.provider;

import static se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationUnitContactInformation.UNIT_CONTACT_INFORMATION;

import java.util.Map;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPdfFieldDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;

@Component
public class UnitContactInformationPdfFieldsProvider implements PdfFieldsProvider {

  @Override
  public Map<String, CustomPdfFieldDTO> fields(
      Certificate certificate, CustomPdfSpecification spec) {
    final var unitName = certificate.getMetadataForPrint().issuingUnit().name().name();
    final var contactInfo =
        certificate
            .getElementDataById(UNIT_CONTACT_INFORMATION)
            .filter(data -> data.value() instanceof ElementValueUnitContactInformation)
            .map(data -> buildAddress(unitName, (ElementValueUnitContactInformation) data.value()))
            .orElse("");
    return Map.of(spec.signature().contactInformation().id(), new CustomPdfFieldDTO(contactInfo));
  }

  private static String buildAddress(
      String unitName, ElementValueUnitContactInformation unitValue) {
    return String.join(
        "\n",
        unitName,
        String.join(
            "", unitValue.address(), ", ", String.join(" ", unitValue.zipCode(), unitValue.city())),
        String.join(" ", "Telefon:", unitValue.phoneNumber()));
  }
}
