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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.Appearance;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FontStyle;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.OverlayText;

class CustomTextDTOTest {

  @Test
  void shallMapAllFieldsFromOverlayText() {
    final var overlayText =
        OverlayText.builder()
            .value("Digitally signed")
            .x(173)
            .y(523)
            .appearance(new Appearance(8f, FontStyle.BOLD))
            .pageIndex(0)
            .tagIndex(15)
            .build();

    final var dto = CustomTextDTO.toDTO(overlayText);

    assertAll(
        () -> assertEquals("Digitally signed", dto.value()),
        () -> assertEquals(173f, dto.x()),
        () -> assertEquals(523f, dto.y()),
        () -> assertEquals(new AppearanceDTO(8f, FontStyleEnumDTO.BOLD), dto.appearance()),
        () -> assertEquals(0, dto.pageIndex()),
        () -> assertEquals(15, dto.tagIndex()));
  }

  @Test
  void shallSetNullStyleWhenAppearanceStyleIsNull() {
    final var overlayText =
        OverlayText.builder()
            .value("Sent text")
            .x(40)
            .y(685)
            .appearance(new Appearance(22f, null))
            .pageIndex(0)
            .build();

    final var dto = CustomTextDTO.toDTO(overlayText);

    assertEquals(new AppearanceDTO(22f, null), dto.appearance());
  }

  @Test
  void shallMapNullTagIndexWhenOverlayTextHasNoTagIndex() {
    final var overlayText =
        OverlayText.builder()
            .value("Sent text")
            .x(40)
            .y(685)
            .appearance(new Appearance(22f, null))
            .pageIndex(0)
            .build();

    final var dto = CustomTextDTO.toDTO(overlayText);

    assertNull(dto.tagIndex());
  }
}
