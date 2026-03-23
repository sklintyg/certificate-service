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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDTO;
import se.inera.intyg.certificateservice.application.common.dto.ResourceLinkDTO;
import se.inera.intyg.certificateservice.domain.action.certificate.model.ActionEvaluation;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.model.RelationType;
import se.inera.intyg.certificateservice.domain.message.model.Message;

@Component
@RequiredArgsConstructor
public class CertificateConverter {

  private final CertificateDataConverter certificateDataConverter;
  private final CertificateMetadataConverter certificateMetadataConverter;
  private final HandleComplementElementVisibilityService handleComplementElementVisibilityService;

  public CertificateDTO convert(
      Certificate certificate,
      List<ResourceLinkDTO> resourceLinks,
      ActionEvaluation actionEvaluation) {
    var certificateDataElementMap = certificateDataConverter.convert(certificate, false);

    if (draftWithComplementRequestOnParent(certificate)) {
      certificate.parent().certificate().messages().stream()
          .map(Message::complements)
          .flatMap(List::stream)
          .forEach(
              complement ->
                  certificate
                      .certificateModel()
                      .elementSpecification(complement.elementId())
                      .getVisibilityConfiguration()
                      .ifPresent(
                          config ->
                              handleComplementElementVisibilityService.handle(
                                  complement.elementId(),
                                  certificateDataElementMap,
                                  certificate,
                                  config)));
    }

    return CertificateDTO.builder()
        .metadata(certificateMetadataConverter.convert(certificate, actionEvaluation))
        .data(certificateDataElementMap)
        .links(resourceLinks)
        .build();
  }

  public CertificateDTO convertForCitizen(
      Certificate certificate, List<ResourceLinkDTO> resourceLinks) {
    return CertificateDTO.builder()
        .metadata(certificateMetadataConverter.convert(certificate, null))
        .data(certificateDataConverter.convert(certificate, true))
        .links(resourceLinks)
        .build();
  }

  private static boolean draftWithComplementRequestOnParent(Certificate certificate) {
    return certificate.isDraft()
        && certificate.parent() != null
        && certificate.parent().type().equals(RelationType.COMPLEMENT);
  }
}
