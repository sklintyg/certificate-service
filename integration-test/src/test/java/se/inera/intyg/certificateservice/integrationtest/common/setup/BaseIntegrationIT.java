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
package se.inera.intyg.certificateservice.integrationtest.common.setup;

import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueBoolean;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.integrationtest.common.util.ApiUtil;
import se.inera.intyg.certificateservice.integrationtest.common.util.InternalApiUtil;
import se.inera.intyg.certificateservice.integrationtest.common.util.TestabilityApiUtil;

public abstract class BaseIntegrationIT {

  protected String wrongVersion() {
    return "wrongVersion";
  }

  protected String codeSystem() {
    return "b64ea353-e8f6-4832-b563-fc7d46f29548";
  }

  protected BaseTestabilityUtilities testabilityUtilities() {
    return BaseTestabilityUtilities.builder().build();
  }

  protected ApiUtil api() {
    return testabilityUtilities().getTestabilityUtilities().getApi();
  }

  protected InternalApiUtil internalApi() {
    return testabilityUtilities().getTestabilityUtilities().getInternalApi();
  }

  protected TestabilityApiUtil testabilityApi() {
    return testabilityUtilities().getTestabilityUtilities().getTestabilityApi();
  }

  protected String type() {
    return testabilityUtilities().getTestabilityCertificate().getType();
  }

  protected String typeVersion() {
    return testabilityUtilities().getTestabilityCertificate().getActiveVersion();
  }

  protected ElementId element() {
    return testabilityUtilities().getTestabilityCertificate().getValueForTest().id();
  }

  protected Object value() {
    final var elementValue =
        testabilityUtilities().getTestabilityCertificate().getValueForTest().value();

    if (elementValue instanceof ElementValueText elementValueText) {
      return elementValueText.text();
    }

    if (elementValue instanceof ElementValueBoolean elementValueBoolean) {
      return elementValueBoolean.value();
    }

    if (elementValue instanceof ElementValueDate elementValueDate) {
      return elementValueDate.date();
    }

    throw new IllegalStateException("Unknown element value type: " + elementValue.getClass());
  }

  protected String code() {
    return testabilityUtilities().getTestabilityCertificate().getCode();
  }

  protected Boolean canReceiveQuestions() {
    return testabilityUtilities()
        .getTestabilityCertificate()
        .getTestabilityAccess()
        .isCanReceiveQuestions();
  }

  protected boolean nurseCanForwardCertificate() {
    return testabilityUtilities()
        .getTestabilityCertificate()
        .getTestabilityAccess()
        .isNurseCanForwardCertificate();
  }

  protected boolean midwifeCanForwardCertificate() {
    return testabilityUtilities()
        .getTestabilityCertificate()
        .getTestabilityAccess()
        .isMidwifeCanForwardCertificate();
  }

  protected boolean canDentistsUseType() {
    return testabilityUtilities()
        .getTestabilityCertificate()
        .getTestabilityAccess()
        .isCanDentistsUseType();
  }

  protected boolean midwifeCanMarkReadyForSignCertificate() {
    return testabilityUtilities()
        .getTestabilityCertificate()
        .getTestabilityAccess()
        .isMidwifeCanMarkReadyForSignCertificate();
  }

  protected boolean nurseCanMarkReadyForSignCertificate() {
    return testabilityUtilities()
        .getTestabilityCertificate()
        .getTestabilityAccess()
        .isNurseCanMarkReadyForSignCertificate();
  }

  protected boolean isAvailableForPatient() {
    return testabilityUtilities()
        .getTestabilityCertificate()
        .getTestabilityAccess()
        .isAvailableForPatient();
  }

  protected String recipient() {
    return testabilityUtilities().getTestabilityCertificate().getRecipient();
  }

  protected String questionId() {
    return testabilityUtilities().getTestabilityCertificate().getValueForTest().id().id();
  }
}
