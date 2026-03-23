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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.common;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Objects;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlNamespaceTrimmer {

  private static final String XSLT_PATH = "xslt/trim-unused-namespaces.xslt";
  private static final Templates TEMPLATES;

  static {
    try (final var xsltInputStream =
        Objects.requireNonNull(
            XmlNamespaceTrimmer.class.getClassLoader().getResourceAsStream(XSLT_PATH))) {
      final var factory = TransformerFactory.newDefaultInstance();
      TEMPLATES = factory.newTemplates(new StreamSource(xsltInputStream));
    } catch (Exception e) {
      throw new IllegalStateException("Failed to initialize Transformer Templates", e);
    }
  }

  public static String trim(String xml) {
    try {
      final var transformer = TEMPLATES.newTransformer();
      try (final var writer = new StringWriter()) {
        final var streamSource = new StreamSource(new StringReader(xml));
        transformer.transform(streamSource, new StreamResult(writer));
        return writer.toString();
      }
    } catch (Exception e) {
      log.warn("Failure trimming unused namespaces from XML", e);
      return xml;
    }
  }
}
