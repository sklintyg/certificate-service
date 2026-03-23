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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.prefill;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.extern.slf4j.Slf4j;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.CVType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.DatePeriodType;
import se.riv.clinicalprocess.healthcond.certificate.v33.Forifyllnad;

@Slf4j
public final class PrefillUnmarshaller {

  private PrefillUnmarshaller() {}

  public static <T> Optional<T> unmarshalType(List<Object> content, Class<T> clazz) {
    final var contentObj =
        content.stream()
            .filter(obj -> obj instanceof org.w3c.dom.Element)
            .map(obj -> (org.w3c.dom.Element) obj)
            .findFirst();

    if (contentObj.isPresent()) {
      try {
        final var context = JAXBContext.newInstance(clazz);
        final var jaxbElement = context.createUnmarshaller().unmarshal(contentObj.get(), clazz);
        return Optional.of(jaxbElement.getValue());
      } catch (Exception e) {
        throw new IllegalStateException("Failed to unmarshal " + clazz.getSimpleName(), e);
      }
    }
    return Optional.empty();
  }

  public static <T> Optional<T> unmarshalXml(String xml, Class<T> clazz) {
    try {
      final var context = JAXBContext.newInstance(clazz);
      final var unmarshaller = context.createUnmarshaller();
      final var stringReader = new StringReader(xml);
      final var jaxbElement = (JAXBElement<T>) unmarshaller.unmarshal(stringReader);
      return Optional.of(jaxbElement.getValue());
    } catch (Exception ex) {
      log.warn("Failed to unmarshal {}", clazz.getSimpleName(), ex);
      return Optional.empty();
    }
  }

  public static Optional<CVType> cvType(List<Object> content) {
    return unmarshalType(content, CVType.class);
  }

  public static Optional<DatePeriodType> datePeriodType(List<Object> content) {
    return unmarshalType(content, DatePeriodType.class);
  }

  public static Optional<Forifyllnad> forifyllnadType(String xml) {
    return unmarshalXml(xml, Forifyllnad.class);
  }

  public static LocalDate toLocalDate(XMLGregorianCalendar xmlGregorianCalendar) {
    return xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDate();
  }
}
