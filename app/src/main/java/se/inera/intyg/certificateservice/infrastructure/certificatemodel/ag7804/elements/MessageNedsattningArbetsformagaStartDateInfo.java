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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804.elements.QuestionNedsattningArbetsformaga.QUESTION_NEDSATTNING_ARBETSFORMAGA_ID;

import java.util.List;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementConfigurationMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementMessage;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementSpecification;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageLevel;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.RuleExpression;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.CertificateElementRuleFactory;
import se.inera.intyg.certificateservice.infrastructure.certificatemodel.common.codesystems.CodeSystemKvFkmu0003;

public class MessageNedsattningArbetsformagaStartDateInfo {

  public static final ElementId MESSAGE_START_DATE_INFO_ID = new ElementId("earlyStartDate");

  private MessageNedsattningArbetsformagaStartDateInfo() {
    throw new IllegalStateException("Utility class");
  }

  public static ElementSpecification messageStartDateInfo() {
    return ElementSpecification.builder()
        .id(MESSAGE_START_DATE_INFO_ID)
        .configuration(
            ElementConfigurationMessage.builder()
                .message(
                    ElementMessage.builder()
                        .content(
                            "Du har angett att sjukskrivningsperioden startar mer än 7 dagar bakåt i tiden. Ange orsaken till detta i fältet \"Övriga upplysningar\".")
                        .level(MessageLevel.OBSERVE)
                        .build())
                .build())
        .rules(
            List.of(
                CertificateElementRuleFactory.show(
                    QUESTION_NEDSATTNING_ARBETSFORMAGA_ID,
                    new RuleExpression(
                        String.format(
                            "($%s.from < -7) || ($%s.from < -7) || ($%s.from < -7) || ($%s.from < -7)",
                            CodeSystemKvFkmu0003.EN_FJARDEDEL.code(),
                            CodeSystemKvFkmu0003.HALFTEN.code(),
                            CodeSystemKvFkmu0003.TRE_FJARDEDEL.code(),
                            CodeSystemKvFkmu0003.HELT_NEDSATT.code())))))
        .build();
  }
}
