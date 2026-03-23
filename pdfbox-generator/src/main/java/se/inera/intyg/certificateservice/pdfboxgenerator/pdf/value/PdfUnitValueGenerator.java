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
package se.inera.intyg.certificateservice.pdfboxgenerator.pdf.value;

import static se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationUnitContactInformation.UNIT_CONTACT_INFORMATION;

import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.TemplatePdfSpecification;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

@Component
public class PdfUnitValueGenerator {

  public List<PdfField> generate(Certificate certificate) {
    if (!(certificate.certificateModel().pdfSpecification()
        instanceof TemplatePdfSpecification templateSpec)) {
      throw new IllegalArgumentException(
          "PdfUnitValueGenerator can only process TemplatePdfSpecification");
    }

    final var elementData = certificate.getElementDataById(UNIT_CONTACT_INFORMATION);

    final var contactInfo =
        elementData.map(data -> buildAddress(certificate, data.value())).orElse("");

    return List.of(
        PdfField.builder()
            .id(templateSpec.signature().contactInformation().id())
            .value(contactInfo)
            .unitField(true)
            .build());
  }

  private static String buildAddress(Certificate certificate, ElementValue elementValue) {
    if (!(elementValue instanceof ElementValueUnitContactInformation unitValue)) {
      throw new IllegalStateException(
          String.format("Wrong value type: '%s'", elementValue.getClass()));
    }

    return String.join(
        "\n",
        certificate.getMetadataForPrint().issuingUnit().name().name(),
        String.join(
            "", unitValue.address(), ", ", String.join(" ", unitValue.zipCode(), unitValue.city())),
        String.join(" ", "Telefon:", unitValue.phoneNumber()));
  }
}
