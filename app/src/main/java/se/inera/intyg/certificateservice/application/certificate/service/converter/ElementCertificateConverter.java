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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDTO;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDataElement;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;

@Component
@RequiredArgsConstructor
public class ElementCertificateConverter {

  private final ElementDataConverter elementDataConverter;
  private final ElementMetaDataConverter elementMetaDataConverter;

  public List<ElementData> convert(CertificateDTO certificateDTO) {
    return Stream.concat(
            certificateDTO.getData().entrySet().stream()
                .filter(removeElementsMissingValue())
                .map(entry -> elementDataConverter.convert(entry.getKey(), entry.getValue())),
            elementMetaDataConverter.convert(certificateDTO.getMetadata()).stream())
        .toList();
  }

  private static Predicate<Entry<String, CertificateDataElement>> removeElementsMissingValue() {
    return entry -> entry.getValue().getValue() != null;
  }
}
