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
package se.inera.intyg.certificateservice.infrastructure.clinicalprocesscertificatev4.validation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.common.xmlschema.LSInputImpl;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

@Slf4j
public class SchemaResourceResolverV4 implements LSResourceResolver {

  private static final String CORE_COMPONENTS_XSD_PATH = "schemas/certificate/4.0/core_components/";

  @Override
  public LSInput resolveResource(
      String type, String namespaceURI, String publicId, String systemId, String baseURI) {
    try {
      final var input = new LSInputImpl();
      final var schemaPath = CORE_COMPONENTS_XSD_PATH + systemId.replaceAll("(\\.\\./){2}.*/", "");
      final var fileReader = getFileStreamSource(schemaPath);
      input.setPublicId(publicId);
      input.setSystemId(systemId);
      input.setBaseURI(baseURI);
      input.setCharacterStream(fileReader);
      return input;

    } catch (IOException e) {
      log.error("Failure reading xsd resource {}", systemId, e);
      return null;
    }
  }

  private InputStreamReader getFileStreamSource(String path) throws IOException {
    final var classLoader = getClass().getClassLoader();
    try (final var inputStream = classLoader.getResourceAsStream(path)) {
      return new InputStreamReader(
          new ByteArrayInputStream(Objects.requireNonNull(inputStream).readAllBytes()));
    }
  }
}
