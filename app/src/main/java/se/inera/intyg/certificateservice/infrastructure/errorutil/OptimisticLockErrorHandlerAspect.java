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
package se.inera.intyg.certificateservice.infrastructure.errorutil;

import jakarta.persistence.OptimisticLockException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class OptimisticLockErrorHandlerAspect {

  @Around("@annotation(optimisticLockErrorHandler)")
  public Object retry(
      ProceedingJoinPoint joinPoint, OptimisticLockErrorHandler optimisticLockErrorHandler)
      throws Throwable {
    final var maxRetries = optimisticLockErrorHandler.maxRetries();

    var attempt = 0;

    while (true) {
      try {
        return joinPoint.proceed();
      } catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
        attempt++;
        if (attempt >= maxRetries) {
          throw new IllegalStateException(
              String.format(
                  "OptimisticLockException after %s retries for method: %s",
                  maxRetries, joinPoint.getSignature().getName()));
        }
        log.warn(
            "OptimisticLockException on attempt {} for method: {}. Retrying...",
            attempt,
            joinPoint.getSignature().getName());
      }
    }
  }
}
