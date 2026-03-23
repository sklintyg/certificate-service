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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionFinnsArbetsformaga.QUESTION_FINNS_ARBETSFORMAGA_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionFinnsArbetsformaga.QUESTION_FINNS_ARBETSFORMAGA_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationTextArea;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMapping;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationText;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.ElementDataPredicateFactory;

public class QuestionBeskrivArbetsformagan {

  public static final ElementId QUESTION_BESKRIV_ARBETSFORMAGAN_ID = new ElementId("6.2");
  public static final FieldId QUESTION_BESKRIV_ARBETSFORMAGAN_FIELD_ID = new FieldId("6.2");

  private QuestionBeskrivArbetsformagan() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionBeskrivArbetsformagan() {
    return ElementSpecification.builder()
        .id(QUESTION_BESKRIV_ARBETSFORMAGAN_ID)
        .configuration(
            ElementConfigurationTextArea.builder()
                .id(QUESTION_BESKRIV_ARBETSFORMAGAN_FIELD_ID)
                .name("Beskriv arbetsförmågan")
                .description(
                    "Svar på nedanstående frågor kan ge arbetsgivaren vägledning när det gäller eventuell anpassning av arbetsuppgifter, behov av arbetsresor eller möjlighet för arbetstagaren att hålla kontakten med arbetsplatsen.<ol><li>Vilka arbetsuppgifter kan arbetstagaren utföra trots sin nedsatta arbetsförmåga?</li><li>Vilka arbetsuppgifter och moment bör arbetstagaren inte alls utföra av medicinska skäl?</li><li>Kan t ex arbetsresor till arbetet hjälpa?</li><li>Är det möjligt för arbetstagaren att vistas på arbetsplatsen vid till exempel arbetsplatsträffar?</li></ol>")
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.show(
                    QUESTION_FINNS_ARBETSFORMAGA_ID, QUESTION_FINNS_ARBETSFORMAGA_FIELD_ID),
                CertificateElementRuleFactory.mandatory(
                    QUESTION_BESKRIV_ARBETSFORMAGAN_ID, QUESTION_BESKRIV_ARBETSFORMAGAN_FIELD_ID)))
        .validations(List.of(ElementValidationText.builder().mandatory(true).build()))
        .mapping(new ElementMapping(QUESTION_FINNS_ARBETSFORMAGA_ID, null))
        .shouldValidate(
            ElementDataPredicateFactory.radioBooleans(
                List.of(QUESTION_FINNS_ARBETSFORMAGA_ID), true))
        .build();
  }
}
