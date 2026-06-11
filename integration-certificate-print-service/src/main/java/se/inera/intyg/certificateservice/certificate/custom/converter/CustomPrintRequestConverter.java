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
package se.inera.intyg.certificateservice.certificate.custom.converter;

import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.custom.dto.CustomPrintRequestDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;

@Component
@RequiredArgsConstructor
public class CustomPrintRequestConverter {

  private final CustomPdfMetadataConverter customPdfMetadataConverter;
  private final CustomPdfFieldsConverter customPdfFieldsConverter;

  public CustomPrintRequestDTO convert(
      Certificate certificate,
      PdfGeneratorOptions options,
      CustomPdfSpecification spec,
      byte[] templateBytes,
      String fileName) {
    final var includeAddress = includeAddress(certificate, options.citizenFormat());

    return new CustomPrintRequestDTO(
        Base64.getEncoder().encodeToString(templateBytes),
        customPdfMetadataConverter.convert(certificate, options, spec, includeAddress, fileName),
        customPdfFieldsConverter.convert(certificate, spec));
  }

  public static boolean includeAddress(Certificate certificate, boolean citizenFormat) {
    if (citizenFormat) {
      return false;
    }
    return certificate.sent() == null || certificate.sent().sentAt() == null;
  }
}
