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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0001.ANNAT;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0001.DIGITALUNDERSOKNING;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0001.FYSISKUNDERSOKNING;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0001.JOURNALUPPGIFTER;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0001.TELEFONKONTAKT;

import java.time.Period;
import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CheckboxDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationCheckboxMultipleDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;
import se.inera.intyg.certificateservice.domain.validation.model.ElementValidationDateList;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class QuestionGrundForMedicinsktUnderlag {

  public static final ElementId QUESTION_GRUND_FOR_MEDICINSKT_UNDERLAG_ID = new ElementId("10");
  public static final FieldId QUESTION_GRUND_FOR_MEDICINSKT_UNDERLAG_FIELD_ID = new FieldId("10.1");

  private QuestionGrundForMedicinsktUnderlag() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification questionGrundForMedicinsktUnderlag(
      ElementSpecification... children) {
    final var checkboxDates =
        List.of(
            CheckboxDate.create(FYSISKUNDERSOKNING, null, Period.ofDays(0)),
            CheckboxDate.create(DIGITALUNDERSOKNING, null, Period.ofDays(0)),
            CheckboxDate.create(TELEFONKONTAKT, null, Period.ofDays(0)),
            CheckboxDate.create(JOURNALUPPGIFTER, null, Period.ofDays(0)),
            CheckboxDate.create(ANNAT, null, Period.ofDays(0)));

    return ElementSpecification.builder()
        .id(QUESTION_GRUND_FOR_MEDICINSKT_UNDERLAG_ID)
        .configuration(
            ElementConfigurationCheckboxMultipleDate.builder()
                .id(QUESTION_GRUND_FOR_MEDICINSKT_UNDERLAG_FIELD_ID)
                .name("Intyget är baserat på")
                .dates(checkboxDates)
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.mandatory(
                    QUESTION_GRUND_FOR_MEDICINSKT_UNDERLAG_ID,
                    checkboxDates.stream().map(CheckboxDate::id).toList())))
        .validations(
            List.of(
                ElementValidationDateList.builder().mandatory(true).max(Period.ofDays(0)).build()))
        .children(List.of(children))
        .build();
  }
}
