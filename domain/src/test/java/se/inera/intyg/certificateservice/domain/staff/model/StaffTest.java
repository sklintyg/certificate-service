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
package se.inera.intyg.certificateservice.domain.staff.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.testdata.TestDataStaff;
import se.inera.intyg.certificateservice.domain.testdata.TestDataUser;

class StaffTest {

  @Test
  void shallReturnStaffWhenCreatingFromUserAjla() {
    assertEquals(TestDataStaff.AJLA_DOKTOR, Staff.create(TestDataUser.AJLA_DOKTOR));
  }

  @Test
  void shallReturnStaffWhenCreatingFromUserAlf() {
    assertEquals(TestDataStaff.ALF_DOKTOR, Staff.create(TestDataUser.ALF_DOKTOR));
  }

  @Test
  void shallReturnStaffWhenCreatingFromUserAlva() {
    assertEquals(
        TestDataStaff.ALVA_VARDADMINISTRATOR, Staff.create(TestDataUser.ALVA_VARDADMINISTRATOR));
  }

  @Test
  void shallReturnStaffWhenCreatingFromUserAnna() {
    assertEquals(TestDataStaff.ANNA_SJUKSKOTERSKA, Staff.create(TestDataUser.ANNA_SJUKSKOTERKSA));
  }

  @Test
  void shallReturnStaffWhenCreatingFromUserBertil() {
    assertEquals(TestDataStaff.BERTIL_BARNMORSKA, Staff.create(TestDataUser.BERTIL_BARNMORSKA));
  }
}
