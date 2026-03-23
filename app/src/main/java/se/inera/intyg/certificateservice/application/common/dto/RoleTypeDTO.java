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

import se.inera.intyg.certificateservice.domain.common.model.Role;

public enum RoleTypeDTO {
  DOCTOR,
  PRIVATE_DOCTOR,
  NURSE,
  MIDWIFE,
  CARE_ADMIN,
  DENTIST;

  public Role toRole() {
    return switch (this) {
      case DOCTOR -> Role.DOCTOR;
      case NURSE -> Role.NURSE;
      case MIDWIFE -> Role.MIDWIFE;
      case CARE_ADMIN -> Role.CARE_ADMIN;
      case DENTIST -> Role.DENTIST;
      case PRIVATE_DOCTOR -> Role.PRIVATE_DOCTOR;
    };
  }

  public static RoleTypeDTO toRoleType(Role role) {
    return switch (role) {
      case DOCTOR -> DOCTOR;
      case NURSE -> NURSE;
      case MIDWIFE -> MIDWIFE;
      case CARE_ADMIN -> CARE_ADMIN;
      case DENTIST -> DENTIST;
      case PRIVATE_DOCTOR -> PRIVATE_DOCTOR;
    };
  }
}
