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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueCode;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueText;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueConverterCodeTest {

  private static final String ID = "CODE_ID";
  private static final String CODE = "CODE";
  private static final CertificateDataValueCode CERTIFICATE_DATA_VALUE_CODE =
      CertificateDataValueCode.builder().id(ID).code(CODE).build();

  private static final FieldId CODE_ID = new FieldId(ID);
  private ElementValueConverterCode elementValueConverterCode;

  @BeforeEach
  void setUp() {
    elementValueConverterCode = new ElementValueConverterCode();
  }

  @Test
  void shallThrowIfTypeIsNotCertificateDataValueCode() {
    final var certificateDataTextValue = CertificateDataValueText.builder().build();
    final var illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () -> elementValueConverterCode.convert(certificateDataTextValue));
    assertTrue(illegalStateException.getMessage().contains("Invalid value type"));
  }

  @Test
  void shallReturnTypeDate() {
    assertEquals(CertificateDataValueType.CODE, elementValueConverterCode.getType());
  }

  @Test
  void shallConvertId() {
    final var result = elementValueConverterCode.convert(CERTIFICATE_DATA_VALUE_CODE);
    final var actualResult = (ElementValueCode) result;
    assertEquals(CODE_ID, actualResult.codeId());
  }

  @Test
  void shallConvertCode() {
    final var result = elementValueConverterCode.convert(CERTIFICATE_DATA_VALUE_CODE);
    final var actualResult = (ElementValueCode) result;
    assertEquals(CODE, actualResult.code());
  }
}
