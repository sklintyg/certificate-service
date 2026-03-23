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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.elements;

import java.time.Period;
import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationDate;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionBeraknatFodelsedatum {

  public static final ElementId QUESTION_BERAKNAT_FODELSEDATUM_ID = new ElementId("54");
  public static final FieldId QUESTION_BERAKNAT_FODELSEDATUM_FIELD_ID = new FieldId("54.1");
  private static final PdfFieldId PDF_FODELSEDATUM_FIELD_ID =
      new PdfFieldId("form1[0].#subform[0].flt_dat[0]");

  private QuestionBeraknatFodelsedatum() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionBeraknatFodelsedatum() {
    return ElementSpecification.builder()
        .id(QUESTION_BERAKNAT_FODELSEDATUM_ID)
        .configuration(
            ElementConfigurationDate.builder()
                .name("Datum")
                .id(QUESTION_BERAKNAT_FODELSEDATUM_FIELD_ID)
                .min(Period.ofDays(0))
                .max(Period.ofYears(1))
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatory(
                    QUESTION_BERAKNAT_FODELSEDATUM_ID, QUESTION_BERAKNAT_FODELSEDATUM_FIELD_ID)))
        .validations(
            List.of(
                ElementValidationDate.builder()
                    .mandatory(true)
                    .min(Period.ofDays(0))
                    .max(Period.ofYears(1))
                    .build()))
        .pdfConfiguration(
            PdfConfigurationDate.builder().pdfFieldId(PDF_FODELSEDATUM_FIELD_ID).build())
        .build();
  }
}
