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
package se.inera.intyg.certificateservice.certificate.custom;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.custom.converter.CustomPrintRequestConverter;
import se.inera.intyg.certificateservice.certificate.custom.integration.PrintCustomCertificateFromCertificatePrintService;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Pdf;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGenerator;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CustomPdfSpecification;

@Component("customPdfGenerator")
@RequiredArgsConstructor
public class CustomPdfGenerator implements PdfGenerator {

  private final CustomPrintRequestConverter customPrintRequestConverter;
  private final PrintCustomCertificateFromCertificatePrintService
      printCustomCertificateFromCertificatePrintService;

  @Override
  public Pdf generate(Certificate certificate, PdfGeneratorOptions options) {
    if (!(certificate.certificateModel().pdfSpecification()
        instanceof CustomPdfSpecification spec)) {
      throw new IllegalArgumentException(
          "CustomPdfGenerator can only process CustomPdfSpecification");
    }

    final var fileName = buildFileName(certificate);
    final var includeAddress =
        CustomPrintRequestConverter.includeAddress(certificate, options.citizenFormat());
    final var templatePath =
        includeAddress ? spec.pdfTemplatePath() : spec.pdfNoAddressTemplatePath();
    final var templateBytes = loadTemplate(templatePath);

    final var request =
        customPrintRequestConverter.convert(certificate, options, spec, templateBytes, fileName);
    final var response = printCustomCertificateFromCertificatePrintService.print(request);

    final var pdfBytes = Base64.getDecoder().decode(response.getPdfData());
    return new Pdf(pdfBytes, fileName);
  }

  byte[] loadTemplate(String templatePath) {
    try (final var in = getClass().getClassLoader().getResourceAsStream(templatePath)) {
      if (in == null) {
        throw new IllegalStateException("PDF template not found at path: " + templatePath);
      }
      return in.readAllBytes();
    } catch (IOException e) {
      throw new IllegalStateException("Could not load PDF template from path: " + templatePath, e);
    }
  }

  private static String buildFileName(Certificate certificate) {
    final var name = certificate.certificateModel().name();
    final var timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd_HHmm"));
    return String.format("%s_%s", name, timestamp)
        .replace("å", "a")
        .replace("ä", "a")
        .replace("ö", "o")
        .replace(" ", "_")
        .replace("–", "")
        .replace("__", "_")
        .toLowerCase();
  }
}
