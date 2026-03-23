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

import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import se.inera.intyg.certificateservice.domain.certificate.model.CertificateId;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementData;
import se.inera.intyg.certificateservice.domain.certificate.model.Xml;
import se.inera.intyg.certificateservice.domain.certificate.service.PrefillProcessor;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateModel;

@Slf4j
@RequiredArgsConstructor
public class PrefillService implements PrefillProcessor {

  private final PrefillHandler prefillHandler;

  @Override
  public Set<ElementData> prefill(
      CertificateModel certificateModel, Xml prefillXml, CertificateId id) {
    if (prefillXml == null) {
      return Collections.emptySet();
    }

    final var unmarshalledPrefill = PrefillUnmarshaller.forifyllnadType(prefillXml.decode());
    if (unmarshalledPrefill.isEmpty()) {
      return Collections.emptySet();
    }

    final var prefillResult =
        PrefillResult.create(certificateModel, unmarshalledPrefill.get(), prefillHandler);

    log.info("Begin prefilling answers for certificate {}", id.id());
    prefillResult.prefill();

    log.info("Prefill result log: {}", prefillResult.toJsonReport());
    return prefillResult.prefilledElements();
  }
}
