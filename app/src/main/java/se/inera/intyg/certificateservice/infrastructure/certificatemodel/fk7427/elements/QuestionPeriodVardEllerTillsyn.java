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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7427.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7427.elements.QuestionVardEllerTillsyn.QUESTION_VARD_ELLER_TILLSYN_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDateRange;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDateRange;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationDateRange;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionPeriodVardEllerTillsyn {

  public static final ElementId QUESTION_PERIOD_VARD_ELLER_TILLSYN_ID = new ElementId("62.6");
  private static final FieldId QUESTION_PERIOD_VARD_ELER_TILLSYN_FIELD_ID = new FieldId("62.6");

  private static final PdfFieldId PDF_FIELD_ID_FROM =
      new PdfFieldId("form1[0].#subform[2].flt_datumFranMed[0]");
  private static final PdfFieldId PDF_FIELD_ID_TO =
      new PdfFieldId("form1[0].#subform[2].flt_datumTillMed[0]");

  private QuestionPeriodVardEllerTillsyn() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionPeriodVardEllerTillsyn() {
    return ElementSpecification.builder()
        .id(QUESTION_PERIOD_VARD_ELLER_TILLSYN_ID)
        .includeWhenRenewing(false)
        .configuration(
            ElementConfigurationDateRange.builder()
                .name("Under vilken period behöver barnet vård eller tillsyn?")
                .labelFrom("Fr.o.m")
                .labelTo("T.o.m")
                .id(QUESTION_PERIOD_VARD_ELER_TILLSYN_FIELD_ID)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatory(
                    QUESTION_PERIOD_VARD_ELLER_TILLSYN_ID,
                    QUESTION_PERIOD_VARD_ELER_TILLSYN_FIELD_ID)))
        .validations(List.of(ElementValidationDateRange.builder().mandatory(true).build()))
        .pdfConfiguration(
            PdfConfigurationDateRange.builder().from(PDF_FIELD_ID_FROM).to(PDF_FIELD_ID_TO).build())
        .mapping(new ElementMapping(QUESTION_VARD_ELLER_TILLSYN_ID, null))
        .build();
  }
}
