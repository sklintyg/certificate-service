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
package se.inera.intyg.certificateservice.domain.message.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import se.inera.intyg.certificateservice.domain.action.certificate.model.CertificateActionType;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificate.repository.CertificateRepository;
import se.inera.intyg.certificateservice.domain.common.exception.CertificateActionForbidden;
import se.inera.intyg.certificateservice.domain.message.model.Complement;
import se.inera.intyg.certificateservice.domain.message.model.Message;
import se.inera.intyg.certificateservice.domain.message.repository.MessageRepository;

@RequiredArgsConstructor
public class ReceiveComplementMessageDomainService {

  private final CertificateRepository certificateRepository;
  private final MessageRepository messageRepository;

  public Message receive(Message message) {
    final var certificate = certificateRepository.getById(message.certificateId());
    if (!certificate.allowTo(CertificateActionType.RECEIVE_COMPLEMENT, Optional.empty())) {
      throw new CertificateActionForbidden(
          "Not allowed to receive complement on certificate for %s"
              .formatted(certificate.id().id()),
          certificate.reasonNotAllowed(CertificateActionType.RECEIVE_COMPLEMENT, Optional.empty()));
    }

    if (!certificate.isCertificateIssuedOnPatient(message.personId())) {
      throw new CertificateActionForbidden(
          "Not allowed to receive complement on certificate for %s, because patient is not matching"
              .formatted(certificate.id().id()),
          List.of("Different patient on certificate and incoming message!"));
    }

    if (certificateDoesNotContainProvidedElementId(message, certificate)) {
      throw new IllegalStateException(
          "Certificate '%s' does not contain element with id: '%s' "
              .formatted(
                  certificate.id().id(),
                  message.complements().stream()
                      .map(Complement::elementId)
                      .filter(id -> !certificate.certificateModel().elementSpecificationExists(id))
                      .toList()));
    }

    return messageRepository.save(message);
  }

  private static boolean certificateDoesNotContainProvidedElementId(
      Message message, Certificate certificate) {
    return message.complements().stream()
        .noneMatch(
            complement ->
                certificate.certificateModel().elementSpecificationExists(complement.elementId()));
  }
}
