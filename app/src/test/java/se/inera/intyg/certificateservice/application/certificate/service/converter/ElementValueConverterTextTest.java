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
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueDate;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueText;
import se.inera.intyg.certificateservice.application.certificate.dto.value.CertificateDataValueType;
import se.inera.intyg.certificateservice.domain.certificate.model.ElementValueText;
import se.inera.intyg.certificateservice.domain.certificatemodel.model.FieldId;

class ElementValueConverterTextTest {

  private static final String TEXT_VALUE = "textValue";
  private static final String ID = "textId";
  private static final CertificateDataValueText CERTIFICATE_DATA_TEXT_VALUE =
      CertificateDataValueText.builder().id(ID).text(TEXT_VALUE).build();
  private static final FieldId TEXT_ID = new FieldId(ID);
  private ElementValueConverterText elementValueConverterText;

  @BeforeEach
  void setUp() {
    elementValueConverterText = new ElementValueConverterText();
  }

  @Test
  void shallThrowIfTypeIsNotCertificateDataTextValue() {
    final var certificateDataValueDate = CertificateDataValueDate.builder().build();
    final var illegalStateException =
        assertThrows(
            IllegalStateException.class,
            () -> elementValueConverterText.convert(certificateDataValueDate));
    assertTrue(illegalStateException.getMessage().contains("Invalid value type"));
  }

  @Test
  void shallReturnTypeText() {
    assertEquals(CertificateDataValueType.TEXT, elementValueConverterText.getType());
  }

  @Test
  void shallConvertId() {
    final var result = elementValueConverterText.convert(CERTIFICATE_DATA_TEXT_VALUE);
    final var actualResult = (ElementValueText) result;
    assertEquals(TEXT_ID, actualResult.textId());
  }

  @Test
  void shallConvertText() {
    final var result = elementValueConverterText.convert(CERTIFICATE_DATA_TEXT_VALUE);
    final var actualResult = (ElementValueText) result;
    assertEquals(TEXT_VALUE, actualResult.text());
  }
}
