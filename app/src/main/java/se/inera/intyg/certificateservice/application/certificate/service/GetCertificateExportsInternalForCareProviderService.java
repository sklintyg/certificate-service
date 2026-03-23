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
import se.inera.intyg.certificateservice.application.certificate.dto.ExportCertificateInternalRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.ExportCertificateInternalResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.ExportInternalResponse;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.Xml;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.certificate.service.XmlGenerator;
import se.inera.intyg.certificateservice.domain.common.model.HsaId;

@Service
@RequiredArgsConstructor
public class GetCertificateExportsInternalForCareProviderService {

  private final CertificateRepository certificateRepository;
  private final XmlGenerator xmlGenerator;

  public ExportInternalResponse get(
      ExportCertificateInternalRequest request, String careProviderId) {
    final var certificateExportPage =
        certificateRepository.getExportByCareProviderId(
            new HsaId(careProviderId), request.getPage(), request.getSize());

    final var exportedCertificates =
        certificateExportPage.certificates().stream()
            .map(
                certificate ->
                    ExportCertificateInternalResponse.builder()
                        .certificateId(certificate.id().id())
                        .xml(getXml(certificate).base64())
                        .revoked(certificate.revoked() != null)
                        .build())
            .toList();

    return ExportInternalResponse.builder().exports(exportedCertificates).build();
  }

  private Xml getXml(Certificate certificate) {
    return certificate.xml() != null
        ? certificate.xml()
        : xmlGenerator.generate(certificate, false);
  }
}
