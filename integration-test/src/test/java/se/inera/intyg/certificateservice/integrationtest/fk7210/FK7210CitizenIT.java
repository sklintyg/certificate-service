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
package se.inera.intyg.certificateservice.integrationtest.fk7210;

import static se.inera.intyg.certificateservice.integrationtest.fk7210.FK7210TestSetup.fk7210TestSetup;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import se.inera.intyg.certificateservice.integrationtest.common.setup.ActiveCertificatesIT;
import se.inera.intyg.certificateservice.integrationtest.common.setup.BaseTestabilityUtilities;
import se.inera.intyg.certificateservice.integrationtest.common.setup.TestabilityUtilities;
import se.inera.intyg.certificateservice.integrationtest.common.tests.ExistsCitizenCertificateIT;
import se.inera.intyg.certificateservice.integrationtest.common.tests.GetCitizenCertificateIT;
import se.inera.intyg.certificateservice.integrationtest.common.tests.GetCitizenCertificateListIT;
import se.inera.intyg.certificateservice.integrationtest.common.tests.PrintCitizenCertificateIT;

class FK7210CitizenIT extends ActiveCertificatesIT {

  public static final String TYPE = FK7210TestSetup.TYPE;

  @BeforeEach
  void setUp() {
    super.setUpBaseIT();

    baseTestabilityUtilities =
        fk7210TestSetup()
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
  @DisplayName(TYPE + "Hämta intyg för invånare")
  class GetCitizenCertificate extends GetCitizenCertificateIT {

    @Override
    protected BaseTestabilityUtilities testabilityUtilities() {
      return baseTestabilityUtilities;
    }
  }

  @Nested
  @DisplayName(TYPE + "Hämta intygslista för invånare")
  class GetCitizenCertificateList extends GetCitizenCertificateListIT {

    @Override
    protected BaseTestabilityUtilities testabilityUtilities() {
      return baseTestabilityUtilities;
    }
  }

  @Nested
  @DisplayName(TYPE + "Skriv ut intyg för invånare")
  class PrintCitizenCertificate extends PrintCitizenCertificateIT {

    @Override
    protected BaseTestabilityUtilities testabilityUtilities() {
      return baseTestabilityUtilities;
    }
  }

  @Nested
  @DisplayName(TYPE + "Finns intyg för invånare")
  class ExistsCitizenCertificate extends ExistsCitizenCertificateIT {

    @Override
    protected BaseTestabilityUtilities testabilityUtilities() {
      return baseTestabilityUtilities;
    }
  }
}
