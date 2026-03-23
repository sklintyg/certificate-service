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

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "certificate_xml")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateXmlEntity {

  @Id
  @Column(name = "`key`")
  private Long key;

  @OneToOne
  @MapsId
  @JoinColumn(name = "`key`")
  private CertificateEntity certificate;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(name = "data")
  private byte[] data;

  public CertificateXmlEntity(String data) {
    this.data = toBytes(data);
  }

  public void setData(String document) {
    this.data = toBytes(document);
  }

  public String getData() {
    return fromBytes(this.data);
  }

  private byte[] toBytes(String data) {
    if (data == null) {
      return new byte[0];
    }
    return data.getBytes(StandardCharsets.UTF_8);
  }

  private String fromBytes(byte[] bytes) {
    return new String(bytes, StandardCharsets.UTF_8);
  }
}
