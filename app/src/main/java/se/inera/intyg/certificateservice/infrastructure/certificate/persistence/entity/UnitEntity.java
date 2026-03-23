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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "unit")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnitEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "`key`")
  private int key;

  @Column(name = "hsa_id", unique = true)
  private String hsaId;

  @Column(name = "name")
  private String name;

  @Column(name = "address")
  private String address;

  @Column(name = "zip_code")
  private String zipCode;

  @Column(name = "city")
  private String city;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "email")
  private String email;

  @Column(name = "workplace_code")
  private String workplaceCode;

  @ManyToOne
  @JoinColumn(name = "unit_type_key")
  private UnitTypeEntity type;

  @Version private Long version;

  public void updateWith(UnitEntity source) {
    if (source == null) {
      throw new IllegalArgumentException("Cannot update UnitEntity with null");
    }
    this.setName(source.getName());
    this.setAddress(source.getAddress());
    this.setZipCode(source.getZipCode());
    this.setCity(source.getCity());
    this.setPhoneNumber(source.getPhoneNumber());
    this.setEmail(source.getEmail());
    this.setWorkplaceCode(source.getWorkplaceCode());
  }

  public boolean hasDiff(UnitEntity other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot compare UnitEntity with null");
    }

    if (!Objects.equals(this.hsaId, other.getHsaId())) {
      throw new IllegalArgumentException("Cannot compare UnitEntity with different hsaId");
    }

    return !(Objects.equals(this.name, other.getName())
        && Objects.equals(this.address, other.getAddress())
        && Objects.equals(this.zipCode, other.getZipCode())
        && Objects.equals(this.city, other.getCity())
        && Objects.equals(this.phoneNumber, other.getPhoneNumber())
        && Objects.equals(this.email, other.getEmail())
        && Objects.equals(this.workplaceCode, other.getWorkplaceCode()));
  }
}
