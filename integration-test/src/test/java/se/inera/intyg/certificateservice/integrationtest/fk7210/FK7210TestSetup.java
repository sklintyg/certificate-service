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
package se.inera.intyg.certificateservice.integrationtest.fk7210;

import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.elements.QuestionBeraknatFodelsedatum.QUESTION_BERAKNAT_FODELSEDATUM_FIELD_ID;
import static se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7210.elements.QuestionBeraknatFodelsedatum.QUESTION_BERAKNAT_FODELSEDATUM_ID;

import java.time.LocalDate;
import java.time.ZoneId;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseTestabilityUtilities;
import se.inera.intyg.certificateservice.integrationtest.common.setup.TestabilityAccess;
import se.inera.intyg.certificateservice.integrationtest.common.setup.TestabilityCertificate;

public class FK7210TestSetup {

  public static final String TYPE = "FK7210 - ";
  private static final String CODE = "IGRAV";
  private static final String CERTIFICATE_TYPE = "fk7210";
  private static final String ACTIVE_CERTIFICATE_TYPE_VERSION = "1.0";
  private static final String RECIPIENT = "FKASSA";
  private static final LocalDate VALUE = LocalDate.now(ZoneId.systemDefault()).plusDays(5);

  public static BaseTestabilityUtilities.BaseTestabilityUtilitiesBuilder fk7210TestSetup() {
    return BaseTestabilityUtilities.builder()
        .testabilityCertificate(
            TestabilityCertificate.builder()
                .type(CERTIFICATE_TYPE)
                .code(CODE)
                .activeVersion(ACTIVE_CERTIFICATE_TYPE_VERSION)
                .recipient(RECIPIENT)
                .valueForTest(
                    ElementData.builder()
                        .id(QUESTION_BERAKNAT_FODELSEDATUM_ID)
                        .value(
                            ElementValueDate.builder()
                                .dateId(QUESTION_BERAKNAT_FODELSEDATUM_FIELD_ID)
                                .date(VALUE)
                                .build())
                        .build())
                .testabilityAccess(
                    TestabilityAccess.builder()
                        .canReceiveQuestions(false)
                        .nurseCanForwardCertificate(false)
                        .midwifeCanForwardCertificate(false)
                        .canDentistsUseType(false)
                        .availableForPatient(true)
                        .midwifeCanMarkReadyForSignCertificate(false)
                        .nurseCanMarkReadyForSignCertificate(false)
                        .build())
                .build());
  }
}
