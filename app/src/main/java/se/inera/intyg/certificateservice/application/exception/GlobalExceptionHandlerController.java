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
package se.inera.intyg.certificateservice.application.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.inera.intyg.certificateservice.domain.common.exception.CertificateActionForbidden;
import se.inera.intyg.certificateservice.domain.common.exception.CitizenCertificateForbidden;
import se.inera.intyg.certificateservice.domain.common.exception.ConcurrentModificationException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandlerController {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
    log.warn("Bad request", exception);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @ExceptionHandler(CertificateActionForbidden.class)
  public ResponseEntity<ApiError> handleCertificateActionForbidden(
      CertificateActionForbidden exception) {
    log.warn("Forbidden - %s".formatted(String.join(" - ", exception.reason())), exception);

    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ApiError.builder().message(String.join(" ", exception.reason())).build());
  }

  @ExceptionHandler(ConcurrentModificationException.class)
  public ResponseEntity<ApiError> handleConcurrentModificationException(
      ConcurrentModificationException exception) {
    log.warn("Conflict", exception);
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(
            ApiError.builder()
                .message(
                    String.join(
                        " ",
                        "%s på enheten %s"
                            .formatted(
                                exception.user().name().fullName(),
                                exception.unit().name().name())))
                .build());
  }

  @ExceptionHandler(CitizenCertificateForbidden.class)
  public ResponseEntity<ApiError> handleCitizenCertificateForbidden(
      CitizenCertificateForbidden exception) {
    log.warn("Forbidden", exception);

    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ApiError.builder().message(exception.getMessage()).build());
  }
}
