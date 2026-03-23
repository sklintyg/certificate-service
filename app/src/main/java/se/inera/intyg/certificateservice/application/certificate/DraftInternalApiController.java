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
package se.inera.intyg.certificateservice.application.certificate;

import static se.inera.intyg.certificateservice.logging.MdcLogConstants.EVENT_TYPE_ACCESSED;
import static se.inera.intyg.certificateservice.logging.MdcLogConstants.EVENT_TYPE_DELETION;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.certificateservice.application.certificate.dto.DisposeObsoleteDraftsRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.DisposeObsoleteDraftsResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.ListObsoleteDraftsRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.ListObsoleteDraftsResponse;
import se.inera.intyg.certificateservice.application.certificate.service.DisposeObsoleteDraftsInternalService;
import se.inera.intyg.certificateservice.logging.PerformanceLogging;

@RequiredArgsConstructor
@RestController
@RequestMapping("/internalapi/draft")
public class DraftInternalApiController {

  private final DisposeObsoleteDraftsInternalService disposeObsoleteDraftsInternalService;

  @PostMapping("/list")
  @PerformanceLogging(
      eventAction = "internal-list-obsolete-drafts",
      eventType = EVENT_TYPE_ACCESSED)
  ListObsoleteDraftsResponse listObsoleteDrafts(@RequestBody ListObsoleteDraftsRequest request) {
    return disposeObsoleteDraftsInternalService.list(request);
  }

  @DeleteMapping
  @PerformanceLogging(
      eventAction = "internal-dispose-obsolete-drafts",
      eventType = EVENT_TYPE_DELETION)
  DisposeObsoleteDraftsResponse deleteDrafts(@RequestBody DisposeObsoleteDraftsRequest request) {
    return disposeObsoleteDraftsInternalService.delete(request);
  }
}
