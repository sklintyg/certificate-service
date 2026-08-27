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
package se.inera.intyg.certificateservice.application.certificate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificate.dto.GetBinaryCertificateInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.service.converter.BinaryCertificateMetadataConverter;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.certificate.service.GetCertificateInternalPdfDomainService;

@Service
@RequiredArgsConstructor
public class GetBinaryCertificateInternalService {

  private final CertificateRepository certificateRepository;
  private final GetCertificateInternalPdfDomainService getCertificateInternalPdfDomainService;
  private final BinaryCertificateMetadataConverter binaryCertificateMetadataConverter;

  public GetBinaryCertificateInternalResponse get(String certificateId) {
    if (certificateId == null || certificateId.isBlank()) {
      throw new IllegalArgumentException("Certificate id cannot be null or empty");
    }

    final var id = new CertificateId(certificateId);
    final var certificate = certificateRepository.getById(id);

    if (certificate.isDraft()) {
      throw new IllegalStateException(
          "Certificate with id %s cannot be draft".formatted(certificateId));
    }

    final var pdf = getCertificateInternalPdfDomainService.get(id);

    return GetBinaryCertificateInternalResponse.builder()
        .metadata(binaryCertificateMetadataConverter.convert(certificate))
        .pdfData(pdf.pdfData())
        .build();
  }
}
