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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateservice.application.certificate.dto.DisposeObsoleteDraftsRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.DisposeObsoleteDraftsResponse;
import se.inera.intyg.certificateservice.application.certificate.dto.ListObsoleteDraftsRequest;
import se.inera.intyg.certificateservice.application.certificate.dto.ListObsoleteDraftsResponse;
import se.inera.intyg.certificateservice.application.certificate.service.DisposeObsoleteDraftsInternalService;

@ExtendWith(MockitoExtension.class)
class DraftInternalApiControllerTest {

  @Mock private DisposeObsoleteDraftsInternalService disposeObsoleteDraftsInternalService;

  @InjectMocks private DraftInternalApiController draftInternalApiController;

  @Test
  void shouldReturnListDraftsResponse() {
    final var expectedResult = ListObsoleteDraftsResponse.builder().build();
    final var request = ListObsoleteDraftsRequest.builder().build();
    doReturn(expectedResult).when(disposeObsoleteDraftsInternalService).list(request);

    final var actualResult = draftInternalApiController.listObsoleteDrafts(request);
    assertEquals(expectedResult, actualResult);
  }

  @Test
  void shouldReturnDeleteDraftsResponse() {
    final var expectedResult = DisposeObsoleteDraftsResponse.builder().build();
    final var request = DisposeObsoleteDraftsRequest.builder().build();
    doReturn(expectedResult).when(disposeObsoleteDraftsInternalService).delete(request);

    final var actualResult = draftInternalApiController.deleteDrafts(request);
    assertEquals(expectedResult, actualResult);
  }
}
