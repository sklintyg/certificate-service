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
package se.inera.intyg.certificateservice.domain.action.certificate.model;

import java.util.List;
import java.util.Optional;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;

public interface CertificateAction {

  CertificateActionType getType();

  boolean evaluate(Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation);

  List<String> reasonNotAllowed(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation);

  default String getName(Optional<Certificate> certificate) {
    return null;
  }

  default String getDescription(Optional<Certificate> certificate) {
    return null;
  }

  default String getBody(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    return null;
  }

  default boolean isEnabled(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    return true;
  }

  default boolean include(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    return evaluate(certificate, actionEvaluation);
  }
}
