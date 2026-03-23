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

import java.util.Optional;
import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;

public class ActionRuleSent implements ActionRule {

  private final boolean sent;

  public ActionRuleSent(boolean sent) {
    this.sent = sent;
  }

  @Override
  public boolean evaluate(
      Optional<Certificate> certificate, Optional<ActionEvaluation> actionEvaluation) {
    return certificate.filter(value -> sent == (value.sent() != null)).isPresent();
  }

  @Override
  public String getReasonForPermissionDenied() {
    return "För att genomföra den begärda åtgärden behöver intyget vara skickat till en intygsmottagare.";
  }
}
