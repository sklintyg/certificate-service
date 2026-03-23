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

import se.inera.intyg.certificateservice.domain.unit.model.Inactive;

public class TestDataCareUnitConstants {

  public static final Inactive INACTIVE_TRUE = new Inactive(true);
  public static final Inactive INACTIVE_FALSE = new Inactive(false);

  public static final String ALFA_MEDICINCENTRUM_ID = "TSTNMT2321000156-ALMC";
  public static final String ALFA_MEDICINCENTRUM_NAME = "Alfa Medicincentrum";
  public static final String ALFA_MEDICINCENTRUM_EMAIL = "AlfaMC@webcert.invalid.se";
  public static final String ALFA_MEDICINCENTRUM_PHONENUMBER = "0101234567890";
  public static final String ALFA_MEDICINCENTRUM_ADDRESS = "Storgatan 1";
  public static final String ALFA_MEDICINCENTRUM_ZIP_CODE = "12345";
  public static final String ALFA_MEDICINCENTRUM_CITY = "Småmåla";
  public static final String ALFA_MEDICINCENTRUM_WORKPLACE_CODE = "1627";
  public static final Inactive ALFA_MEDICINCENTRUM_INACTIVE = INACTIVE_FALSE;

  public static final String ALFA_VARDCENTRAL_ID = "TSTNMT2321000156-ALVC";
  public static final String ALFA_VARDCENTRAL_NAME = "Alfa Vårdcentral";
  public static final String ALFA_VARDCENTRAL_EMAIL = "AlfaVC@webcert.invalid.se";
  public static final String ALFA_VARDCENTRAL_PHONENUMBER = "0101234567890";
  public static final String ALFA_VARDCENTRAL_ADDRESS = "Storgatan 1";
  public static final String ALFA_VARDCENTRAL_ZIP_CODE = "12345";
  public static final String ALFA_VARDCENTRAL_CITY = "Småmåla";
  public static final String ALFA_VARDCENTRAL_WORKPLACE_CODE = "1627";
  public static final Inactive ALFA_VARDCENTRAL_INACTIVE = INACTIVE_FALSE;

  public static final String BETA_VARDCENTRAL_ID = "TSTNMT2321000156-BEVC";
  public static final String BETA_VARDCENTRAL_NAME = "Beta Vårdcentral";
  public static final String BETA_VARDCENTRAL_EMAIL = "BetaVC@webcert.invalid.se";
  public static final String BETA_VARDCENTRAL_PHONENUMBER = "0101234567890";
  public static final String BETA_VARDCENTRAL_ADDRESS = "Storgatan 1";
  public static final String BETA_VARDCENTRAL_ZIP_CODE = "12345";
  public static final String BETA_VARDCENTRAL_CITY = "Småmåla";
  public static final String BETA_VARDCENTRAL_WORKPLACE_CODE = "1627";
  public static final Inactive BETA_VARDCENTRAL_INACTIVE = INACTIVE_FALSE;
}
