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

import static se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfConstants.CHECKED_BOX_VALUE;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.pdfboxgenerator.pdf.PdfField;

@Component
public class PdfBooleanValueGenerator implements PdfElementValue<ElementValueBoolean> {

  @Override
  public Class<ElementValueBoolean> getType() {
    return ElementValueBoolean.class;
  }

  @Override
  public List<PdfField> generate(
      ElementSpecification elementSpecification, ElementValueBoolean elementValue) {
    if (elementSpecification.pdfConfiguration()
        instanceof PdfConfigurationRadioBoolean radioConfig) {
      return getFields(elementValue, radioConfig);
    }

    final var pdfConfiguration = (PdfConfigurationBoolean) elementSpecification.pdfConfiguration();
    return getFields(elementValue, pdfConfiguration);
  }

  private List<PdfField> getFields(
      ElementValueBoolean valueBoolean, PdfConfigurationBoolean configuration) {
    if (valueBoolean.value() == null) {
      return Collections.emptyList();
    }

    if (configuration.checkboxFalse() == null && !valueBoolean.value()) {
      return List.of();
    }

    final var pdfFieldId = getCheckboxId(configuration, valueBoolean);

    return List.of(PdfField.builder().id(pdfFieldId.id()).value(CHECKED_BOX_VALUE).build());
  }

  private List<PdfField> getFields(
      ElementValueBoolean valueBoolean, PdfConfigurationRadioBoolean configuration) {
    if (valueBoolean.value() == null) {
      return Collections.emptyList();
    }

    return List.of(
        PdfField.builder()
            .id(configuration.pdfFieldId().id())
            .value(
                valueBoolean.value()
                    ? configuration.optionTrue().value()
                    : configuration.optionFalse().value())
            .build());
  }

  private static PdfFieldId getCheckboxId(
      PdfConfigurationBoolean configForQuestion, ElementValueBoolean valueBoolean) {
    if (Boolean.FALSE.equals(valueBoolean.value())) {
      return configForQuestion.checkboxFalse();
    }
    return configForQuestion.checkboxTrue();
  }
}
