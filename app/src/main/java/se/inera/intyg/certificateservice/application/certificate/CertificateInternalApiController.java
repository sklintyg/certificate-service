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
package se.inera.intyg.certificateservice.application.certificate;

import static se.inera.intyg.certificateservice.logging.MdcLogConstants.EVENT_TYPE_ACCESSED;
import static se.inera.intyg.certificateservice.logging.MdcLogConstants.EVENT_TYPE_CHANGE;
import static se.inera.intyg.certificateservice.logging.MdcLogConstants.EVENT_TYPE_DELETION;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateExistsResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificatesWithQAInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificatesWithQAInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.ExportCertificateInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.ExportInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateCountIssuedByRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateCountIssuedByResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateInternalMetadataResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.GetCertificateInternalXmlResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.GetSickLeaveCertificateInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.GetSickLeaveCertificateInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.GetValidSickLeaveCertificateIdsInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.GetValidSickLeaveCertificateIdsInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.LockDraftsRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.LockDraftsResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.TotalExportsInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.service.CertificateExistsService;
import se.inera.intyg.certificateservice.application.certificate.service.EraseCertificateInternalForCareProviderService;
import se.inera.intyg.certificateservice.application.certificate.service.GetCertificateCountIssuedByInternalService;
import se.inera.intyg.certificateservice.application.certificate.service.GetCertificateExportsInternalForCareProviderService;
import se.inera.intyg.certificateservice.application.certificate.service.GetCertificateInternalMetadataService;
import se.inera.intyg.certificateservice.application.certificate.service.GetCertificateInternalService;
import se.inera.intyg.certificateservice.application.certificate.service.GetCertificateInternalXmlService;
import se.inera.intyg.certificateservice.application.certificate.service.GetSickLeaveCertificateInternalService;
import se.inera.intyg.certificateservice.application.certificate.service.GetTotalExportsInternalForCareProviderService;
import se.inera.intyg.certificateservice.application.certificate.service.GetValidSickLeaveCertificatesInternalService;
import se.inera.intyg.certificateservice.application.certificate.service.LockDraftsInternalService;
import se.inera.intyg.certificateservice.application.certificate.service.PlaceholderCertificateExistsService;
import se.inera.intyg.certificateservice.application.certificate.service.RevokePlaceholderCertificateInternalService;
import se.inera.intyg.certificateservice.application.patient.service.GetCertificatesWithQAInternalService;
import se.inera.intyg.certificateservice.logging.PerformanceLogging;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internalapi/certificate")
public class CertificateInternalApiController {

  private final EraseCertificateInternalForCareProviderService
      eraseCertificateInternalForCareProviderService;
  private final GetTotalExportsInternalForCareProviderService
      getTotalExportsInternalForCareProviderService;
  private final GetCertificateExportsInternalForCareProviderService
      getCertificateExportsInternalForCareProviderService;
  private final GetCertificateInternalXmlService getCertificateInternalXmlService;
  private final GetCertificateInternalMetadataService getCertificateInternalMetadataService;
  private final GetCertificateInternalService getCertificateInternalService;
  private final CertificateExistsService certificateExistsService;
  private final LockDraftsInternalService lockDraftsInternalService;
  private final GetCertificatesWithQAInternalService getCertificatesWithQAInternalService;
  private final PlaceholderCertificateExistsService placeholderCertificateExistsService;
  private final RevokePlaceholderCertificateInternalService
      revokePlaceholderCertificateInternalService;
  private final GetSickLeaveCertificateInternalService getSickLeaveCertificateInternalService;
  private final GetValidSickLeaveCertificatesInternalService
      getValidSickLeaveCertificatesInternalService;
  private final GetCertificateCountIssuedByInternalService
      getCertificateCountIssuedByInternalService;

  @GetMapping("/{certificateId}/exists")
  @PerformanceLogging(
      eventAction = "internal-find-existing-certificate",
      eventType = EVENT_TYPE_ACCESSED)
  CertificateExistsResponse findExistingCertificate(
      @PathVariable("certificateId") String certificateId) {
    return certificateExistsService.exist(certificateId);
  }

  @PostMapping("/{certificateId}/xml")
  @PerformanceLogging(
      eventAction = "internal-retrieve-certificate-xml",
      eventType = EVENT_TYPE_ACCESSED)
  GetCertificateInternalXmlResponse getCertificateXml(
      @PathVariable("certificateId") String certificateId) {
    return getCertificateInternalXmlService.get(certificateId);
  }

  @GetMapping("/{certificateId}/metadata")
  @PerformanceLogging(
      eventAction = "internal-retrieve-certificate-metadata",
      eventType = EVENT_TYPE_ACCESSED)
  GetCertificateInternalMetadataResponse getCertificateMetadata(
      @PathVariable("certificateId") String certificateId) {
    return getCertificateInternalMetadataService.get(certificateId);
  }

  @PostMapping("/{certificateId}")
  @PerformanceLogging(
      eventAction = "internal-retrieve-certificate",
      eventType = EVENT_TYPE_ACCESSED)
  GetCertificateInternalResponse getCertificate(
      @PathVariable("certificateId") String certificateId) {
    return getCertificateInternalService.get(certificateId);
  }

  @PostMapping("/{certificateId}/sickleave")
  @PerformanceLogging(
      eventAction = "internal-retrieve-sick-leave-certificate",
      eventType = EVENT_TYPE_ACCESSED)
  GetSickLeaveCertificateInternalResponse getSickLeaveCertificate(
      @PathVariable("certificateId") String certificateId,
      @RequestBody(required = false) GetSickLeaveCertificateInternalRequest request) {
    return getSickLeaveCertificateInternalService.get(
        certificateId, request != null && request.isIgnoreModelRules());
  }

  @PostMapping("/lock")
  @PerformanceLogging(eventAction = "internal-lock-certificate", eventType = EVENT_TYPE_CHANGE)
  LockDraftsResponse lockDrafts(@RequestBody LockDraftsRequest request) {
    return lockDraftsInternalService.lock(request);
  }

  @PostMapping("/qa")
  @PerformanceLogging(
      eventAction = "internal-retrieve-certificate-with-qa",
      eventType = EVENT_TYPE_ACCESSED)
  CertificatesWithQAInternalResponse getCertificatesWithQA(
      @RequestBody CertificatesWithQAInternalRequest request) {
    return getCertificatesWithQAInternalService.get(request);
  }

  @PostMapping("/export/{careProviderId}")
  @PerformanceLogging(
      eventAction = "internal-retrieve-export-certificates-for-care-provider",
      eventType = EVENT_TYPE_ACCESSED)
  ExportInternalResponse getExportCertificatesForCareProvider(
      @RequestBody ExportCertificateInternalRequest request,
      @PathVariable("careProviderId") String careProviderId) {
    return getCertificateExportsInternalForCareProviderService.get(request, careProviderId);
  }

  @GetMapping("/export/{careProviderId}/total")
  @PerformanceLogging(
      eventAction = "internal-retrieve-total-export-for-care-provider",
      eventType = EVENT_TYPE_ACCESSED)
  TotalExportsInternalResponse getTotalExportsForCareProvider(
      @PathVariable("careProviderId") String careProviderId) {
    return getTotalExportsInternalForCareProviderService.get(careProviderId);
  }

  @DeleteMapping("/erase/{careProviderId}")
  @PerformanceLogging(
      eventAction = "internal-erase-certificates-for-care-provider",
      eventType = EVENT_TYPE_DELETION)
  void eraseCertificates(@PathVariable("careProviderId") String careProviderId) {
    eraseCertificateInternalForCareProviderService.erase(careProviderId);
  }

  @GetMapping("/placeholder/{certificateId}/exists")
  @PerformanceLogging(
      eventAction = "find-existing-placeholder-certificate",
      eventType = EVENT_TYPE_ACCESSED)
  CertificateExistsResponse findExistingPlaceholderCertificate(
      @PathVariable("certificateId") String certificateId) {
    return placeholderCertificateExistsService.exist(certificateId);
  }

  @PostMapping("/placeholder/{certificateId}/revoke")
  @PerformanceLogging(
      eventAction = "internal-revoke-placeholder-certificate",
      eventType = EVENT_TYPE_ACCESSED)
  void revokePlaceholderCertificate(@PathVariable("certificateId") String certificateId) {
    revokePlaceholderCertificateInternalService.revoke(certificateId);
  }

  @PostMapping("/sickleave/valid")
  @PerformanceLogging(
      eventAction = "internal-get-valid-sick-leave-certificate-ids",
      eventType = EVENT_TYPE_ACCESSED)
  GetValidSickLeaveCertificateIdsInternalResponse getValidSickLeaveCertificateIds(
      @RequestBody GetValidSickLeaveCertificateIdsInternalRequest request) {
    return getValidSickLeaveCertificatesInternalService.get(request);
  }

  @PostMapping("/issuedBy")
  @PerformanceLogging(
      eventAction = "internal-certificate-count-issuedBy",
      eventType = EVENT_TYPE_ACCESSED)
  GetCertificateCountIssuedByResponse getCertificateCountIssuedBy(
      @RequestBody GetCertificateCountIssuedByRequest request) {
    return getCertificateCountIssuedByInternalService.get(request);
  }
}
