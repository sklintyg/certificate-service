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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.elements.QuestionPatientensBehandlingOchVardsituation.QUESTION_PATIENTENS_BEHANDLING_OCH_VARDSITUATION_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.elements.QuestionUppskattaHurLangeTillstandetKommerVaraLivshotande.QUESTION_UPSKATTA_HUR_LANGE_TILLSTANDET_KOMMER_VARA_LIVSHOTANDE_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.elements.QuestionUppskattaHurLangeTillstandetKommerVaraLivshotande.QUESTION_UPSKATTA_HUR_LANGE_TILLSTANDET_KOMMER_VARA_LIVSHOTANDE_ID;

import java.time.Period;
import java.util.List;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationDate;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionTillstandetUppskattasLivshotandeTillOchMed {

  public static final ElementId QUESTION_TILLSTANDET_UPPSKATTAS_LIVSHOTANDE_TILL_OCH_MED_ID =
      new ElementId("52.6");
  private static final FieldId QUESTION_TILLSTANDET_UPPSKATTAS_LIVSHOTANDE_TILL_OCH_MED_FIELD_ID =
      new FieldId("52.6");
  private static final PdfFieldId PDF_CONDITION_IS_LIFE_THREATENING_TO_FIELD_ID =
      new PdfFieldId("form1[0].#subform[1].flt_datumTillMed[0]");

  private QuestionTillstandetUppskattasLivshotandeTillOchMed() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionTillstandetUppskattasLivshotandeTillOchMed() {
    return ElementSpecification.builder()
        .id(QUESTION_TILLSTANDET_UPPSKATTAS_LIVSHOTANDE_TILL_OCH_MED_ID)
        .configuration(
            ElementConfigurationDate.builder()
                .id(QUESTION_TILLSTANDET_UPPSKATTAS_LIVSHOTANDE_TILL_OCH_MED_FIELD_ID)
                .name("Till och med vilket datum")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatory(
                    QUESTION_TILLSTANDET_UPPSKATTAS_LIVSHOTANDE_TILL_OCH_MED_ID,
                    QUESTION_TILLSTANDET_UPPSKATTAS_LIVSHOTANDE_TILL_OCH_MED_FIELD_ID),
                CertificateElementRuleFactory.show(
                    QUESTION_UPSKATTA_HUR_LANGE_TILLSTANDET_KOMMER_VARA_LIVSHOTANDE_ID,
                    QUESTION_UPSKATTA_HUR_LANGE_TILLSTANDET_KOMMER_VARA_LIVSHOTANDE_FIELD_ID)))
        .validations(
            List.of(ElementValidationDate.builder().mandatory(true).min(Period.ofDays(0)).build()))
        .shouldValidate(
            elementData ->
                elementData.stream()
                    .filter(
                        data ->
                            data.id()
                                .equals(
                                    QUESTION_UPSKATTA_HUR_LANGE_TILLSTANDET_KOMMER_VARA_LIVSHOTANDE_ID))
                    .map(element -> (ElementValueBoolean) element.value())
                    .anyMatch(value -> Boolean.TRUE.equals(value.value())))
        .mapping(new ElementMapping(QUESTION_PATIENTENS_BEHANDLING_OCH_VARDSITUATION_ID, null))
        .pdfConfiguration(
            PdfConfigurationDate.builder()
                .pdfFieldId(PDF_CONDITION_IS_LIFE_THREATENING_TO_FIELD_ID)
                .build())
        .build();
  }
}
