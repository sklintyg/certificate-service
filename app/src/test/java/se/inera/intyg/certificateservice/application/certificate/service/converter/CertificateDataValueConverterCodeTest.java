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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueCode;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueDate;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.ElementType;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class CertificateDataValueConverterCodeTest {

  private static final String CODE_ID = "codeId";
  private static final String CODE = "code";
  private CertificateDataValueConverterCode certificateDataValueConverterCode;

  @BeforeEach
  void setUp() {
    certificateDataValueConverterCode = new CertificateDataValueConverterCode();
  }

  @Test
  void shallReturnCorrectType() {
    assertEquals(ElementType.RADIO_MULTIPLE_CODE, certificateDataValueConverterCode.getType());
  }

  @Test
  void shallThrowIfWrongType() {
    final var elementValue = ElementValueDate.builder().build();
    assertThrows(
        IllegalStateException.class,
        () -> certificateDataValueConverterCode.convert(null, elementValue));
  }

  @Test
  void shallIncludeId() {
    final var elementValueCode =
        ElementValueCode.builder().codeId(new FieldId(CODE_ID)).code(CODE).build();

    final var certificateDataValue =
        (CertificateDataValueCode)
            certificateDataValueConverterCode.convert(null, elementValueCode);

    assertEquals(CODE_ID, certificateDataValue.getId());
  }

  @Test
  void shallIncludeCode() {
    final var elementValueCode =
        ElementValueCode.builder().codeId(new FieldId(CODE_ID)).code(CODE).build();

    final var certificateDataValue =
        (CertificateDataValueCode)
            certificateDataValueConverterCode.convert(null, elementValueCode);

    assertEquals(CODE, certificateDataValue.getCode());
  }
}
