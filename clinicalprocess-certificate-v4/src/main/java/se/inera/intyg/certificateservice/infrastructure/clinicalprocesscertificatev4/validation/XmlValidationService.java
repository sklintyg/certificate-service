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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.validation;

import lombok.RequiredArgsConstructor;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Xml;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.SchematronPath;

@RequiredArgsConstructor
public class XmlValidationService {

  private final XmlSchematronValidator xmlSchematronValidator;
  private final XmlSchemaValidator xmlSchemaValidator;

  public void validate(Xml xml, SchematronPath schematronPath, CertificateId certificateId) {
    validateParameters(xml, certificateId);
    final var schemaValidation = xmlSchemaValidator.validate(certificateId, xml);

    final var schematronValidation =
        schematronPath == null
            || xmlSchematronValidator.validate(certificateId, xml, schematronPath);

    if (!schematronValidation || !schemaValidation) {
      throw new IllegalStateException(
          ("Certificate did not pass schematron validation."
                  + " Schematron validation result: '%s'. "
                  + "Schema validation result: '%s'")
              .formatted(schematronValidation, schemaValidation));
    }
  }

  private void validateParameters(Xml xml, CertificateId certificateId) {
    if (xml == null || xml.xml() == null || xml.xml().isBlank()) {
      throw new IllegalArgumentException("Missing required parameter xml: '%s'".formatted(xml));
    }
    if (certificateId == null || certificateId.id().isBlank()) {
      throw new IllegalArgumentException(
          "Missing required parameter certificateId: '%s'".formatted(certificateId));
    }
  }
}
