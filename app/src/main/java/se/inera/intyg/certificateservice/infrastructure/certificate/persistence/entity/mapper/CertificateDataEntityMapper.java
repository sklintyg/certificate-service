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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.elementdata.ElementDataMapper;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.elementdata.MappedElementData;
import se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity.CertificateDataEntity;

@Component
@RequiredArgsConstructor
public class CertificateDataEntityMapper {

  private final ElementDataMapper elementDataMapper;

  public CertificateDataEntity toEntity(List<ElementData> elements) {
    final var mappedElements = elements.stream().map(elementDataMapper::toMapped).toList();

    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

      objectMapper().writeValue(out, mappedElements);

      return CertificateDataEntity.builder().data(out.toByteArray()).build();
    } catch (IOException e) {
      throw new IllegalStateException("Error when processing element data to JSON", e);
    }
  }

  public List<ElementData> toDomain(CertificateDataEntity entity) {
    try {
      final var elements =
          objectMapper()
              .readValue(entity.getData(), new TypeReference<List<MappedElementData>>() {});

      return elements.stream().map(elementDataMapper::toDomain).toList();
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("Error when processing json to ElementData", e);
    }
  }

  private static JsonMapper objectMapper() {
    return JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build();
  }
}
