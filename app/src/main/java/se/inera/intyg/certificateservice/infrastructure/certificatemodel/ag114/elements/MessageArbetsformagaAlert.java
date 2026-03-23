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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionPeriodBedomning.QUESTION_PERIOD_BEDOMNING_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag114.elements.QuestionPeriodBedomning.QUESTION_PERIOD_BEDOMNING_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageLevel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class MessageArbetsformagaAlert {

  public static final ElementId MESSAGE_ARBETSFORMAGA_ALERT_ID =
      new ElementId("messageArbetsformaga");

  private MessageArbetsformagaAlert() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification messageArbetsformagaAlert() {
    return ElementSpecification.builder()
        .id(MESSAGE_ARBETSFORMAGA_ALERT_ID)
        .configuration(
            ElementConfigurationMessage.builder()
                .message(
                    ElementMessage.builder()
                        .content(
                            "Om patienten bedöms vara arbetsoförmögen i mer än 14 dagar ska Läkarintyg om arbetsförmåga – arbetsgivaren (AG7804) användas. Intyget skapas från Försäkringskassans läkarintyg för sjukpenning (FK7804) och innehåller motsvarande information.")
                        .level(MessageLevel.INFO)
                        .build())
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.show(
                    QUESTION_PERIOD_BEDOMNING_ID,
                    new RuleExpression(
                        String.format(
                            "($%s.to - $%s.from) >= 14",
                            QUESTION_PERIOD_BEDOMNING_FIELD_ID.value(),
                            QUESTION_PERIOD_BEDOMNING_FIELD_ID.value())))))
        .build();
  }
}
