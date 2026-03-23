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
package se.inera.intyg.certificateservice.infrastructure.certificate.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "staff_version")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffVersionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "`key`")
  private int key;

  @Column(name = "hsa_id", nullable = false)
  private String hsaId;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "valid_from")
  private LocalDateTime validFrom;

  @Column(name = "valid_to", nullable = false)
  private LocalDateTime validTo;

  @ManyToOne
  @JoinColumn(name = "staff_role_key")
  private StaffRoleEntity role;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "staff_pa_title_version",
      joinColumns = @JoinColumn(name = "staff_version_key"))
  private List<PaTitleVersionEmbeddable> paTitles;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "staff_speciality_version",
      joinColumns = @JoinColumn(name = "staff_version_key"))
  private List<SpecialityVersionEmbeddable> specialities;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "staff_healthcare_professional_licence_version",
      joinColumns = @JoinColumn(name = "staff_version_key"))
  private List<HealthcareProfessionalLicenceVersionEmbeddable> healthcareProfessionalLicences;

  @ManyToOne
  @JoinColumn(name = "staff_key", referencedColumnName = "`key`", nullable = false)
  private StaffEntity staff;
}
