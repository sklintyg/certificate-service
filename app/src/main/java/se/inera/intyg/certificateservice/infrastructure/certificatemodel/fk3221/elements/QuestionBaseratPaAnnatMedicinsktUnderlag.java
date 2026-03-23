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

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationRadioBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfRadioOption;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationBoolean;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionBaseratPaAnnatMedicinsktUnderlag {

  public static final ElementId QUESTION_BASERAT_PA_ANNAT_UNDERLAG_ID = new ElementId("3");
  public static final FieldId QUESTION_BASERAT_PA_ANNAT_UNDERLAG_FIELD_ID = new FieldId("3.1");
  private static final PdfFieldId PDF_BASERAT_PA_ANNAT_UNDERLAG_FIELD_ID =
      new PdfFieldId("form1[0].#subform[0].RadioButtonList2[0]");
  private static final PdfRadioOption PDF_BASERAT_PA_ANNAT_UNDERLAG_OPTION_TRUE =
      new PdfRadioOption("2");
  private static final PdfRadioOption PDF_BASERAT_PA_ANNAT_UNDERLAG_OPTION_FALSE =
      new PdfRadioOption("1");

  private QuestionBaseratPaAnnatMedicinsktUnderlag() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionBaseratPaAnnatMedicinsktUnderlag() {
    return ElementSpecification.builder()
        .id(QUESTION_BASERAT_PA_ANNAT_UNDERLAG_ID)
        .configuration(
            ElementConfigurationRadioBoolean.builder()
                .id(QUESTION_BASERAT_PA_ANNAT_UNDERLAG_FIELD_ID)
                .selectedText("Ja")
                .unselectedText("Nej")
                .name("Är utlåtandet även baserat på andra medicinska utredningar eller underlag?")
                .build())
        .validations(List.of(ElementValidationBoolean.builder().mandatory(true).build()))
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatoryExist(
                    QUESTION_BASERAT_PA_ANNAT_UNDERLAG_ID,
                    QUESTION_BASERAT_PA_ANNAT_UNDERLAG_FIELD_ID)))
        .pdfConfiguration(
            PdfConfigurationRadioBoolean.builder()
                .pdfFieldId(PDF_BASERAT_PA_ANNAT_UNDERLAG_FIELD_ID)
                .optionTrue(PDF_BASERAT_PA_ANNAT_UNDERLAG_OPTION_TRUE)
                .optionFalse(PDF_BASERAT_PA_ANNAT_UNDERLAG_OPTION_FALSE)
                .build())
        .build();
  }
}
