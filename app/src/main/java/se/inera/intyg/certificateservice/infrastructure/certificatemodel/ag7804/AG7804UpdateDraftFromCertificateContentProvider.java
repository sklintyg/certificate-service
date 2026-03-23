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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.ag7804;

import se.inera.intyg.certificateservice.domain.certificate.model.Certificate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.CertificateActionContentProvider;

public class AG7804UpdateDraftFromCertificateContentProvider
    implements CertificateActionContentProvider {

  private static final String NAME = "Hjälp med ifyllnad?";
  private static final String BODY =
      """
      <p>Det finns ett Läkarintyg för sjukpenning för denna patient som är utfärdat <span class='iu-fw-bold'>%s</span> på en enhet som du har åtkomst till. Vill du kopiera de svar som givits i det intyget till detta intygsutkast?</p>
      """;

  @Override
  public String body(Certificate certificate) {
    if (certificate == null) {
      return null;
    }

    return certificate
        .candidateForUpdate()
        .map(cert -> BODY.formatted(cert.signed().toLocalDate()))
        .orElse(null);
  }

  @Override
  public String name(Certificate certificate) {
    return NAME;
  }
}
