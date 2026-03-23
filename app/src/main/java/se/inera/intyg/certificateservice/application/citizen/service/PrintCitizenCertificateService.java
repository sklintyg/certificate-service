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
package se.inera.intyg.certificateservice.application.citizen.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.citizen.dto.PrintCitizenCertificateRequest;
import se.inera.intyg.certificateservice.application.citizen.dto.PrintCitizenCertificateResponse;
import se.inera.intyg.certificateservice.application.citizen.validation.CitizenCertificateRequestValidator;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementId;
import se.inera.intyg.certificateservice.domain.citizen.service.PrintCitizenCertificateDomainService;
import se.inera.intyg.certificateservice.domain.common.model.PersonId;

@Service
@RequiredArgsConstructor
public class PrintCitizenCertificateService {

  private final PrintCitizenCertificateDomainService printCitizenCertificateDomainService;
  private final CitizenCertificateRequestValidator citizenCertificateRequestValidator;

  public PrintCitizenCertificateResponse get(
      PrintCitizenCertificateRequest request, String certificateId) {
    citizenCertificateRequestValidator.validate(certificateId, request.getPersonId());
    final var pdf =
        printCitizenCertificateDomainService.get(
            new CertificateId(certificateId),
            PersonId.builder()
                .id(request.getPersonId().getId())
                .type(request.getPersonId().getType().toPersonIdType())
                .build(),
            request.getAdditionalInfo(),
            request.getCustomizationId() != null
                ? List.of(new ElementId(request.getCustomizationId()))
                : List.of());

    return PrintCitizenCertificateResponse.builder()
        .filename(pdf.fileName())
        .pdfData(pdf.pdfData())
        .build();
  }
}
