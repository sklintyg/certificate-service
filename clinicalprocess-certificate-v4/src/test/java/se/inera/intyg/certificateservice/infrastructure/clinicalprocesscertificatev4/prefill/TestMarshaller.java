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
import java.io.StringWriter;
import java.util.function.Function;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class TestMarshaller {

  public static <T> Element getElement(T object, Function<T, JAXBElement<T>> jaxbElementCreator) {
    try {
      final var jaxbElement = jaxbElementCreator.apply(object);
      final var context = JAXBContext.newInstance(object.getClass());
      final var marshaller = context.createMarshaller();
      final var writer = new StringWriter();
      marshaller.marshal(jaxbElement, writer);

      final var docFactory = DocumentBuilderFactory.newInstance();
      docFactory.setNamespaceAware(true);
      final var doc =
          docFactory
              .newDocumentBuilder()
              .parse(new InputSource(new StringReader(writer.toString())));
      return doc.getDocumentElement();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
