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
package se.inera.intyg.certificateservice.integrationtest.common.util;

import java.util.Collections;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import se.inera.intyg.certificateservice.patient.dto.PersonsResponseDTO;
import tools.jackson.databind.ObjectMapper;

public final class MockServerTestUtil {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private MockServerTestUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static MockServerClient createClient() {
    return new MockServerClient(
        Containers.MOCK_SERVER_CONTAINER.getHost(),
        Containers.MOCK_SERVER_CONTAINER.getServerPort());
  }

  public static void reset(MockServerClient mockServerClient) {
    mockServerClient.reset();
  }

  public static void mockIntygProxyService(MockServerClient mockServerClient) {
    try {
      mockServerClient
          .when(HttpRequest.request("/api/v1/persons"))
          .respond(
              HttpResponse.response()
                  .withBody(
                      OBJECT_MAPPER.writeValueAsString(
                          PersonsResponseDTO.builder().persons(Collections.emptyList()).build()))
                  .withStatusCode(200)
                  .withContentType(MediaType.APPLICATION_JSON));
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }
}
