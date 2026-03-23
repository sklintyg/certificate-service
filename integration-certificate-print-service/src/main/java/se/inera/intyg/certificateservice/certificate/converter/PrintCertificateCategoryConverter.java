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

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.dto.PrintCertificateCategoryDTO;
import se.inera.intyg.certificateservice.certificate.dto.PrintCertificateQuestionDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCategory;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;

@Component
@RequiredArgsConstructor
public class PrintCertificateCategoryConverter {

  private final PrintCertificateQuestionConverter printCertificateQuestionConverter;

  public PrintCertificateCategoryDTO convert(
      Certificate certificate, ElementSpecification category, PdfGeneratorOptions pdfOptions) {
    if (!(category.configuration() instanceof ElementConfigurationCategory)) {
      throw new IllegalStateException(
          "Only category can be at top level of element specifications");
    }

    return PrintCertificateCategoryDTO.builder()
        .name(category.configuration().name())
        .id(category.id().id())
        .questions(convertChildren(certificate, category, pdfOptions))
        .build();
  }

  private List<PrintCertificateQuestionDTO> convertChildren(
      Certificate certificate, ElementSpecification category, PdfGeneratorOptions pdfOptions) {
    return category.children().stream()
        .map(
            elementSpecification ->
                printCertificateQuestionConverter.convert(
                    elementSpecification, certificate, pdfOptions))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }
}
