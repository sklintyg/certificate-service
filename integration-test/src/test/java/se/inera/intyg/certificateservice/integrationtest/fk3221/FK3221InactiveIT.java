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
package se.inera.intyg.certificateservice.integrationtest.fk3221;

import static se.inera.intyg.certificateservice.integrationtest.fk3221.FK3221TestSetup.fk3221TestSetup;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseTestabilityUtilities;
import se.inera.intyg.certificateservice.integrationtest.common.setup.InActiveCertificatesIT;
import se.inera.intyg.certificateservice.integrationtest.common.setup.TestabilityUtilities;
import se.inera.intyg.certificateservice.integrationtest.common.tests.InactiveTypeIT;

class FK3221InactiveIT extends InActiveCertificatesIT {

  public static final String TYPE = FK3221TestSetup.TYPE;

  @BeforeEach
  void setUp() {
    super.setUpBaseIT();

    baseTestabilityUtilities =
        fk3221TestSetup()
            .testabilityUtilities(
                TestabilityUtilities.builder()
                    .api(api)
                    .internalApi(internalApi)
                    .testabilityApi(testabilityApi)
                    .build())
            .build();
  }

  @AfterEach
  void tearDown() {
    super.tearDownBaseIT();
  }

  @Nested
  @DisplayName(TYPE + "Inaktivt intyg")
  class InactiveType extends InactiveTypeIT {

    @Override
    protected BaseTestabilityUtilities testabilityUtilities() {
      return baseTestabilityUtilities;
    }
  }
}
