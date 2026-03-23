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
package se.inera.intyg.certificateservice.application.certificate.dto.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.MessageLevel;

class MessageLevelTest {

  @Test
  void shallConvertInfo() {
    assertEquals(
        se.inera.intyg.certificateservice.application.certificate.dto.config.MessageLevel.INFO,
        se.inera.intyg.certificateservice.application.certificate.dto.config.MessageLevel
            .toMessageLevel(MessageLevel.INFO));
  }

  @Test
  void shallConvertObserve() {
    assertEquals(
        se.inera.intyg.certificateservice.application.certificate.dto.config.MessageLevel.OBSERVE,
        se.inera.intyg.certificateservice.application.certificate.dto.config.MessageLevel
            .toMessageLevel(MessageLevel.OBSERVE));
  }

  @Test
  void shallConvertError() {
    assertEquals(
        se.inera.intyg.certificateservice.application.certificate.dto.config.MessageLevel.ERROR,
        se.inera.intyg.certificateservice.application.certificate.dto.config.MessageLevel
            .toMessageLevel(MessageLevel.ERROR));
  }
}
