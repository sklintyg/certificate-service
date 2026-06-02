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

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory.withCitation;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.elements.QuestionBeraknatFodelsedatum.QUESTION_BERAKNAT_FODELSEDATUM_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.elements.QuestionBeraknatFodelsedatum.QUESTION_BERAKNAT_FODELSEDATUM_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageLevel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;

public class MessageBeraknatFodelsedatum {

  private MessageBeraknatFodelsedatum() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification messageBeraknatFodelsedatum() {
    return ElementSpecification.builder()
        .id(new ElementId("fodelsedatum"))
        .configuration(
            ElementConfigurationMessage.builder()
                .message(
                    ElementMessage.builder()
                        .content(
                            """
                            Du har angivit dagens datum som beräknat födelsedatum.<br>
                            Säkerställ att datumet stämmer.
                            """)
                        .level(MessageLevel.INFO)
                        .build())
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.show(
                    QUESTION_BERAKNAT_FODELSEDATUM_ID,
                    new RuleExpression(
                        CertificateElementRuleFactory.today(
                            withCitation(QUESTION_BERAKNAT_FODELSEDATUM_FIELD_ID.value()))))))
        .build();
  }
}
