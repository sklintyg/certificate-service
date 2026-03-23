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
package se.inera.intyg.certificateservice.certificate;

import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.converter.PrintCertificateCategoryConverter;
import se.inera.intyg.certificateservice.certificate.converter.PrintCertificateMetadataConverter;
import se.inera.intyg.certificateservice.certificate.dto.PrintCertificateCategoryDTO;
import se.inera.intyg.certificateservice.certificate.dto.PrintCertificateRequestDTO;
import se.inera.intyg.certificateservice.certificate.integration.PrintCertificateFromCertificatePrintService;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Pdf;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGenerator;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationUnitContactInformation;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;

@Component("generalPdfGenerator")
@AllArgsConstructor
public class GeneralPdfGenerator implements PdfGenerator {

  private final PrintCertificateMetadataConverter printCertificateMetadataConverter;
  private final PrintCertificateCategoryConverter printCertificateCategoryConverter;
  private final PrintCertificateFromCertificatePrintService
      printCertificateFromCertificatePrintService;

  @Override
  public Pdf generate(Certificate certificate, PdfGeneratorOptions options) {
    final var fileName = getFileName(certificate);

    final var request =
        PrintCertificateRequestDTO.builder()
            .metadata(
                printCertificateMetadataConverter.convert(
                    certificate, options.citizenFormat(), fileName))
            .categories(
                certificate.certificateModel().elementSpecifications().stream()
                    .filter(isNotContactInformation())
                    .map(
                        element ->
                            printCertificateCategoryConverter.convert(
                                certificate, element, options))
                    .filter(hasQuestions())
                    .toList())
            .build();

    final var response = printCertificateFromCertificatePrintService.print(request);

    return new Pdf(response.getPdfData(), fileName);
  }

  private String getFileName(Certificate certificate) {
    final var name =
        certificate.certificateModel().recipient().generalName() == null
            ? certificate.certificateModel().name()
            : certificate.certificateModel().recipient().generalName();

    return name.replace("å", "a")
        .replace("ä", "a")
        .replace("ö", "o")
        .replace(" ", "_")
        .replace("–", "")
        .replace("__", "_")
        .toLowerCase();
  }

  private Predicate<ElementSpecification> isNotContactInformation() {
    return elementSpecification ->
        !(elementSpecification.configuration()
            instanceof ElementConfigurationUnitContactInformation);
  }

  private Predicate<PrintCertificateCategoryDTO> hasQuestions() {
    return elementSpecification -> !elementSpecification.getQuestions().isEmpty();
  }
}
