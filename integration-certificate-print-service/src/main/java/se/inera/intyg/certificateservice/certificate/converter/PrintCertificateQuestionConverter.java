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
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.certificate.dto.ElementSimplifiedValueDTO;
import se.inera.intyg.certificateservice.certificate.dto.ElementSimplifiedValueLabeledListDTO;
import se.inera.intyg.certificateservice.certificate.dto.ElementSimplifiedValueLabeledTextDTO;
import se.inera.intyg.certificateservice.certificate.dto.ElementSimplifiedValueListDTO;
import se.inera.intyg.certificateservice.certificate.dto.ElementSimplifiedValueTableDTO;
import se.inera.intyg.certificateservice.certificate.dto.ElementSimplifiedValueTextDTO;
import se.inera.intyg.certificateservice.certificate.dto.PrintCertificateQuestionDTO;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValue;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueLabeledList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueLabeledText;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueList;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueTable;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementSimplifiedValueText;
import se.inera.intyg.certificateservice.domain.certificate.service.PdfGeneratorOptions;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;

@Component
public class PrintCertificateQuestionConverter {

  public Optional<PrintCertificateQuestionDTO> convert(
      ElementSpecification elementSpecification,
      Certificate certificate,
      PdfGeneratorOptions pdfGeneratorOptions) {
    final var elementData = certificate.getElementDataById(elementSpecification.id());
    final var allElementData = certificate.elementData();

    if (elementShouldNotBeDisplayed(elementSpecification, allElementData)) {
      return Optional.empty();
    }

    return elementSpecification
        .simplifiedValue(elementData, allElementData, pdfGeneratorOptions)
        .map(
            elementSimplifiedValue ->
                PrintCertificateQuestionDTO.builder()
                    .id(elementSpecification.id().id())
                    .name(elementSpecification.configuration().name())
                    .value(convertValue(elementSimplifiedValue))
                    .subquestions(
                        elementSpecification.children().stream()
                            .map(child -> convert(child, certificate, pdfGeneratorOptions))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .toList())
                    .build());
  }

  private static boolean elementShouldNotBeDisplayed(
      ElementSpecification elementSpecification, List<ElementData> allElementData) {
    return elementSpecification.shouldValidate() != null
        && elementSpecification.shouldValidate().negate().test(allElementData);
  }

  private ElementSimplifiedValueDTO convertValue(ElementSimplifiedValue elementSimplifiedValue) {
    if (elementSimplifiedValue instanceof ElementSimplifiedValueText valueText) {
      return ElementSimplifiedValueTextDTO.builder().text(valueText.text()).build();
    }

    if (elementSimplifiedValue instanceof ElementSimplifiedValueList valueList) {
      return ElementSimplifiedValueListDTO.builder().list(valueList.list()).build();
    }

    if (elementSimplifiedValue instanceof ElementSimplifiedValueTable valueTable) {
      return ElementSimplifiedValueTableDTO.builder()
          .headings(valueTable.headings())
          .values(valueTable.values())
          .build();
    }

    if (elementSimplifiedValue instanceof ElementSimplifiedValueLabeledList valueLabeledList) {
      return ElementSimplifiedValueLabeledListDTO.builder()
          .list(
              valueLabeledList.list().stream()
                  .map(
                      labeledText ->
                          ElementSimplifiedValueLabeledTextDTO.builder()
                              .label(labeledText.label())
                              .text(labeledText.text())
                              .build())
                  .toList())
          .build();
    }

    if (elementSimplifiedValue instanceof ElementSimplifiedValueLabeledText valueText) {
      return ElementSimplifiedValueLabeledTextDTO.builder()
          .label(valueText.label())
          .text(valueText.text())
          .build();
    }

    throw new IllegalStateException("No converter for this simplified value type");
  }
}
