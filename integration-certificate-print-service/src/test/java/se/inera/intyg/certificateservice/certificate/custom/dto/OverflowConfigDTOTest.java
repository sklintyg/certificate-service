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
package se.inera.intyg.certificateservice.certificate.custom.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverflowConfig;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.PdfFieldId;

class OverflowConfigDTOTest {

  @Test
  void shallMapOverflowConfigToDTO() {
    final var overflow =
        OverflowConfig.builder()
            .overflowFieldId(new PdfFieldId("overflowField"))
            .overflowLabel("Overflow")
            .build();

    final var expected = new OverflowConfigDTO("overflowField", "Overflow");

    assertEquals(expected, OverflowConfigDTO.toDTO(overflow));
  }

  @Test
  void shallReturnNullIfOverflowConfigIsNull() {
    assertNull(OverflowConfigDTO.toDTO(null));
  }

  @Test
  void shallReturnNullIfOverflowFieldIdIsNull() {
    final var overflowConfig =
        OverflowConfig.builder().overflowFieldId(null).overflowLabel("Overflow").build();

    assertNull(OverflowConfigDTO.toDTO(overflowConfig));
  }
}
