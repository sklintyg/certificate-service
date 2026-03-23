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
import se.inera.intyg.certificateservice.application.certificate.dto.ValidateCertificateResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.ValidationErrorDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.config.ValidateCertificateRequest;
import se.inera.intyg.certificateservice.application.certificate.service.converter.ElementCertificateConverter;
import se.inera.intyg.certificateservice.application.certificate.service.validation.ValidateCertificateRequestValidator;
import se.inera.intyg.certificateservice.application.common.ActionEvaluationFactory;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.service.ValidateCertificateDomainService;
import se.inera.intyg.certificateservice.domain.validation.model.ValidationError;

@Service
@RequiredArgsConstructor
public class ValidateCertificateService {

  private final ValidateCertificateRequestValidator validateCertificateRequestValidator;
  private final ActionEvaluationFactory actionEvaluationFactory;
  private final ElementCertificateConverter elementCertificateConverter;
  private final ValidateCertificateDomainService validateCertificateDomainService;

  public ValidateCertificateResponse validate(
      ValidateCertificateRequest validateCertificateRequest, String certificateId) {
    validateCertificateRequestValidator.validate(validateCertificateRequest, certificateId);

    final var actionEvaluation =
        actionEvaluationFactory.create(
            validateCertificateRequest.getPatient(),
            validateCertificateRequest.getUser(),
            validateCertificateRequest.getUnit(),
            validateCertificateRequest.getCareUnit(),
            validateCertificateRequest.getCareProvider());

    final var elementData =
        elementCertificateConverter.convert(validateCertificateRequest.getCertificate());

    final var validationResult =
        validateCertificateDomainService.validate(
            new CertificateId(certificateId), elementData, actionEvaluation);

    return ValidateCertificateResponse.builder()
        .validationErrors(
            validationResult.errors().stream().map(this::convertValidationErrors).toList())
        .build();
  }

  private ValidationErrorDTO convertValidationErrors(ValidationError validationError) {
    return ValidationErrorDTO.builder()
        .id(validationError.elementId().id())
        .category(validationError.categoryId().id())
        .field(validationError.fieldId().value())
        .text(validationError.message().value())
        .build();
  }
}
