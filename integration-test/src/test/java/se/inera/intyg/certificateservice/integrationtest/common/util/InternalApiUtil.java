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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateExistsResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificatesWithQAInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificatesWithQAInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.DisposeObsoleteDraftsRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.DisposeObsoleteDraftsResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateInternalMetadataResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateInternalXmlResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.GetSentInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.GetSickLeaveCertificatesInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.GetSickLeaveCertificatesInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.ListObsoleteDraftsRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.ListObsoleteDraftsResponse;
import se.inera.intyg.certificateservice.application.message.dto.GetCertificateMessageInternalResponse;
import se.inera.intyg.certificateservice.application.message.dto.GetMessageInternalXmlResponse;
import se.inera.intyg.certificateservice.application.message.dto.GetSentMessagesCountResponse;

@Slf4j
@RequiredArgsConstructor
public class InternalApiUtil {

  private final TestRestTemplate restTemplate;
  private final int port;

  public ResponseEntity<GetCertificateInternalXmlResponse> getCertificateXml(String certificateId) {
    final var requestUrl =
        "http://localhost:%s/internalapi/certificate/%s/xml".formatted(port, certificateId);

    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.POST,
        new HttpEntity<>(null, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public ResponseEntity<GetCertificateInternalMetadataResponse> getCertificateMetadata(
      String certificateId) {
    final var requestUrl =
        "http://localhost:%s/internalapi/certificate/%s/metadata".formatted(port, certificateId);

    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.GET,
        new HttpEntity<>(null, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public ResponseEntity<GetCertificateInternalResponse> getCertificate(String certificateId) {
    final var requestUrl =
        "http://localhost:%s/internalapi/certificate/%s".formatted(port, certificateId);

    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.POST,
        new HttpEntity<>(null, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public ResponseEntity<CertificateExistsResponse> certificateExists(String certificateId) {
    final var requestUrl =
        "http://localhost:%s/internalapi/certificate/%s/exists".formatted(port, certificateId);
    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.GET,
        new HttpEntity<>(headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public ResponseEntity<GetCertificateMessageInternalResponse> getMessages(String certificateId) {
    final var requestUrl =
        "http://localhost:%s/internalapi/message/%s".formatted(port, certificateId);

    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.POST,
        new HttpEntity<>(null, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public ResponseEntity<GetMessageInternalXmlResponse> getMessageXml(String messageId) {
    final var requestUrl =
        "http://localhost:%s/internalapi/message/%s/xml".formatted(port, messageId);

    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.POST,
        new HttpEntity<>(null, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public ResponseEntity<CertificatesWithQAInternalResponse> getCertificatesInternalWithQA(
      CertificatesWithQAInternalRequest request) {
    final var requestUrl = "http://localhost:%s/internalapi/certificate/qa".formatted(port);

    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.POST,
        new HttpEntity<>(request, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public ResponseEntity<GetSickLeaveCertificatesInternalResponse> getSickLeaveCertificatesInternal(
      GetSickLeaveCertificatesInternalRequest request) {
    final var requestUrl = "http://localhost:%s/internalapi/patient/sickleave".formatted(port);

    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.POST,
        new HttpEntity<>(request, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyList());
  }

  public ResponseEntity<ListObsoleteDraftsResponse> listObsoleteDrafts(
      ListObsoleteDraftsRequest request) {
    final var requestUrl = "http://localhost:%s/internalapi/draft/list".formatted(port);

    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.POST,
        new HttpEntity<>(request, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public ResponseEntity<DisposeObsoleteDraftsResponse> disposeObsoleteDrafts(
      DisposeObsoleteDraftsRequest request) {
    final var requestUrl = "http://localhost:%s/internalapi/draft".formatted(port);

    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.DELETE,
        new HttpEntity<>(request, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public ResponseEntity<GetSentMessagesCountResponse> getUnansweredCommunicationMessages(
      GetSentInternalRequest request) {
    final var requestUrl = "http://localhost:%s/internalapi/message/sent".formatted(port);

    final var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    return this.restTemplate.exchange(
        requestUrl,
        HttpMethod.POST,
        new HttpEntity<>(request, headers),
        new ParameterizedTypeReference<>() {},
        Collections.emptyMap());
  }

  public void reset() {
    // Nothing to reset right now...
  }
}
