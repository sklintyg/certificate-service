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
package se.inera.intyg.certificateservice.application.certificate.service.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataIcfValue;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDate;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueIcf;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueConverterIcfTest {

  private static final String TEXT_VALUE = "textValue";
  private static final String ID = "textId";
  private static final List<String> VALUES = List.of("code1", "code2");
  private static final CertificateDataIcfValue CERTIFICATE_DATA_ICF_VALUE =
      CertificateDataIcfValue.builder().id(ID).text(TEXT_VALUE).icfCodes(VALUES).build();
  private static final FieldId TEXT_ID = new FieldId(ID);
  private ElementValueConverterIcf elementValueConverterIcf;

  @BeforeEach
  void setUp() {
    elementValueConverterIcf = new ElementValueConverterIcf();
  }

  @Test
  void shallThrowIfTypeIsNotCertificateDataIcfValue() {
    final var certificateDataValueDate = CertificateDataValueDate.builder().build();
    final var illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () -> elementValueConverterIcf.convert(certificateDataValueDate));
    assertTrue(illegalStateException.getMessage().contains("Invalid value type"));
  }

  @Test
  void shallReturnTypeText() {
    assertEquals(CertificateDataValueType.ICF, elementValueConverterIcf.getType());
  }

  @Test
  void shallConvertId() {
    final var result = elementValueConverterIcf.convert(CERTIFICATE_DATA_ICF_VALUE);
    final var actualResult = (ElementValueIcf) result;
    assertEquals(TEXT_ID, actualResult.id());
  }

  @Test
  void shallConvertText() {
    final var result = elementValueConverterIcf.convert(CERTIFICATE_DATA_ICF_VALUE);
    final var actualResult = (ElementValueIcf) result;
    assertEquals(TEXT_VALUE, actualResult.text());
  }

  @Test
  void shallConvertCodes() {
    final var result = elementValueConverterIcf.convert(CERTIFICATE_DATA_ICF_VALUE);
    final var actualResult = (ElementValueIcf) result;
    assertEquals(VALUES, actualResult.icfCodes());
  }
}
