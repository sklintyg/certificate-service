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
import jakarta.persistence.Version;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

@Entity
@Table(name = "staff")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "`key`")
  private int key;

  @Column(name = "hsa_id", unique = true)
  private String hsaId;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "middle_name")
  private String middleName;

  @Column(name = "last_name")
  private String lastName;

  @ManyToOne
  @JoinColumn(name = "staff_role_key")
  private StaffRoleEntity role;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "staff_pa_title", joinColumns = @JoinColumn(name = "staff_key"))
  private List<PaTitleEmbeddable> paTitles;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "staff_speciality", joinColumns = @JoinColumn(name = "staff_key"))
  private List<SpecialityEmbeddable> specialities;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "staff_healthcare_professional_licence",
      joinColumns = @JoinColumn(name = "staff_key"))
  private List<HealthcareProfessionalLicenceEmbeddable> healthcareProfessionalLicences;

  @Version private Long version;

  public void updateWith(StaffEntity source) {
    if (source == null) {
      throw new IllegalArgumentException("Cannot update StaffEntity with null");
    }
    this.setFirstName(source.getFirstName());
    this.setMiddleName(source.getMiddleName());
    this.setLastName(source.getLastName());
    this.setRole(source.getRole());
    this.setPaTitles(source.getPaTitles());
    this.setSpecialities(source.getSpecialities());
    this.setHealthcareProfessionalLicences(source.getHealthcareProfessionalLicences());
  }

  public boolean hasDiff(StaffEntity other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot compare StaffEntity with null");
    }

    if (!Objects.equals(this.hsaId.toUpperCase(), other.getHsaId().toUpperCase())) {
      throw new IllegalArgumentException("Cannot compare StaffEntity with different hsaId");
    }

    return !(Objects.equals(this.firstName, other.getFirstName())
        && Objects.equals(this.middleName, other.getMiddleName())
        && Objects.equals(this.lastName, other.getLastName())
        && Objects.equals(this.role, other.getRole())
        && CollectionUtils.isEqualCollection(this.paTitles, other.getPaTitles())
        && CollectionUtils.isEqualCollection(this.specialities, other.getSpecialities())
        && CollectionUtils.isEqualCollection(
            this.healthcareProfessionalLicences, other.getHealthcareProfessionalLicences()));
  }
}
