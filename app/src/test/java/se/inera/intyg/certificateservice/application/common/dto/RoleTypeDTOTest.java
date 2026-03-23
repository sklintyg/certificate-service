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
package se.inera.intyg.certificateservice.application.common.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.domain.common.model.Role;

class RoleTypeDTOTest {

  @Test
  void shallReturnRoleDoctor() {
    assertEquals(Role.DOCTOR, RoleTypeDTO.DOCTOR.toRole());
  }

  @Test
  void shallReturnRoleNurse() {
    assertEquals(Role.NURSE, RoleTypeDTO.NURSE.toRole());
  }

  @Test
  void shallReturnRoleDentist() {
    assertEquals(Role.DENTIST, RoleTypeDTO.DENTIST.toRole());
  }

  @Test
  void shallReturnRoleMidwife() {
    assertEquals(Role.MIDWIFE, RoleTypeDTO.MIDWIFE.toRole());
  }

  @Test
  void shallReturnRoleCareAdmin() {
    assertEquals(Role.CARE_ADMIN, RoleTypeDTO.CARE_ADMIN.toRole());
  }

  @Test
  void shallReturnRoleTypeDTODoctor() {
    assertEquals(RoleTypeDTO.DOCTOR, RoleTypeDTO.toRoleType(Role.DOCTOR));
  }

  @Test
  void shallReturnRoleTypeDTONurse() {
    assertEquals(RoleTypeDTO.NURSE, RoleTypeDTO.toRoleType(Role.NURSE));
  }

  @Test
  void shallReturnRoleTypeDTODentist() {
    assertEquals(RoleTypeDTO.DENTIST, RoleTypeDTO.toRoleType(Role.DENTIST));
  }

  @Test
  void shallReturnRoleTypeDTOMidwife() {
    assertEquals(RoleTypeDTO.MIDWIFE, RoleTypeDTO.toRoleType(Role.MIDWIFE));
  }

  @Test
  void shallReturnRoleTypeDTOCareAdmin() {
    assertEquals(RoleTypeDTO.CARE_ADMIN, RoleTypeDTO.toRoleType(Role.CARE_ADMIN));
  }
}
