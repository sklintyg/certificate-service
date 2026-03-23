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

import java.time.LocalDate;
import java.time.Period;

public class TestDataElementDataConstants {

  public static final String DATE_ELEMENT_ID = "dateElementId";
  public static final String DATE_ELEMENT_NAME = "dateElementName";
  public static final String DATE_ELEMENT_VALUE_ID = "dateElementValueId";
  public static final LocalDate DATE_ELEMENT_VALUE_DATE = LocalDate.now();
  public static final Period DATE_ELEMENT_CONFIGURATION_MIN = Period.ofDays(0);
  public static final Period DATE_ELEMENT_CONFIGURATION_MAX = Period.ofMonths(1);
  public static final String DATE_ELEMENT_RULE_EXPRESSION = "$%s".formatted(DATE_ELEMENT_VALUE_ID);

  public static final String CATEGORY_ELEMENT_ID = "categoryElementId";
  public static final String CATEGORY_ELEMENT_NAME = "categoryElementName";
  public static final String UNIT_ELEMENT_ID = "UNIT_CONTACT_INFORMATION";

  public static final String TEXT_ELEMENT_ID = "textElementId";
  public static final String TEXT_ELEMENT_FIELD_ID = "textElementFieldId";
  public static final String TEXT_ELEMENT_VALUE_DATE = "textValue";

  public static final String BOOLEAN_ELEMENT_ID = "booleanElementId";
  public static final String BOOLEAN_ELEMENT_FIELD_ID = "booleanElementFieldId";
  public static final Boolean BOOLEAN_ELEMENT_VALUE_DATE = false;

  public static final String MESSAGE_ELEMENT_ID = "messageElementId";
  public static final String MESSAGE_ELEMENT_NAME = "messageElementName";

  public static final String ISSUING_UNIT_ELEMENT_ID = "issuingUnitElementId";
}
