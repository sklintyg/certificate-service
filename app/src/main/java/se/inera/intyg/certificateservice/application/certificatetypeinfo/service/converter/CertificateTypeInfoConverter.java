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
package se.inera.intyg.certificateservice.application.certificatetypeinfo.service.converter;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.service.converter.CertificateConfirmationModalConverter;
import se.inera.intyg.certificateservice.application.certificatetypeinfo.dto.CertificateTypeInfoDTO;
import se.inera.intyg.certificateservice.application.common.converter.ResourceLinkConverter;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateAction;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;

@Component
@RequiredArgsConstructor
public class CertificateTypeInfoConverter {

  private final ResourceLinkConverter resourceLinkConverter;
  private final CertificateConfirmationModalConverter confirmationModalConverter;

  public CertificateTypeInfoDTO convert(
      CertificateModel certificateModel,
      List<CertificateAction> certificateActions,
      ActionEvaluation actionEvaluation) {
    return CertificateTypeInfoDTO.builder()
        .type(certificateModel.id().type().type())
        .typeName(certificateModel.typeName().name())
        .name(certificateModel.name())
        .description(certificateModel.description())
        .links(
            certificateActions.stream()
                .map(
                    certificateAction ->
                        resourceLinkConverter.convert(
                            certificateAction, Optional.empty(), actionEvaluation))
                .toList())
        .confirmationModal(
            certificateModel.confirmationModalProvider() != null
                ? confirmationModalConverter.convert(
                    certificateModel.confirmationModalProvider().of(null, actionEvaluation))
                : null)
        .build();
  }
}
