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

import static se.inera.intyg.certificateservice.integrationtest.common.util.CertificateUtil.certificateId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import se.inera.intyg.certificateservice.application.certificate.dto.CreateCertificateResponse;
import se.inera.intyg.certificateservice.testability.certificate.dto.TestabilityCertificateRequest;
import se.inera.intyg.certificateservice.testability.certificate.dto.TestabilityResetCertificateRequest;

@Slf4j
@RequiredArgsConstructor
public class TestabilityApiUtil {

  private final TestRestTemplate restTemplate;
  private final int port;
  private List<String> certificateIds = new ArrayList<>();

  public List<CreateCertificateResponse> addCertificates(
      TestabilityCertificateRequest... requests) {
    return Stream.of(requests).map(request -> addCertificate(request).getBody()).toList();
  }

  public ResponseEntity<CreateCertificateResponse> addCertificate(
      TestabilityCertificateRequest request) {
    final var requestUrl = "http://localhost:%s/testability/certificate".formatted(port);
    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    final ResponseEntity<CreateCertificateResponse> response =
        this.restTemplate.exchange(
            requestUrl,
            HttpMethod.POST,
            new HttpEntity<>(request, headers),
            new ParameterizedTypeReference<>() {},
            Collections.emptyMap());

    if (certificateId(response.getBody()) != null) {
      certificateIds.add(certificateId(response.getBody()));
    }
    return response;
  }

  public void reset() {
    if (certificateIds.isEmpty()) {
      return;
    }

    final var requestUrl = "http://localhost:%s/testability/certificate".formatted(port);
    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    final var request =
        TestabilityResetCertificateRequest.builder().certificateIds(certificateIds).build();
    final var response =
        this.restTemplate.<Void>exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(request, headers),
            new ParameterizedTypeReference<>() {},
            Collections.emptyMap());
    if (response.getStatusCode() != HttpStatus.OK) {
      log.error(
          "Could not reset testability with request '%s'! StatusCode: '%s'"
              .formatted(request, response.getStatusCode()));
    }
  }
}
