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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.CertificateModelFactoryFK7426.TEXT_FIELD_LIMIT;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.FK7426PdfSpecification.FORTSATTNINGSBLAD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7426.FK7426PdfSpecification.ROW_MAX_LENGTH;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionSymtom {

  public static final ElementId QUESTION_SYMTOM_ID = new ElementId("55");
  public static final FieldId QUESTION_SYMTOM_FIELD_ID = new FieldId("55.1");
  private static final PdfFieldId PDF_FIELD_ID =
      new PdfFieldId("form1[0].#subform[0].flt_txtFlerradig[0]");

  private QuestionSymtom() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionSymtom() {
    return ElementSpecification.builder()
        .id(QUESTION_SYMTOM_ID)
        .configuration(
            ElementConfigurationTextArea.builder()
                .name("Fyll i vilka symtom barnet har om diagnos inte är fastställd")
                .id(QUESTION_SYMTOM_FIELD_ID)
                .build())
        .rules(List.of(CertificateElementRuleFactory.limit(QUESTION_SYMTOM_ID, TEXT_FIELD_LIMIT)))
        .validations(List.of(ElementValidationText.builder().limit((int) TEXT_FIELD_LIMIT).build()))
        .pdfConfiguration(
            PdfConfigurationText.builder()
                .pdfFieldId(PDF_FIELD_ID)
                .maxLength(ROW_MAX_LENGTH * 5)
                .overflowSheetFieldId(FORTSATTNINGSBLAD_ID)
                .build())
        .build();
  }
}
