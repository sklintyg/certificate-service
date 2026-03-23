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
package se.inera.intyg.certificateservice.integrationtest.ts8071.v2;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionOvrigBeskrivning.QUESTION_OVRIG_BESKRIVNING_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.ts8071.elements.common.QuestionOvrigBeskrivning.QUESTION_OVRIG_BESKRIVNING_ID;

import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseTestabilityUtilities;
import se.inera.intyg.certificateservice.integrationtest.common.setup.TestabilityAccess;
import se.inera.intyg.certificateservice.integrationtest.common.setup.TestabilityCertificate;

public class TS8071V2TestSetup {

  public static final String TYPE = "TS8071 - ";
  public static final String ACTIVE_CERTIFICATE_TYPE_VERSION = "2.0";
  private static final String CODE = "TS8071";
  private static final String CERTIFICATE_TYPE = "ts8071";
  private static final String RECIPIENT = "TRANSP";
  private static final String VALUE = "Svarstext för övrig beskrivning.";

  public static BaseTestabilityUtilities.BaseTestabilityUtilitiesBuilder ts8071V2TestSetup() {
    return BaseTestabilityUtilities.builder()
        .testabilityCertificate(
            TestabilityCertificate.builder()
                .type(CERTIFICATE_TYPE)
                .code(CODE)
                .activeVersion(ACTIVE_CERTIFICATE_TYPE_VERSION)
                .recipient(RECIPIENT)
                .valueForTest(
                    ElementData.builder()
                        .id(QUESTION_OVRIG_BESKRIVNING_ID)
                        .value(
                            ElementValueText.builder()
                                .textId(QUESTION_OVRIG_BESKRIVNING_FIELD_ID)
                                .text(VALUE)
                                .build())
                        .build())
                .testabilityAccess(
                    TestabilityAccess.builder()
                        .canReceiveQuestions(false)
                        .nurseCanForwardCertificate(true)
                        .midwifeCanForwardCertificate(true)
                        .canDentistsUseType(false)
                        .availableForPatient(true)
                        .midwifeCanMarkReadyForSignCertificate(true)
                        .nurseCanMarkReadyForSignCertificate(true)
                        .build())
                .build());
  }
}
