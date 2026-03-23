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
package se.inera.intyg.certificateservice.infrastructure.certificatemodel.fk7804;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class FK7804CertificateCreateDraftFromCertificateContentProviderTest {

  FK7804CertificateCreateDraftFromCertificateContentProvider provider =
      new FK7804CertificateCreateDraftFromCertificateContentProvider();

  @Test
  void shouldReturnBody() {
    assertEquals(
        """
        <div><div class="ic-alert ic-alert--status ic-alert--info">
        <i class="ic-alert__icon ic-info-icon"></i>
        Kom ihåg att stämma av med patienten om hen vill att du skickar Läkarintyget för sjukpenning till Försäkringskassan. Gör detta i så fall först.</div>
        <p class='iu-pt-400'>Skapa ett Läkarintyg om arbetsförmåga - arbetsgivaren (AG7804) utifrån ett Läkarintyg för sjukpenning innebär att informationsmängder som är gemensamma för båda intygen automatiskt förifylls.
        </p></div>
        """,
        provider.body(null));
  }

  @Test
  void shouldReturnDescription() {
    assertEquals(
        "Skapar ett intyg till arbetsgivaren utifrån Försäkringskassans intyg.",
        provider.description(null));
  }

  @Test
  void shouldReturnName() {
    assertEquals("Skapa AG7804", provider.name(null));
  }
}
