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

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificatePdfResponse;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
public class CertificatePrintServiceMock {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private final MockServerClient mockServerClient;

  public void mockCustomPdf() {
    try {
      final byte[] stubPdf = "%PDF-1.4".getBytes(StandardCharsets.UTF_8);
      final byte[] encodedPdfData = Base64.getEncoder().encode(stubPdf);

      mockServerClient
          .when(HttpRequest.request("/api/print/custom").withMethod("POST"))
          .respond(
              HttpResponse.response()
                  .withBody(
                      OBJECT_MAPPER.writeValueAsString(
                          Map.of("pdfData", Base64.getEncoder().encodeToString(encodedPdfData))))
                  .withStatusCode(200)
                  .withContentType(MediaType.APPLICATION_JSON));
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }

  public void mockPdf() {
    try {
      mockServerClient
          .when(HttpRequest.request("/api/print/general").withMethod("POST"))
          .respond(
              HttpResponse.response(
                      OBJECT_MAPPER.writeValueAsString(
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
