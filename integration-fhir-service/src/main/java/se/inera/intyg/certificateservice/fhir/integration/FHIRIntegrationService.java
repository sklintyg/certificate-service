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
package se.inera.intyg.certificateservice.fhir.integration;

import static se.inera.intyg.certificateservice.logging.MdcLogConstants.EVENT_TYPE_ACCESSED;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r5.model.Questionnaire;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.logging.PerformanceLogging;

@Service
public class FHIRIntegrationService {

  @PerformanceLogging(eventAction = "get-questionnaire", eventType = EVENT_TYPE_ACCESSED)
  public Questionnaire getQuestionnaire() {
    final var ctx = FhirContext.forR5();

    final var client = ctx.newRestfulGenericClient("https://hapi.fhir.org/baseR5");

    return client.read().resource(Questionnaire.class).withId("898148").execute();
  }
}