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
package se.inera.intyg.certificateservice.domain.validation.model;

import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class CharacterValidator {

  private CharacterValidator() {
    throw new IllegalStateException("Utility class");
  }

  public static void iso88591(String text) {
    final var encoder = StandardCharsets.ISO_8859_1.newEncoder();
    final var invalidChars =
        text.chars()
            .filter(c -> !encoder.canEncode((char) c))
            .distinct()
            .mapToObj(c -> String.valueOf((char) c))
            .collect(Collectors.joining(", "));

    if (!invalidChars.isEmpty()) {
      throw new IllegalArgumentException(
          "Text contains characters not supported in ISO 8859-1: [%s]".formatted(invalidChars));
    }
  }
}
