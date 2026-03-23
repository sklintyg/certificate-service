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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3221.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3221.FK3221PdfSpecification.PDF_TEXT_FIELD_LENGTH;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3221.elements.QuestionFunktionsnedsattning.FUNKTIONSNEDSATTNING_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCodeList;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementVisibilityConfigurationsCheckboxMultipleCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public abstract class AbstractFunktionsnedsattningMotivering {

  protected static final String GENERAL_LABEL_FUNKTIONSNEDSATTNING =
      "Beskriv funktionsnedsättningen, om möjligt med grad. Ange även undersökningsfynd.";

  AbstractFunktionsnedsattningMotivering() {
    throw new IllegalStateException("Utility class");
  }

  protected static ElementSpecification getFunktionsnedsattningMotivering(
      ElementId questionId,
      FieldId questionFieldId,
      FieldId parentFieldId,
      String name,
      String label,
      String description,
      PdfFieldId pdfFieldId) {
    return ElementSpecification.builder()
        .id(questionId)
        .visibilityConfiguration(
            ElementVisibilityConfigurationsCheckboxMultipleCode.builder()
                .elementId(FUNKTIONSNEDSATTNING_ID)
                .fieldId(parentFieldId)
                .build())
        .configuration(
            ElementConfigurationTextArea.builder()
                .id(questionFieldId)
                .name(name)
                .label(label)
                .description(description)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.limit(questionId, (short) 4000),
                CertificateElementRuleFactory.mandatory(questionId, questionFieldId),
                CertificateElementRuleFactory.show(FUNKTIONSNEDSATTNING_ID, parentFieldId)))
        .validations(List.of(ElementValidationText.builder().mandatory(true).limit(4000).build()))
        .shouldValidate(
            elementData ->
                elementData.stream()
                    .filter(data -> data.id().equals(FUNKTIONSNEDSATTNING_ID))
                    .map(element -> (ElementValueCodeList) element.value())
                    .anyMatch(
                        value ->
                            value.list().stream()
                                .anyMatch(codeValue -> codeValue.codeId().equals(parentFieldId))))
        .pdfConfiguration(
            PdfConfigurationText.builder()
                .pdfFieldId(pdfFieldId)
                .maxLength(PDF_TEXT_FIELD_LENGTH * 4)
                .overflowSheetFieldId(
                    new PdfFieldId(("form1[0].#subform[4].flt_txtFortsattningsblad[0]")))
                .build())
        .build();
  }
}
