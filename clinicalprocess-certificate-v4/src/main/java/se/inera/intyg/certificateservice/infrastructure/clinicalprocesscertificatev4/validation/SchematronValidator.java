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

import com.helger.schematron.sch.SchematronResourceSCH;
import com.helger.schematron.svrl.SVRLHelper;
import com.helger.schematron.svrl.jaxb.SchematronOutputType;
import java.io.StringReader;
import javax.xml.transform.stream.StreamSource;
import lombok.extern.slf4j.Slf4j;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.Xml;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.SchematronPath;

@Slf4j
public class SchematronValidator implements XmlSchematronValidator {

  @Override
  public boolean validate(CertificateId certificateId, Xml xml, SchematronPath schematronPath) {
    final var xmlStream = new StreamSource(new StringReader(xml.xml()));

    if (schematronPath != null) {
      final var schematronResource = SchematronResourceSCH.fromClassPath(schematronPath.value());
      try {
        final var schematronOutput = schematronResource.applySchematronValidationToSVRL(xmlStream);
        return schematronResult(schematronOutput, certificateId.id());
      } catch (Exception e) {
        log.error(
            "Schematron validation of certificate id {} failed with an exception.",
            certificateId,
            e);
        return false;
      }
    }

    return true;
  }

  private boolean schematronResult(SchematronOutputType output, String certificateId) {
    final var failedAssertions = SVRLHelper.getAllFailedAssertions(output);

    if (failedAssertions.isEmpty()) {
      log.info("Schematron validation passed for certificate id {}.", certificateId);
      return true;
    } else {
      final var failures =
          failedAssertions.stream()
              .map(fail -> "failed: %s, MSG: %s".formatted(fail.getTest(), fail.getText()))
              .toList();
      log.warn("Schematron validation failed for certificate id {}.\n{}", certificateId, failures);
      return false;
    }
  }
}
