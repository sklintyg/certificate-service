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
package se.inera.intyg.certificateservice.domain.certificate.model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public record Xml(String xml) {

  public String base64() {
    return Base64.getEncoder().encodeToString(xml.getBytes(StandardCharsets.UTF_8));
  }

  public String decode() {
    if (xml == null || xml.isEmpty()) {
      throw new IllegalArgumentException("XML content cannot be null or empty");
    }
    return new String(Base64.getDecoder().decode(xml), StandardCharsets.UTF_8);
  }
}
