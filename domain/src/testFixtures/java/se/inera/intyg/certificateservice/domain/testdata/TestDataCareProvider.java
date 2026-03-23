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
package se.inera.intyg.certificateservice.domain.testdata;

import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProviderConstants.ALFA_REGIONEN_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProviderConstants.ALFA_REGIONEN_NAME;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProviderConstants.BETA_REGIONEN_ID;
import static se.inera.intyg.certificateservice.domain.testdata.TestDataCareProviderConstants.BETA_REGIONEN_NAME;

import se.inera.intyg.certificateservice.domain.common.model.HsaId;
import se.inera.intyg.certificateservice.domain.unit.model.CareProvider;
import se.inera.intyg.certificateservice.domain.unit.model.CareProvider.CareProviderBuilder;
import se.inera.intyg.certificateservice.domain.unit.model.UnitName;

public class TestDataCareProvider {

  public static final CareProvider ALFA_REGIONEN = alfaRegionenBuilder().build();

  public static final CareProvider BETA_REGIONEN = betaRegionenBuilder().build();

  public static CareProviderBuilder alfaRegionenBuilder() {
    return CareProvider.builder()
        .hsaId(new HsaId(ALFA_REGIONEN_ID))
        .name(new UnitName(ALFA_REGIONEN_NAME));
  }

  public static CareProviderBuilder betaRegionenBuilder() {
    return CareProvider.builder()
        .hsaId(new HsaId(BETA_REGIONEN_ID))
        .name(new UnitName(BETA_REGIONEN_NAME));
  }
}
