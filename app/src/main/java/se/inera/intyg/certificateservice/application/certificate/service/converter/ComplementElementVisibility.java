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

import java.util.Map;
import se.inera.intyg.certificateservice.application.certificate.dto.CertificateDataElement;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementVisibilityConfiguration;

/**
 * Interface for handling visibility of "complement" elements.
 *
 * <p>A complement element is an element that should be included in the certificate data when the
 * visibility rules (described by an {@link ElementVisibilityConfiguration}) indicate it is relevant
 * even though the original/parent element may not be visible.
 */
public interface ComplementElementVisibility {

  void handle(
      Map<String, CertificateDataElement> dataElementMap,
      ElementVisibilityConfiguration visibilityConfiguration);

  boolean supports(ElementVisibilityConfiguration elementVisibilityConfiguration);
}
