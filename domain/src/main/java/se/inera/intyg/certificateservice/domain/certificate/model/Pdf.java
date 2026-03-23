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

import java.util.Arrays;
import java.util.Objects;

public record Pdf(byte[] pdfData, String fileName) {

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pdf pdf = (Pdf) o;
    return Arrays.equals(pdfData, pdf.pdfData) && Objects.equals(fileName, pdf.fileName);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(fileName);
    result = 31 * result + Arrays.hashCode(pdfData);
    return result;
  }

  @Override
  public String toString() {
    return "Pdf{" + "pdfData=" + Arrays.toString(pdfData) + ", fileName='" + fileName + '\'' + '}';
  }
}
