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

public class TestDataSubUnitConstants {

  public static final Inactive INACTIVE_TRUE = new Inactive(true);
  public static final Inactive INACTIVE_FALSE = new Inactive(false);

  public static final String ALFA_ALLERGIMOTTAGNINGEN_ID = "TSTNMT2321000156-ALAM";
  public static final String ALFA_ALLERGIMOTTAGNINGEN_NAME = "Alfa Allergimottagningen";
  public static final String ALFA_ALLERGIMOTTAGNINGEN_EMAIL = "AlfaAM@webcert.invalid.se";
  public static final String ALFA_ALLERGIMOTTAGNINGEN_PHONENUMBER = "0101234567890";
  public static final String ALFA_ALLERGIMOTTAGNINGEN_ADDRESS = "Storgatan 1";
  public static final String ALFA_ALLERGIMOTTAGNINGEN_ZIP_CODE = "12345";
  public static final String ALFA_ALLERGIMOTTAGNINGEN_CITY = "Småmåla";
  public static final String ALFA_ALLERGIMOTTAGNINGEN_WORKPLACE_CODE = "1627";
  public static final Inactive ALFA_ALLERGIMOTTAGNINGEN_INACTIVE = INACTIVE_FALSE;

  public static final String ALFA_HUDMOTTAGNINGEN_ID = "TSTNMT2321000156-ALHM";
  public static final String ALFA_HUDMOTTAGNINGEN_NAME = "Alfa Hudmottagningen";
  public static final String ALFA_HUDMOTTAGNINGEN_EMAIL = "AlfaHM@webcert.invalid.se";
  public static final String ALFA_HUDMOTTAGNINGEN_PHONENUMBER = "0101234567890";
  public static final String ALFA_HUDMOTTAGNINGEN_ADDRESS = "Storgatan 1";
  public static final String ALFA_HUDMOTTAGNINGEN_ZIP_CODE = "12345";
  public static final String ALFA_HUDMOTTAGNINGEN_CITY = "Småmåla";
  public static final String ALFA_HUDMOTTAGNINGEN_WORKPLACE_CODE = "1627";
  public static final Inactive ALFA_HUDMOTTAGNINGEN_INACTIVE = INACTIVE_FALSE;

  public static final String BETA_HUDMOTTAGNINGEN_ID = "TSTNMT2321000156-BEHD";
  public static final String BETA_HUDMOTTAGNINGEN_NAME = "Beta Hudmottagningen";
  public static final String BETA_HUDMOTTAGNINGEN_EMAIL = "BetaHM@webcert.invalid.se";
  public static final String BETA_HUDMOTTAGNINGEN_PHONENUMBER = "0101234567890";
  public static final String BETA_HUDMOTTAGNINGEN_ADDRESS = "Storgatan 1";
  public static final String BETA_HUDMOTTAGNINGEN_ZIP_CODE = "12345";
  public static final String BETA_HUDMOTTAGNINGEN_CITY = "Småmåla";
  public static final String BETA_HUDMOTTAGNINGEN_WORKPLACE_CODE = "1627";
  public static final Inactive BETA_HUDMOTTAGNINGEN_INACTIVE = INACTIVE_FALSE;
}
