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
package se.inera.intyg.certificateservice.application.certificatetypeinfo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.CertificateModelIdDTO;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.GetLatestCertificateExternalTypeVersionResponse;
import se.inera.intyg.certificateservice.domain.certificatemodel.repository.CertificateModelRepository;
import se.inera.intyg.certificateservice.domain.common.model.Code;

@Service
@RequiredArgsConstructor
public class GetLatestCertificateExternalTypeVersionService {

  private final CertificateModelRepository certificateModelRepository;

  public GetLatestCertificateExternalTypeVersionResponse get(String codeSystem, String code) {
    validateParameters(codeSystem, code);

    return certificateModelRepository
        .findLatestActiveByExternalType(new Code(code, codeSystem, null))
        .map(
            certificateModel ->
                GetLatestCertificateExternalTypeVersionResponse.builder()
                    .certificateModelId(
                        CertificateModelIdDTO.builder()
                            .type(certificateModel.id().type().type())
                            .version(certificateModel.id().version().version())
                            .build())
                    .build())
        .orElse(GetLatestCertificateExternalTypeVersionResponse.builder().build());
  }

  private static void validateParameters(String codeSystem, String code) {
    if (codeSystem == null || codeSystem.isBlank()) {
      throw new IllegalArgumentException("Required parameter missing: 'codeSystem'");
    }

    if (code == null || code.isBlank()) {
      throw new IllegalArgumentException("Required parameter missing: 'code'");
    }
  }
}
