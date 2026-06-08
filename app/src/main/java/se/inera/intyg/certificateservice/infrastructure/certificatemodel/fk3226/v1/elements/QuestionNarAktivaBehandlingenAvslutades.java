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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.v1.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.v1.elements.QuestionPatientensBehandlingOchVardsituation.ENDAST_PALLIATIV_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.v1.elements.QuestionPatientensBehandlingOchVardsituation.QUESTION_PATIENTENS_BEHANDLING_OCH_VARDSITUATION_ID;

import java.time.Period;
import java.util.List;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfConfigurationDate;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationDate;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificatePrintTagProvider;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateTextProvider;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.v1.FK3226PrintTagKey;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk3226.v1.FK3226TextKey;

public class QuestionNarAktivaBehandlingenAvslutades {

  public static final ElementId QUESTION_NAR_AKTIVA_BEHANDLINGEN_AVSLUTADES_ID =
      new ElementId("52.2");
  private static final FieldId QUESTION_NAR_AKTIVA_BEHANDLINGEN_AVSLUTADES_FIELD_ID =
      new FieldId("52.2");

  private QuestionNarAktivaBehandlingenAvslutades() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionNarAktivaBehandlingenAvslutades(
      CertificateTextProvider texts, CertificatePrintTagProvider printTags) {
    return ElementSpecification.builder()
        .id(QUESTION_NAR_AKTIVA_BEHANDLINGEN_AVSLUTADES_ID)
        .configuration(
            ElementConfigurationDate.builder()
                .id(QUESTION_NAR_AKTIVA_BEHANDLINGEN_AVSLUTADES_FIELD_ID)
                .name(texts.text(FK3226TextKey.QUESTION_NAR_AKTIVA_BEHANDLINGEN_AVSLUTADES_NAME))
                .max(Period.ofDays(0))
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatory(
                    QUESTION_NAR_AKTIVA_BEHANDLINGEN_AVSLUTADES_ID,
                    QUESTION_NAR_AKTIVA_BEHANDLINGEN_AVSLUTADES_FIELD_ID),
                CertificateElementRuleFactory.show(
                    QUESTION_PATIENTENS_BEHANDLING_OCH_VARDSITUATION_ID,
                    ENDAST_PALLIATIV_FIELD_ID)))
        .validations(
            List.of(ElementValidationDate.builder().mandatory(true).max(Period.ofDays(0)).build()))
        .shouldValidate(
            elementData ->
                elementData.stream()
                    .filter(
                        data ->
                            data.id().equals(QUESTION_PATIENTENS_BEHANDLING_OCH_VARDSITUATION_ID))
                    .map(element -> (ElementValueCode) element.value())
                    .anyMatch(value -> value.codeId().equals(ENDAST_PALLIATIV_FIELD_ID)))
        .mapping(new ElementMapping(QUESTION_PATIENTENS_BEHANDLING_OCH_VARDSITUATION_ID, null))
        .pdfConfiguration(
            PdfConfigurationDate.builder()
                .pdfFieldId(
                    printTags.fieldId(
                        FK3226PrintTagKey.QUESTION_NAR_AKTIVA_BEHANDLINGEN_AVSLUTADES_DATE))
                .build())
        .build();
  }
}
