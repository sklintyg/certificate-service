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
package se.inera.intyg.certificateservice.fhir.integration;

import ca.uhn.fhir.context.FhirContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class FhirR5HttpMessageConverter extends AbstractHttpMessageConverter<IBaseResource> {

  private static final FhirContext FHIR_CONTEXT = FhirContext.forR5();

  public FhirR5HttpMessageConverter() {
    super(MediaType.APPLICATION_JSON, new MediaType("application", "fhir+json"));
  }

  @Override
  protected boolean supports(Class<?> clazz) {
    return IBaseResource.class.isAssignableFrom(clazz);
  }

  @Override
  protected IBaseResource readInternal(Class<? extends IBaseResource> clazz,
      HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
    final var json = new String(inputMessage.getBody().readAllBytes(), StandardCharsets.UTF_8);
    return FHIR_CONTEXT.newJsonParser().parseResource(json);
  }

  @Override
  protected void writeInternal(IBaseResource resource, HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {
    final var json = FHIR_CONTEXT.newJsonParser().encodeResourceToString(resource);
    outputMessage.getBody().write(json.getBytes(StandardCharsets.UTF_8));
  }
}
