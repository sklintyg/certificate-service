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

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificatePdfResponse;

@RequiredArgsConstructor
public class CertificatePrintServiceMock {

  private final MockServerClient mockServerClient;

  public void mockPdf() {
    try {
      mockServerClient
          .when(HttpRequest.request("/api/print"))
          .respond(
              HttpResponse.response(
                      new ObjectMapper()
                          .writeValueAsString(
                              GetCertificatePdfResponse.builder()
                                  .fileName("lakarintyg_transportstyrelsen")
                                  .pdfData("pdfData".getBytes())
                                  .build()))
                  .withStatusCode(200)
                  .withContentType(MediaType.APPLICATION_JSON));
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }
}
