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
package se.inera.intyg.certificateservice.patient.integration;

import static se.inera.intyg.certificateservice.logging.MdcHelper.LOG_SESSION_ID_HEADER;
import static se.inera.intyg.certificateservice.logging.MdcHelper.LOG_TRACE_ID_HEADER;
import static se.inera.intyg.certificateservice.logging.MdcLogConstants.EVENT_TYPE_ACCESSED;
import static se.inera.intyg.certificateservice.logging.MdcLogConstants.SESSION_ID_KEY;
import static se.inera.intyg.certificateservice.logging.MdcLogConstants.TRACE_ID_KEY;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import se.inera.intyg.certificateservice.logging.PerformanceLogging;
import se.inera.intyg.certificateservice.patient.dto.PersonsRequestDTO;
import se.inera.intyg.certificateservice.patient.dto.PersonsResponseDTO;

@Service
public class IPSIntegrationService {

  private final RestClient ipsRestClient;

  public IPSIntegrationService(@Qualifier("ipsRestClient") RestClient ipsRestClient) {
    this.ipsRestClient = ipsRestClient;
  }

  @PerformanceLogging(eventAction = "find-persons-in-ips", eventType = EVENT_TYPE_ACCESSED)
  public PersonsResponseDTO findPersons(PersonsRequestDTO request) {
    return ipsRestClient
        .post()
        .uri("/api/v1/persons")
        .body(request)
        .header(LOG_TRACE_ID_HEADER, MDC.get(TRACE_ID_KEY))
        .header(LOG_SESSION_ID_HEADER, MDC.get(SESSION_ID_KEY))
        .contentType(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(PersonsResponseDTO.class);
  }
}
